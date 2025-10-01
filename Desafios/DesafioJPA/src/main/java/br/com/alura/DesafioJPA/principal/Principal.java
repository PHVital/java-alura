package br.com.alura.DesafioJPA.principal;

import br.com.alura.DesafioJPA.model.Artista;
import br.com.alura.DesafioJPA.model.Musica;
import br.com.alura.DesafioJPA.model.TipoArtista;
import br.com.alura.DesafioJPA.repository.ArtistaRepository;
import br.com.alura.DesafioJPA.service.ConsultaGemini;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ArtistaRepository repositorio;
    private Scanner leitura = new Scanner(System.in);

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() throws Exception {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    *** Screen Sound Music ***
                    1- Cadastrar artistas
                    2- Cadastrar músicas
                    3- Listar músicas
                    4- Buscar músicas por artista
                    5- Pesquisar dados sobre um artista
                    
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtistas();
                    break;
                case 2:
                    cadastrarMusicas();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                case 5:
                    pesquisarDadosDoArtista();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void cadastrarArtistas() {
        System.out.println("Informe o nome do artista: ");
        var nome = leitura.nextLine().trim();
        System.out.println("Informe o tipo desse artista (solo, dupla ou banda): ");
        var tipo = leitura.nextLine().trim();
        TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
        Artista artista = new Artista(nome, tipoArtista);

        repositorio.saveAndFlush(artista);
        System.out.println("Artista cadastrado com sucesso!");
    }

    private void cadastrarMusicas() {
        System.out.println("Cadastrar música de que artista?");
        var nomeArtista = leitura.nextLine().trim();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nomeArtista);

        if (artista.isPresent()) {
            System.out.println("Informe o título da música: ");
            var nomeMusica = leitura.nextLine().trim();
            System.out.println("Informe o nome do álbum: ");
            var nomeAlbum = leitura.nextLine().trim();
            Musica musica = new Musica(nomeMusica, nomeAlbum);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);

            repositorio.saveAndFlush(artista.get());
            System.out.println("Música cadastrada com sucesso!");
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

    public void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(m -> {
            System.out.println("Música: " + m.getTitulo() + " - Artista: " + a.getNome());
        }));
    }

    public void buscarMusicasPorArtista() {
        System.out.println("Buscar músicas de que artista?");
        var nomeArtista = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nomeArtista);

        if (artista.isPresent()) {
            System.out.println("Músicas de " + artista.get().getNome() + ":");
            artista.get().getMusicas().forEach(m ->
                    System.out.println("- " + m.getTitulo()));
        } else {
            System.out.println("Artista não encontrado!");
        }
    }

    private void pesquisarDadosDoArtista() throws Exception {
        System.out.println("Sobre qual artista você quer saber mais?");
        var nome = leitura.nextLine();
        var resposta = ConsultaGemini.obterInformacao(nome);
        System.out.println(resposta.trim());
    }
}
