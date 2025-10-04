package br.com.alura.Screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {
    @GetMapping("/series")
    public String obterSerie() {
        return "Aqui vão ser listadas as séries";
    }
}
