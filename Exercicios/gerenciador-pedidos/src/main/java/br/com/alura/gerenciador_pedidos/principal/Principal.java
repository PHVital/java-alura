package br.com.alura.gerenciador_pedidos.principal;

import br.com.alura.gerenciador_pedidos.modelos.Categoria;
import br.com.alura.gerenciador_pedidos.modelos.Pedido;
import br.com.alura.gerenciador_pedidos.modelos.Produto;
import br.com.alura.gerenciador_pedidos.repository.CategoriaRepository;
import br.com.alura.gerenciador_pedidos.repository.PedidoRepository;
import br.com.alura.gerenciador_pedidos.repository.ProdutoRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Principal {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PedidoRepository pedidoRepository;

    public Principal(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public void salvarPedido() {
    Pedido pedido = new Pedido(LocalDate.now());
    Produto produto = new Produto("Notebook", 3500.0);
    Categoria categoria = new Categoria("Eletrônicos");

    pedidoRepository.save(pedido);
    produtoRepository.save(produto);
    categoriaRepository.save(categoria);
    }

}
