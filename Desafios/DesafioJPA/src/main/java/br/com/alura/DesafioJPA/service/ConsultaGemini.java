package br.com.alura.DesafioJPA.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

record Part(String text) {}
record Content(List<Part> parts) {}
record Candidate(Content content) {}
record GeminiResponse(List<Candidate> candidates) {}

public class ConsultaGemini {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + API_KEY;

    public static String obterInformacao(String nomeArtista) throws Exception {
        if (API_KEY == null) {
            throw new IllegalStateException("Variável de ambiente API_KEY não está definida!");
        }

        String prompt = "Me fale um pouco sobre o artista: " + nomeArtista;

        String requestBody = """
               {
                 "contents": [
                   {
                     "parts": [
                       {
                         "text": "%s"
                       }
                     ]
                   }
                 ]
               }
               """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            GeminiResponse geminiResponse = mapper.readValue(response.body(), GeminiResponse.class);

            return geminiResponse.candidates().get(0).content().parts().get(0).text();
        } else {
            return "Erro ao consultar a API do Gemini: " + response.body();
        }
    }
}
