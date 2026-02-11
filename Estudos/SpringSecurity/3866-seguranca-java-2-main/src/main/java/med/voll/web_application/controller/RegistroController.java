package med.voll.web_application.controller;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.paciente.DadosCadastroPublicoPaciente;
import med.voll.web_application.domain.usuario.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("registro")
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String carregarFormulario(Model model) {
        model.addAttribute("dados", new DadosCadastroPublicoPaciente("", "", "", "", ""));
        return "autenticacao/formulario-registro";
    }

    @PostMapping
    public String registrar(@Valid @ModelAttribute("dados") DadosCadastroPublicoPaciente dados,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            return "autenticacao/formulario-registro";
        }

        try {
            usuarioService.registrarPaciente(dados);
            return "redirect:/login?cadastroRealizado";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            return "autenticacao/formulario-registro";
        }
    }
}
