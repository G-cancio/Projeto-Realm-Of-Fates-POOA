package jogo; // declara que esta classe pertence ao pacote "jogo"

import java.util.List;   // importa List para trabalhar com a lista de habilidades
import java.util.Random; // importa Random para gerar numeros aleatorios (critico, chance de erro)
import java.util.Scanner; // importa Scanner para ler ENTER do teclado entre os turnos

/**
 * Gerencia a batalha por turnos entre dois personagens.
 *
 * O fluxo da batalha e:
 * 1. Jogador 1 escolhe uma acao
 * 2. A acao e executada contra o Jogador 2
 * 3. Se Jogador 2 ainda estiver vivo, e a vez dele
 * 4. Repete ate um dos dois chegar a 0 de HP
 */
public class Batalha {

    // Scanner para ler o ENTER que o jogador pressiona entre os turnos
    Scanner scanner = new Scanner(System.in);

    // Random para gerar numeros aleatorios (chance de critico, chance de errar ataque pesado)
    Random random = new Random();

    // os dois personagens que vao lutar
    Personagem jogador1;
    Personagem jogador2;

    // flags (verdadeiro/falso) que indicam se cada jogador esta em postura defensiva
    // quando true, o proximo dano recebido sera reduzido a metade
    boolean j1Defendendo = false;
    boolean j2Defendendo = false;

    // contador de turnos para exibir no terminal
    int turno = 1;

    /**
     * Construtor: recebe os dois personagens que vao batalhar.
     */
    public Batalha(Personagem jogador1, Personagem jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }

    /**
     * Inicia e controla o loop principal da batalha.
     * Continua executando turnos ate que um dos jogadores chegue a 0 de HP.
     */
    public void iniciar() {
        System.out.println("\n=== A BATALHA COMECA! ===\n");

        // loop continua enquanto os dois estiverem vivos
        while (jogador1.estaVivo() && jogador2.estaVivo()) {

            // turno do jogador 1: ele ataca o jogador 2
            // o ultimo parametro "true" indica que e o jogador 1 que esta agindo
            executarTurno(jogador1, jogador2, "JOGADOR 1", true);

            // verifica se jogador 2 morreu apos o ataque do jogador 1
            // se sim, sai do loop sem dar turno ao jogador 2
            if (!jogador2.estaVivo()) break;

            // turno do jogador 2: ele ataca o jogador 1
            // o parametro "false" indica que NAO e o jogador 1 agindo (e o jogador 2)
            executarTurno(jogador2, jogador1, "JOGADOR 2", false);

            turno++; // incrementa o contador de turnos
        }

        exibirResultado(); // mostra quem venceu
    }

    /**
     * Executa um turno para o atacante.
     * Exibe o menu de acoes e chama o metodo correspondente a escolha.
     *
     * atacante: o personagem que esta agindo neste turno
     * defensor: o personagem que vai receber a acao
     * label: texto "JOGADOR 1" ou "JOGADOR 2" para exibir no terminal
     * j1Vez: true se for a vez do jogador 1, false se for a vez do jogador 2
     */
    private void executarTurno(Personagem atacante, Personagem defensor, String label, boolean j1Vez) {
        exibirStatus(); // mostra HP dos dois antes de cada turno
        System.out.println("--- TURNO " + turno + " | VEZ DE: " + atacante.getNome() + " (" + label + ") ---");

        // menu de acoes
        System.out.println("\nEscolha sua acao:");
        System.out.println("  [1] Atacar        — Dano normal");
        System.out.println("  [2] Ataque Pesado — +50% dano, 70% de chance de acertar");
        System.out.println("  [3] Defender      — Reduz 50% do proximo dano recebido");

        // verifica se o personagem tem habilidades para mostrar a opcao 4
        List<String> habilidades = atacante.getHabilidades();
        boolean temHabilidade = habilidades != null && !habilidades.isEmpty();
        if (temHabilidade) {
            System.out.println("  [4] Usar Habilidade");
        }

        // define o maximo de opcoes com base em se tem habilidade ou nao
        int maxOp = temHabilidade ? 4 : 3;
        int acao = CriadorPersonagem.lerOpcao(1, maxOp); // le a escolha do jogador

        // executa a acao escolhida
        if (acao == 1) atacar(atacante, defensor, j1Vez);
        if (acao == 2) ataquePesado(atacante, defensor, j1Vez);
        if (acao == 3) defender(atacante, j1Vez);
        if (acao == 4) usarHabilidade(atacante, defensor);

        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine(); // espera o jogador pressionar ENTER antes de continuar
    }

    /**
     * Executa um ataque normal do atacante contra o defensor.
     *
     * Calcula o dano com base nos atributos do atacante.
     * Verifica se o defensor esta em postura defensiva (dano reduzido a metade).
     * Tem 10% de chance de ser um golpe critico (+50% de dano).
     * O dano final e pelo menos 1, mesmo que a defesa seja alta.
     *
     * j1Atacou: true se o atacante for o jogador 1 (para saber qual flag de defesa verificar)
     */
    private void atacar(Personagem atacante, Personagem defensor, boolean j1Atacou) {
        int dano = atacante.calcularDano(); // dano base do atacante
        int defesa = defensor.calcularDefesa(); // reducao de dano do defensor

        // verifica se o defensor esta defendendo:
        // se j1Atacou=true, o defensor e o jogador 2, entao verifica j2Defendendo
        // se j1Atacou=false, o defensor e o jogador 1, entao verifica j1Defendendo
        boolean defendendo = j1Atacou ? j2Defendendo : j1Defendendo;
        if (defendendo) {
            dano = dano / 2; // reduz o dano a metade

            // reseta a flag de defesa — so funciona uma vez por uso
            if (j1Atacou) j2Defendendo = false;
            else j1Defendendo = false;

            System.out.println("  Defesa ativada! Dano reduzido a metade.");
        }

        // sorteia um numero de 0 a 9; se cair 0, e critico (10% de chance)
        boolean critico = random.nextInt(10) == 0;
        if (critico) {
            dano = dano + (dano / 2); // aumenta o dano em 50%
            System.out.println("  GOLPE CRITICO!");
        }

        // dano final = dano do atacante - defesa do defensor
        int danoFinal = dano - defesa;
        if (danoFinal < 1) danoFinal = 1; // garante dano minimo de 1

        defensor.receberDano(danoFinal); // aplica o dano no defensor
        System.out.println("  " + atacante.getNome() + " atacou " + defensor.getNome() + "!");
        System.out.println("  Dano: " + dano + " | Defesa: " + defesa + " | Dano final: " + danoFinal);
    }

    /**
     * Executa um ataque pesado: causa +50% de dano, mas tem apenas 70% de chance de acertar.
     * Se errar, nenhum dano e causado.
     * Tambem verifica postura defensiva do defensor, igual ao ataque normal.
     */
    private void ataquePesado(Personagem atacante, Personagem defensor, boolean j1Atacou) {
        System.out.println("  " + atacante.getNome() + " prepara um ataque pesado...");

        // sorteia 0 a 9; valores 0 a 6 (7 numeros) representam 70% de chance de acertar
        boolean acertou = random.nextInt(10) < 7;
        if (acertou) {
            // dano com 50% a mais que o ataque normal
            int dano = atacante.calcularDano() + (atacante.calcularDano() / 2);
            int defesa = defensor.calcularDefesa();

            // mesma logica de defesa do metodo atacar()
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
            // 30% de chance: ataque falha completamente
            System.out.println("  Errou! O ataque nao conectou.");
        }
    }

    /**
     * Coloca o personagem em postura defensiva.
     * Na proxima vez que ele receber dano, o valor sera reduzido a metade.
     *
     * j1Defendeu: true se for o jogador 1 se defendendo, false se for o jogador 2
     */
    private void defender(Personagem personagem, boolean j1Defendeu) {
        System.out.println("  " + personagem.getNome() + " assume postura defensiva!");
        System.out.println("  O proximo dano recebido sera reduzido em 50%.");

        // ativa a flag de defesa do jogador correspondente
        if (j1Defendeu) j1Defendendo = true;
        else j2Defendendo = true;
    }

    /**
     * Permite que o atacante use uma de suas habilidades especiais.
     * Exibe a lista de habilidades e pede ao jogador que escolha uma.
     *
     * Se a habilidade contiver "Cura" no nome, ela cura o atacante.
     * Qualquer outra habilidade causa dano extra ao defensor.
     */
    private void usarHabilidade(Personagem atacante, Personagem defensor) {
        List<String> habilidades = atacante.getHabilidades();

        // exibe as habilidades disponiveis
        System.out.println("\n  Escolha a habilidade:");
        for (int i = 0; i < habilidades.size(); i++) {
            System.out.println("    [" + (i + 1) + "] " + habilidades.get(i));
        }

        // le a escolha e pega o nome da habilidade (indice - 1 para converter para base 0)
        int idx = CriadorPersonagem.lerOpcao(1, habilidades.size()) - 1;
        String hab = habilidades.get(idx);
        System.out.println("  " + atacante.getNome() + " usa " + hab + "!");

        // define o efeito com base no nome da habilidade
        if (hab.contains("Cura")) {
            // habilidade de cura: recupera 20 HP para o proprio atacante
            atacante.receberCura(20);
            System.out.println("  " + atacante.getNome() + " recuperou 20 HP!");
        } else {
            // qualquer outra habilidade: causa dano extra (+15) ao defensor
            int danoExtra = atacante.calcularDano() + 15;
            int danoFinal = danoExtra - defensor.calcularDefesa();
            if (danoFinal < 1) danoFinal = 1;
            defensor.receberDano(danoFinal);
            System.out.println("  Dano especial: " + danoFinal);
        }
    }

    /**
     * Exibe o HP atual dos dois jogadores no terminal.
     * Chamado no inicio de cada turno para manter o jogador informado.
     */
    private void exibirStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println(jogador1.getNome() + " HP: " + jogador1.barraHP());
        System.out.println(jogador2.getNome() + " HP: " + jogador2.barraHP());
        System.out.println();
    }

    /**
     * Exibe o resultado final da batalha.
     * Verifica qual jogador ainda esta vivo e o declara vencedor.
     */
    private void exibirResultado() {
        System.out.println("\n=== FIM DE BATALHA ===");

        // verifica qual jogador sobreviveu
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
