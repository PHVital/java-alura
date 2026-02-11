package med.voll.web_application.domain.usuario;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.paciente.DadosCadastroPublicoPaciente;
import med.voll.web_application.domain.usuario.email.EmailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public Long salvarUsuario(String nome, String email, Perfil perfil) {
        String primeiraSenha = UUID.randomUUID().toString().substring(0, 8);
        String senhaCriptografada = passwordEncoder.encode(primeiraSenha);
        var usuario = usuarioRepository.save(new Usuario(nome, email, senhaCriptografada, perfil));
        emailService.enviarEmailSenhaAleatoria(usuario, primeiraSenha);
        return usuario.getId();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void alterarSenha(DadosAlteracaoSenha dados, Usuario logado) {
        if (!passwordEncoder.matches(dados.senhaAtual(), logado.getPassword()))
            throw new RegraDeNegocioException("Senha digitada não confere com a senha atual");
        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao()))
            throw new RegraDeNegocioException("Senha e confirmação não conferem");

        String senhaCriptograda = passwordEncoder.encode(dados.novaSenha());
        logado.alterarSenha(senhaCriptograda);
        logado.setSenhaAlterada(true);
        usuarioRepository.save(logado);
        atualizarUsuarioSpringSecurity(logado);
    }

    public void enviarToken(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new RegraDeNegocioException("Usuário não encontrado")
        );
        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        emailService.enviarEmailSenha(usuario);
    }

    private static void atualizarUsuarioSpringSecurity(Usuario logado) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var newAuth = new UsernamePasswordAuthenticationToken(logado, null, authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void recuperarConta(String codigo, DadosRecuperacaoConta dados) {
        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(codigo)
                .orElseThrow(
                        () -> new RegraDeNegocioException("Link inválido")
                );

        if (usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Link expirado");
        }

        if (!dados.novaSenha().equals(dados.novaSenhaConfirmacao()))
            throw new RegraDeNegocioException("Senha e confirmação não conferem");

        String senhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        usuario.alterarSenha(senhaCriptografada);

        usuario.setToken(null);
        usuario.setExpiracaoToken(null);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void registrarPaciente(DadosCadastroPublicoPaciente dados) {

        if (usuarioRepository.findByEmailIgnoreCase(dados.email()).isPresent()) {
            throw new RegraDeNegocioException("E-mail já cadastrado");
        }

        String senhaCriptografada = passwordEncoder.encode(dados.senha());

        var usuario = new Usuario(
                dados.nome(),
                dados.email(),
                senhaCriptografada,
                Perfil.PACIENTE
        );

        usuario.setAtivo(false);

        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusHours(24));

        usuarioRepository.save(usuario);

        emailService.enviarEmailConfirmacao(usuario);
    }

    public Usuario findUsuario(String token) {
        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(token)
                .orElseThrow(
                        () -> new RegraDeNegocioException("Token Invalido")
                );

        return usuario;
    }

}
