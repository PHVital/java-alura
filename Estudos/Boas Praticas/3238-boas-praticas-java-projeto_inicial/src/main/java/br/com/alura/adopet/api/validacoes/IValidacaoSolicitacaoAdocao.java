package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;

public interface IValidacaoSolicitacaoAdocao {
    void validar(SolicitacaoAdocaoDTO dto);
}
