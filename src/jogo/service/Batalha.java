package jogo.service;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import jogo.model.Personagem;
import jogo.util.Leitura;

/**
 * Gerencia o combate entre dois personagens em turnos alternados.
 * Controla ações como ataque normal, ataque pesado, defesa e uso de habilidades.
 * Exibe status a cada turno e declara o vencedor ao final.
 */
public class Batalha {

    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    private Personagem jogador1;
    private Personagem jogador2;

    private boolean j1Defendendo = false;
    private boolean j2Defendendo = false;

    private int turno = 1;

    /**
     * Construtor que recebe os dois combatentes.
     *
     * @param jogador1 primeiro personagem (ataca primeiro)
     * @param jogador2 segundo personagem
     */
    public Batalha(Personagem jogador1, Personagem jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }

    /**
     * Inicia o loop da batalha enquanto ambos estiverem vivos.
     * A cada iteração, executa um turno para cada jogador alternadamente.
     */
    public void iniciar() {
        System.out.println("\n=== A BATALHA COMECA! ===\n");

        while (jogador1.estaVivo() && jogador2.estaVivo()) {
            executarTurno(jogador1, jogador2, "JOGADOR 1", true);
            if (!jogador2.estaVivo()) break;
            executarTurno(jogador2, jogador1, "JOGADOR 2", false);
            turno++;
        }
        exibirResultado();
    }

    /**
     * Executa um turno completo para um personagem atacante contra um defensor.
     * Exibe menu de ações, lê a escolha e delega para o método específico.
     *
     * @param atacante personagem que age no turno
     * @param defensor personagem que sofre as ações
     * @param label    identificador textual ("JOGADOR 1" ou "JOGADOR 2")
     * @param j1Vez    {@code true} se for a vez do jogador 1 (para controle de estado defensivo)
     */
    private void executarTurno(Personagem atacante, Personagem defensor, String label, boolean j1Vez) {
        exibirStatus();
        System.out.println("--- TURNO " + turno + " | VEZ DE: " + atacante.getNome() + " (" + label + ") ---");

        System.out.println("\nEscolha sua acao:");
        System.out.println("  [1] Atacar        — Dano normal");
        System.out.println("  [2] Ataque Pesado — +50% dano, 70% de chance de acertar");
        System.out.println("  [3] Defender      — Reduz 50% do proximo dano recebido");

        List<String> habilidades = atacante.getHabilidades();
        boolean temHabilidade = habilidades != null && !habilidades.isEmpty();
        if (temHabilidade) {
            System.out.println("  [4] Usar Habilidade");
        }

        int maximoOpcoes = temHabilidade ? 4 : 3;
        int acao = Leitura.lerOpcao(1, maximoOpcoes);

        switch (acao) {
            case 1 -> atacar(atacante, defensor, j1Vez);
            case 2 -> ataquePesado(atacante, defensor, j1Vez);
            case 3 -> defender(atacante, j1Vez);
            case 4 -> usarHabilidade(atacante, defensor);
        }

        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Executa um ataque normal: calcula dano, aplica defesa e possível estado de defesa do oponente.
     * Chance de 10% de acerto crítico (+50% dano).
     *
     * @param atacante  quem ataca
     * @param defensor  quem defende
     * @param j1Atacou  indica se o atacante é o jogador 1 (para controle de flag de defesa)
     */
    private void atacar(Personagem atacante, Personagem defensor, boolean j1Atacou) {
        int dano = atacante.calcularDano();
        int defesa = defensor.calcularDefesa();

        boolean defendendo = j1Atacou ? j2Defendendo : j1Defendendo;
        if (defendendo) {
            dano = dano / 2;
            if (j1Atacou) j2Defendendo = false;
            else j1Defendendo = false;
            System.out.println("  Defesa ativada! Dano reduzido a metade.");
        }

        boolean critico = random.nextInt(10) == 0;
        if (critico) {
            dano = dano + (dano / 2);
            System.out.println("  GOLPE CRITICO!");
        }

        int danoFinal = dano - defesa;
        if (danoFinal < 1) danoFinal = 1;

        defensor.receberDano(danoFinal);
        System.out.println("  " + atacante.getNome() + " atacou " + defensor.getNome() + "!");
        System.out.println("  Dano: " + dano + " | Defesa: " + defesa + " | Dano final: " + danoFinal);
    }

    /**
     * Ataque pesado: +50% de dano mas com 70% de chance de acerto.
     * Se errar, não causa dano.
     *
     * @param atacante  quem ataca
     * @param defensor  quem defende
     * @param j1Atacou  indica se é jogador 1
     */
    private void ataquePesado(Personagem atacante, Personagem defensor, boolean j1Atacou) {
        System.out.println("  " + atacante.getNome() + " prepara um ataque pesado...");

        boolean acertou = random.nextInt(10) < 7;
        if (acertou) {
            int dano = atacante.calcularDano() + (atacante.calcularDano() / 2);
            int defesa = defensor.calcularDefesa();

            boolean defendendo = j1Atacou ? j2Defendendo : j1Defendendo;
            if (defendendo) {
                dano = dano / 2;
                if (j1Atacou) j2Defendendo = false;
                else j1Defendendo = false;
                System.out.println("  Defesa ativada! Dano reduzido a metade.");
            }

            int danoFinal = dano - defesa;
            if (danoFinal < 1) danoFinal = 1;
            defensor.receberDano(danoFinal);
            System.out.println("  Acertou! Dano final: " + danoFinal);
        } else {
            System.out.println("  Errou! O ataque nao conectou.");
        }
    }

    /**
     * Ativa o modo defesa para o personagem, reduzindo pela metade o dano do próximo ataque recebido.
     *
     * @param personagem  o personagem que defende
     * @param j1Defendeu  {@code true} se for o jogador 1 defendendo
     */
    private void defender(Personagem personagem, boolean j1Defendeu) {
        System.out.println("  " + personagem.getNome() + " assume postura defensiva!");
        System.out.println("  O proximo dano recebido sera reduzido em 50%.");
        if (j1Defendeu) j1Defendendo = true;
        else j2Defendendo = true;
    }

    /**
     * Permite usar uma habilidade especial do personagem atacante.
     * Se o nome da habilidade contiver "Cura", recupera 20 HP do próprio atacante.
     * Caso contrário, causa dano extra (dano base + 15) no defensor.
     *
     * @param atacante  quem usa a habilidade
     * @param defensor  alvo da habilidade (se for ofensiva)
     */
    private void usarHabilidade(Personagem atacante, Personagem defensor) {
        List<String> habilidades = atacante.getHabilidades();
        System.out.println("\n  Escolha a habilidade:");
        for (int i = 0; i < habilidades.size(); i++) {
            System.out.println("    [" + (i + 1) + "] " + habilidades.get(i));
        }

        int idx = Leitura.lerOpcao(1, habilidades.size()) - 1;
        String hab = habilidades.get(idx);
        System.out.println("  " + atacante.getNome() + " usa " + hab + "!");

        if (hab.contains("Cura")) {
            atacante.receberCura(20);
            System.out.println("  " + atacante.getNome() + " recuperou 20 HP!");
        } else {
            int danoExtra = atacante.calcularDano() + 15;
            int defesa = defensor.calcularDefesa();

            boolean j1Atacou = (atacante == jogador1);
            boolean defendendo = j1Atacou ? j2Defendendo : j1Defendendo;
            if (defendendo) {
                danoExtra = danoExtra / 2;
                if (j1Atacou) j2Defendendo = false;
                else j1Defendendo = false;
                System.out.println("  Defesa ativada! Dano reduzido a metade.");
            }

            int danoFinal = danoExtra - defesa;
            if (danoFinal < 1) danoFinal = 1;
            defensor.receberDano(danoFinal);
            System.out.println("  Dano especial: " + danoFinal);
        }
    }

    /** Exibe a barra de HP de ambos os personagens. */
    private void exibirStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println(jogador1.getNome() + " HP: " + jogador1.barraHP());
        System.out.println(jogador2.getNome() + " HP: " + jogador2.barraHP());
        System.out.println();
    }

    /** Exibe o vencedor e o HP final do vencedor. */
    private void exibirResultado() {
        System.out.println("\n=== FIM DE BATALHA ===");
        if (jogador1.estaVivo()) {
            System.out.println("VENCEDOR: " + jogador1.getNome());
            System.out.println("Derrotado: " + jogador2.getNome());
            System.out.println("HP final: " + jogador1.barraHP());
        } else {
            System.out.println("VENCEDOR: " + jogador2.getNome());
            System.out.println("Derrotado: " + jogador1.getNome());
            System.out.println("HP final: " + jogador2.barraHP());
        }
    }
}