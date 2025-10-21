package br.com.alura.Screenmatch.service;

import br.com.alura.Screenmatch.dto.EpisodioDTO;
import br.com.alura.Screenmatch.dto.SerieDTO;
import br.com.alura.Screenmatch.model.Categoria;
import br.com.alura.Screenmatch.model.Serie;
import br.com.alura.Screenmatch.repository.SerieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSerie() {
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(),
                        s.getAtores(), s.getPoster(), s.getSinopse()))
                .toList();
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(),
                    s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .toList();
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .toList();
    }

    public List<SerieDTO> obterSeriesPorCategoria(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converteDados(repositorio.findByGenero(categoria));
    }

    public List<EpisodioDTO> obterTopEpisodios(Long id) {
        var serie = repositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Série não encontrada"));
        return  repositorio.topEpisodiosPorSerie(serie)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .toList();
    }
}
