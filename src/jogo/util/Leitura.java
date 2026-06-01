package jogo.util;

import java.util.Scanner;

public class Leitura {

    private static Scanner scanner = new Scanner(System.in);
    /**
     * Pede ao jogador que escolha uma opcao do menu entre min e max.
     * Fica em loop ate receber um numero valido dentro do intervalo.
     * "public" porque tambem e usado pela classe Batalha.
     */
    public static int lerOpcao(int minimo, int maximo) {
        while (true) {
            System.out.print("  Opcao [" + minimo + "-" + maximo + "]: ");
            try {
                int opcao = scanner.nextInt();
                if (opcao >= minimo && opcao <= maximo) return opcao;
                System.out.println("  Opcao invalida.");
            } catch (NumberFormatException e) {
                System.out.println("  Digite um numero.");
            }
        }
    }

    public static int lerAtributo(String nome, int maximo) {
        while (true) {
            System.out.print("  " + nome + " (1 a " + maximo + "): ");
            try {
                int valor = scanner.nextInt();
                if (valor >= 1 && valor <= maximo) return valor;
                System.out.println("  Valor invalido.");
            } catch (NumberFormatException e) {
                System.out.println("  Digite um numero.");
            }
        }
    }
}
