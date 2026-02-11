package med.voll.web_application.domain.paciente;

public record DadosCadastroPublicoPaciente(String nome,
                                           String email,
                                           String senha,
                                           String telefone,
                                           String cpf) {
}
