package br.com.alura.contador;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class ContadorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ContadorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Digite um numero: ");
		int n = sc.nextInt();

		for (int i = 1; i <= n; i++) {
			System.out.println(i);
		}

		Tarefa tarefa = new Tarefa("Estudar", false, "Eu");
		ObjectMapper mapper = new ObjectMapper();

		try {
			File arquivo = new File("tarefa.json");
			mapper.writeValue(arquivo, tarefa);
			System.out.println("Tarefa salva com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			File arquivo = new File("tarefa.json");
			Tarefa tarefa2 = mapper.readValue(arquivo, Tarefa.class);
			System.out.println(tarefa2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
