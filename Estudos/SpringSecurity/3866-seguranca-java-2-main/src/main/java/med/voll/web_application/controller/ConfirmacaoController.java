package med.voll.web_application.controller;

import jakarta.transaction.Transactional;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ConfirmacaoController {

    private final UsuarioService usuarioService;

    public ConfirmacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/confirmar-conta")
    @Transactional
    public String confirmarConta(@RequestParam String token) {

        var usuario = usuarioService.findUsuario(token);

        if (usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Token expirado");
        }

        usuario.ativarConta();

        return "conta-confirmada";
    }
}
