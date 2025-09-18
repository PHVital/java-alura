package br.com.alura.gerenciador_pedidos.repository;

import br.com.alura.gerenciador_pedidos.modelos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
