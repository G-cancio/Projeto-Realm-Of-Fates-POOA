package jogo;
import jogo.service.Batalha;
import jogo.service.CriadorPersonagem;
import jogo.model.Personagem;
import jogo.util.Leitura;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    /**
     * Ponto de entrada do programa.
     * O Java sempre comeca a execucao por este metodo.
     */
    public static void main(String[] args) {
        System.out.println("=== REALM OF FATES ===\n");

        boolean rodando = true;
        while (rodando) {
            System.out.println("[1] Nova Batalha");
            System.out.println("[2] Sair");

            int op = Leitura.lerOpcao(1, 2);

            if (op == 1) iniciarBatalha();  // cria personagens e briga
            if (op == 2) rodando = false;   // encerra o loop
        }

        System.out.println("Ate a proxima aventura!");
    }

    /**
     * Cria os dois personagens via menu interativo e inicia a batalha.
     * Cada jogador passa pelo processo completo de criacao antes de lutar.
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
