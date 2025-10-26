package br.com.alura.GeradordeFrases.controller;

import br.com.alura.GeradordeFrases.dto.FrasesDTO;
import br.com.alura.GeradordeFrases.repository.FrasesRepository;
import br.com.alura.GeradordeFrases.service.FrasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/series")
public class FrasesController {
    @Autowired
    FrasesService service;

    @GetMapping("/frases")
    public FrasesDTO obterFraseAleatoria() {
        return service.obterFraseAleatoria();
    }
}
