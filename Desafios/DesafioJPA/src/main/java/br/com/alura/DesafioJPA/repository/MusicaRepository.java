package br.com.alura.DesafioJPA.repository;

import br.com.alura.DesafioJPA.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicaRepository extends JpaRepository<Musica, Long> {
    @Query("SELECT m FROM Artista a JOIN a.musicas m WHERE a.nome ILIKE %:nomeArtista%")
    List<Musica> buscaMusicasPorArtista(String nomeArtista);
}
