package jogo; // declara que esta classe pertence ao pacote "jogo"

import jogo.service.Batalha;
import jogo.service.CriadorPersonagem;
import jogo.model.Personagem;

import java.util.Scanner; // importa Scanner para ler o ENTER do jogador

/**
 * Classe principal do jogo Realm of Fates.
 * E aqui que o programa comeca (metodo main).
 *
 * O menu oferece tres opcoes:
 * 1. Nova Batalha — cria dois personagens e inicia a luta
 * 2. Ver Problema — demonstra os dois problemas sem o padrao Builder
 * 3. Sair         — encerra o programa
 */
public class Main {

    // Scanner para ler o ENTER do jogador ao voltar ao menu
    static Scanner scanner = new Scanner(System.in);

    /**
     * Ponto de entrada do programa.
     * O Java sempre comeca a execucao por este metodo.
     */
    public static void main(String[] args) {
        System.out.println("=== REALM OF FATES ===\n");

        // loop principal do menu — continua ate o jogador escolher "Sair"
        boolean rodando = true;
        while (rodando) {
            System.out.println("[1] Nova Batalha");
            System.out.println("[2] Sair");

            int op = CriadorPersonagem.lerOpcao(1, 3); // le a opcao do jogador

            if (op == 1) iniciarBatalha();  // cria personagens e briga
            if (op == 3) rodando = false;   // encerra o loop
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
