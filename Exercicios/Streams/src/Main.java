import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1 - Dada a lista de números inteiros abaixo, filtre apenas os números pares e imprima-os.
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);
        numeros.stream()
                .filter(n -> n % 2 == 0)
                .forEach(System.out::println);

        // 2 - Dada a lista de strings abaixo, converta todas para letras maiúsculas e imprima-as.
        List<String> palavras = Arrays.asList("java", "stream", "lambda");
        palavras.stream()
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);


        // 3 - Dada a lista de números inteiros abaixo, filtre os números ímpares, multiplique cada um por 2 e colete os resultados em uma nova lista.
        numeros.stream()
                .filter(n -> n % 2 != 0)
                .map(n -> n * 2)
                .toList();

        // 4 - Dada a lista de strings abaixo, remova as duplicatas (palavras que aparecem mais de uma vez) e imprima o resultado.
        palavras = Arrays.asList("apple", "banana", "apple", "orange", "banana");
        palavras.stream()
                .distinct()
                .forEach(System.out::println);

        // 5 - Dada a lista de sublistas de números inteiros abaixo, extraia todos os números primos em uma única lista e os ordene em ordem crescente.
        List<List<Integer>> listaDeNumeros = Arrays.asList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(5, 6, 7, 8),
                Arrays.asList(9, 10, 11, 12)
        );
        listaDeNumeros.stream()
                .flatMap(List::stream)
                .filter(Main::ehPrimo)
                .sorted()
                .toList();
    }

    public static boolean ehPrimo(int numero) {
        if (numero < 2) return false;
        for (int i = 2; i <= Math.sqrt(numero); i++) {
            if (numero % i == 0) {
                return false;
            }
        }
        return true;
    }
}