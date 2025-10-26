package br.com.alura.GeradordeFrases.repository;

import br.com.alura.GeradordeFrases.dto.FrasesDTO;
import br.com.alura.GeradordeFrases.model.Frase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FrasesRepository extends JpaRepository<Frase, Long> {
    @Query("SELECT f FROM Frase f order by function('RANDOM') LIMIT 1")
    Frase fraseAleatoria();
}
