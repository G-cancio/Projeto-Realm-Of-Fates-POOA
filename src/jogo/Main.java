package jogo; // declara que esta classe pertence ao pacote "jogo"

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
            System.out.println("[2] Ver Problema (sem padrao)");
            System.out.println("[3] Sair");

            int op = CriadorPersonagem.lerOpcao(1, 3); // le a opcao do jogador

            if (op == 1) iniciarBatalha();  // cria personagens e briga
            if (op == 2) exibirProblema();  // demonstra os problemas do codigo
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
        Personagem p1 = CriadorPersonagem.criarPersonagem("Jogador 1"); // cria o personagem do jogador 1

        System.out.println("\n=== JOGADOR 2, crie seu personagem! ===");
        Personagem p2 = CriadorPersonagem.criarPersonagem("Jogador 2"); // cria o personagem do jogador 2

        // cria a batalha passando os dois personagens e a inicia
        Batalha batalha = new Batalha(p1, p2);
        batalha.iniciar();

        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine(); // espera o jogador pressionar ENTER
    }

    /**
     * Demonstra os dois problemas que o padrao Builder resolve.
     * Cria personagens diretamente no codigo (sem menu) para mostrar
     * como o construtor telescopico e os setters livres sao problematicos.
     */
    private static void exibirProblema() {
        System.out.println("\n=== DEMONSTRACAO DO PROBLEMA — SEM PADRAO ===");

        // --- PROBLEMA 1: Construtor Telescopico ---
        // Todos os 11 parametros precisam ser passados na ordem correta.
        // Atributos opcionais exigem null explicito — confuso e fragil.
        System.out.println("\n--- PROBLEMA 1: Construtor Telescopico ---");
        System.out.println("Criando Gandalf — quem sabe o que e cada null?");

        Personagem gandalf = new Personagem(
            "Gandalf",       // nome
            "Mago",          // classe
            "Humano",        // raca
            8, 20, 10, 12,   // forca, inteligencia, destreza, resistencia
            "Cajado Arcano", // armaPrincipal
            null,            // armadura — obrigado a passar null para omitir
            null,            // habilidades — obrigado a passar null para omitir
            null             // background — obrigado a passar null para omitir
        );
        System.out.println(gandalf); // exibe o personagem criado

        // --- PROBLEMA 2: Setters Livres ---
        // O objeto e criado vazio e preenchido um setter por vez.
        // Entre a primeira e a ultima linha, o objeto esta incompleto.
        // Qualquer codigo que acessar "legolas" antes do fim vai ler dados invalidos.
        System.out.println("\n--- PROBLEMA 2: Setters Livres ---");
        System.out.println("Objeto existe ANTES de estar pronto:");

        Personagem legolas = new Personagem(); // INVALIDO: forca=0, raca=null, hp=0
        legolas.setNome("Legolas");
        legolas.setClasse("Arqueiro");

        // neste ponto: forca=0, raca=null, hpMaximo=0 — objeto ainda invalido
        System.out.println("Apos 2 setters (forca=0, raca=null, hp=0):");
        System.out.println(legolas);

        // continua preenchendo os atributos restantes
        legolas.setRaca("Elfo");
        legolas.setForca(12);
        legolas.setInteligencia(15);
        legolas.setDestreza(20);
        legolas.setResistencia(10);
        legolas.inicializarHP(); // precisa chamar manualmente — facil de esquecer

        System.out.println("\nApos todos os setters:");
        System.out.println(legolas);

        // conclusao do problema
        System.out.println("\nCONCLUSAO:");
        System.out.println("  - Nenhuma abordagem garante objeto completo ao ser usado.");
        System.out.println("  - Nenhuma valida os dados inseridos.");
        System.out.println("  - O padrao Builder resolve esses problemas.");

        System.out.println("\nPressione ENTER para voltar...");
        scanner.nextLine(); // espera o jogador pressionar ENTER
    }
}
