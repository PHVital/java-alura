package br.com.alura.tabelaFipe.principal;

import br.com.alura.tabelaFipe.model.DadosMarca;
import br.com.alura.tabelaFipe.model.DadosModelo;
import br.com.alura.tabelaFipe.model.Veiculo;
import br.com.alura.tabelaFipe.service.ConsumoApi;
import br.com.alura.tabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner scan = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() {
        System.out.println("**** OPÇÕES ****");
        System.out.println("Carro");
        System.out.println("Moto");
        System.out.println("Caminhão");
        System.out.println("\nDigite uma das opções para consultar: ");

        var opcao = scan.nextLine();

        String endereco;
        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/";
        } else {
            endereco = URL_BASE + "caminhoes/";
        }
        var json = consumoApi.obterDados(endereco + "/marcas");
        var marcas = converteDados.obterLista(json, DadosMarca.class);
        marcas.stream()
                .sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var codigo = scan.nextLine();

        endereco = endereco + "marcas/" + codigo + "/modelos";
        json = consumoApi.obterDados(endereco);
        var modeloLista = converteDados.obterDados(json, DadosModelo.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do nome do carro a ser buscado: ");
        var nomeVeiculo = scan.nextLine();

        List<DadosMarca> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("\nModelos filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo: ");
        var codigoModelo = scan.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumoApi.obterDados(endereco);
        List<DadosMarca> anos = converteDados.obterLista(json, DadosMarca.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = converteDados.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados por ano: ");
        veiculos.stream()
                .sorted(Comparator.comparing(Veiculo::ano))
                .forEach(System.out::println);
    }
}
