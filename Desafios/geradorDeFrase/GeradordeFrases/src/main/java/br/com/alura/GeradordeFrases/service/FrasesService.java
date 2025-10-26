package br.com.alura.GeradordeFrases.service;

import br.com.alura.GeradordeFrases.dto.FrasesDTO;
import br.com.alura.GeradordeFrases.model.Frase;
import br.com.alura.GeradordeFrases.repository.FrasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrasesService {
    @Autowired
    private FrasesRepository repository;

    public FrasesDTO obterFraseAleatoria() {
        Frase frase = repository.fraseAleatoria();
        return new FrasesDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonagem(), frase.getPoster());
    }
}
