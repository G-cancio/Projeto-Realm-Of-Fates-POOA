package jogo.util;

import java.util.Scanner;

/**
 * Classe utilitária para leitura de dados do console com validação de entrada.
 * Fornece métodos seguros para ler opções numéricas dentro de intervalos e
 * para distribuição de pontos em atributos.
 */
public class Leitura {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Lê um número inteiro do console dentro de um intervalo fechado [minimo, maximo].
     * Repete a solicitação enquanto a entrada for inválida (não numérica ou fora do intervalo).
     *
     * @param minimo valor mínimo aceito (inclusive)
     * @param maximo valor máximo aceito (inclusive)
     * @return o número válido digitado pelo usuário
     */
    public static int lerOpcao(int minimo, int maximo) {
        while (true) {
            System.out.print("  Opcao [" + minimo + "-" + maximo + "]: ");
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                if (opcao >= minimo && opcao <= maximo) return opcao;
                System.out.println("  Opcao invalida.");
            } catch (NumberFormatException e) {
                System.out.println("  Digite um numero.");
            }
        }
    }

    /**
     * Lê um valor inteiro para um atributo, respeitando um limite máximo.
     * Utilizado na distribuição de pontos de atributos durante a criação do personagem.
     *
     * @param nome   nome do atributo (exibido na mensagem)
     * @param maximo valor máximo permitido (mínimo sempre 1)
     * @return o valor escolhido pelo usuário (entre 1 e maximo)
     */
    public static int lerAtributo(String nome, int maximo) {
        while (true) {
            System.out.print("  " + nome + " (1 a " + maximo + "): ");
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 1 && valor <= maximo) return valor;
                System.out.println("  Valor invalido.");
            } catch (NumberFormatException e) {
                System.out.println("  Digite um numero.");
            }
        }
    }
}