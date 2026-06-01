package jogo.service;

import jogo.model.Personagem;
import jogo.util.Leitura;

import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Batalha {

    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    Personagem jogador1;
    Personagem jogador2;

    boolean j1Defendendo = false;
    boolean j2Defendendo = false;

    int turno = 1;

    public Batalha(Personagem jogador1, Personagem jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
    }

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

    private void defender(Personagem personagem, boolean j1Defendeu) {
        System.out.println("  " + personagem.getNome() + " assume postura defensiva!");
        System.out.println("  O proximo dano recebido sera reduzido em 50%.");

        if (j1Defendeu) j1Defendendo = true;
        else j2Defendendo = true;
    }

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


    private void exibirStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println(jogador1.getNome() + " HP: " + jogador1.barraHP());
        System.out.println(jogador2.getNome() + " HP: " + jogador2.barraHP());
        System.out.println();
    }

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
