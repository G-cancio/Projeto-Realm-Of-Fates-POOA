package jogo;

import java.util.Scanner;
import jogo.model.Personagem;
import jogo.service.Batalha;
import jogo.service.CriadorPersonagem;
import jogo.util.Leitura;

/**
 * Ponto de entrada principal do jogo Realm of Fates.
 * Exibe um menu simples com duas opções: iniciar nova batalha ou sair.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Método principal invocado pela JVM.
     * Mantém o programa em loop até que o usuário escolha sair.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== REALM OF FATES ===\n");

        boolean rodando = true;
        while (rodando) {
            System.out.println("[1] Nova Batalha");
            System.out.println("[2] Sair");

            int op = Leitura.lerOpcao(1, 2);

            if (op == 1) iniciarBatalha();
            if (op == 2) rodando = false;
        }

        System.out.println("Ate a proxima aventura!");
    }

    /**
     * Cria dois personagens (Jogador 1 e Jogador 2) através do
     * {@link CriadorPersonagem} e inicia a batalha entre eles.
     * Após o fim da batalha, aguarda o usuário pressionar ENTER para voltar ao menu.
     */
    private static void iniciarBatalha() {
        System.out.println("\n=== JOGADOR 1, crie seu personagem! ===");
        Personagem p1 = CriadorPersonagem.criarPersonagem("Jogador 1");

        System.out.println("\n=== JOGADOR 2, crie seu personagem! ===");
        Personagem p2 = CriadorPersonagem.criarPersonagem("Jogador 2");

        Batalha batalha = new Batalha(p1, p2);
        batalha.iniciar();

        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }
}