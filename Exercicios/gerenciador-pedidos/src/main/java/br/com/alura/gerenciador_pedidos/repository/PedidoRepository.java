package br.com.alura.gerenciador_pedidos.repository;

import br.com.alura.gerenciador_pedidos.modelos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
