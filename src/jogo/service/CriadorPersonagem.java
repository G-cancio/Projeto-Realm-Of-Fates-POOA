package jogo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import jogo.model.Personagem;
import jogo.util.Leitura;
import jogo.util.Validador;

/**
 * Responsável pela criação interativa de um personagem.
 * Gerencia menus para escolha de classe, raça, distribuição de pontos,
 * equipamentos, habilidades e background.
 * Utiliza o padrão Builder para construir o {@link Personagem} e
 * valida os campos obrigatórios via reflexão com {@link Validador}.
 */
public class CriadorPersonagem {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Método principal para criação de um personagem.
     * Repete todo o processo enquanto houver campos obrigatórios nulos,
     * garantindo que apenas personagens válidos sejam retornados.
     *
     * @param jogador identificador do jogador (ex: "Jogador 1")
     * @return um personagem completamente preenchido e validado
     */
    public static Personagem criarPersonagem(String jogador) {
        Personagem personagem = null;
        boolean valido = false;

        while (!valido) {
            System.out.println("\n=== CRIACAO DE PERSONAGEM — " + jogador + " ===");

            String nome = "";
            while (nome.isEmpty()) {
                System.out.print("\nDigite o nome do personagem: ");
                nome = scanner.nextLine().trim();
                if (nome.isEmpty()) System.out.println("  O nome nao pode ser vazio.");
            }

            int indexClasse = escolherClasse();
            String classe = obterNomeClasse(indexClasse);
            String raca = escolherRaca();
            List<Integer> atributos = escolherAtributos();

            int forca = atributos.get(0);
            int inteligencia = atributos.get(1);
            int destreza = atributos.get(2);
            int resistencia = atributos.get(3);

            // Aplica bônus raciais
            switch (raca) {
                case "Elfo" -> {
                    destreza += 4;
                    inteligencia += 4;
                }
                case "Anao" -> {
                    resistencia += 6;
                    forca += 4;
                }
                case "Orc" -> forca += 8;
                case "Halfling" -> destreza += 6;
            }

            String arma = escolherArma(indexClasse);
            String armadura = escolherArmadura();
            List<String> habilidades = escolherHabilidades(indexClasse);
            String background = escolherBackground();

            Personagem candidato = new Personagem.PersonagemBuilder()
                    .nome(nome)
                    .classe(classe)
                    .raca(raca)
                    .forca(forca)
                    .inteligencia(inteligencia)
                    .destreza(destreza)
                    .resistencia(resistencia)
                    .armaPrincipal(arma)
                    .armadura(armadura)
                    .habilidades(habilidades)
                    .background(background)
                    .hp()
                    .build();

            List<String> erros = Validador.validar(candidato);
            if (erros.isEmpty()) {
                personagem = candidato;
                valido = true;
                System.out.println("\n✅ Personagem validado com sucesso!");
            } else {
                System.out.println("\n❌ ERRO: Campos obrigatórios faltando:");
                for (String erro : erros) System.out.println("   - " + erro);
                System.out.println("\nRecriando personagem. Pressione ENTER para recomeçar...");
                scanner.nextLine();
            }
        }

        System.out.println("\nPersonagem criado!\n" + personagem);
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
        return personagem;
    }

    /**
     * Exibe menu de escolha de classe e retorna o índice (0 a 4).
     *
     * @return índice da classe escolhida
     */
    public static int escolherClasse() {
        System.out.println("\nEscolha a classe:");
        System.out.println("  [1] Guerreiro — Alto dano fisico, boa resistencia");
        System.out.println("  [2] Mago      — Magia poderosa, fraco fisicamente");
        System.out.println("  [3] Arqueiro  — Precisao e velocidade");
        System.out.println("  [4] Ladino    — Furtivo, ataques criticos");
        System.out.println("  [5] Paladino  — Equilibrio entre ataque e defesa");
        return Leitura.lerOpcao(1, 5) - 1;
    }

    private static String obterNomeClasse(int index) {
        String[] classes = {"Guerreiro", "Mago", "Arqueiro", "Ladino", "Paladino"};
        return classes[index];
    }

    /**
     * Exibe menu de escolha de raça e retorna o nome da raça.
     *
     * @return nome da raça (Humano, Elfo, Anao, Orc, Halfling)
     */
    public static String escolherRaca() {
        System.out.println("\nEscolha a raca:");
        System.out.println("  [1] Humano   — Versatil, sem bonus especifico");
        System.out.println("  [2] Elfo     — +4 Destreza e Inteligencia");
        System.out.println("  [3] Anao     — +6 Resistencia, +4 Forca");
        System.out.println("  [4] Orc      — +8 Forca");
        System.out.println("  [5] Halfling — +6 Destreza");
        String[] racas = {"Humano", "Elfo", "Anao", "Orc", "Halfling"};
        return racas[Leitura.lerOpcao(1, 5) - 1];
    }

    /**
     * Permite distribuir 40 pontos entre os quatro atributos (força, inteligência, destreza, resistência).
     * Cada atributo deve ter no mínimo 1 ponto.
     *
     * @return lista com os valores finais na ordem: [força, inteligência, destreza, resistência]
     */
    public static List<Integer> escolherAtributos() {
        System.out.println("\nDistribua 40 pontos entre os 4 atributos (minimo 1 cada):");
        int pontos = 40;
        int forca = Leitura.lerAtributo("Forca", pontos - 3);
        pontos -= forca;
        int inteligencia = Leitura.lerAtributo("Inteligencia", pontos - 2);
        pontos -= inteligencia;
        int destreza = Leitura.lerAtributo("Destreza", pontos - 1);
        pontos -= destreza;
        int resistencia = pontos;
        System.out.println("  Resistencia: " + resistencia + " (pontos restantes)");
        return List.of(forca, inteligencia, destreza, resistencia);
    }

    /**
     * Menu para escolha de armadura.
     *
     * @return tipo de armadura: "Pesada", "Media", "Leve" ou "Nenhuma"
     */
    public static String escolherArmadura() {
        System.out.println("\nEscolha a armadura:");
        System.out.println("  [1] Pesada  — Reduz 8 de dano");
        System.out.println("  [2] Media   — Reduz 5 de dano");
        System.out.println("  [3] Leve    — Reduz 3 de dano");
        System.out.println("  [4] Nenhuma — Sem reducao");
        String[] armaduras = {"Pesada", "Media", "Leve", "Nenhuma"};
        return armaduras[Leitura.lerOpcao(1, 4) - 1];
    }

    /**
     * Menu de escolha de arma baseado na classe do personagem.
     *
     * @param indexClasse índice da classe (0 a 4)
     * @return nome da arma escolhida
     */
    public static String escolherArma(int indexClasse) {
        String[][] armasPorClasse = {
                {"Espada Longa", "Machado", "Lanca", "Martelo"},
                {"Cajado Arcano", "Varinha", "Tomo Sombrio", "Orbe"},
                {"Arco Longo", "Arco Curto", "Besta", "Arco de Caca"},
                {"Adaga Envenenada", "Punhal", "Garra Dupla", "Estoque"},
                {"Espada Sagrada", "Maca Benta", "Lanca da Luz", "Escudo"}
        };
        String[] armasDisponiveis = armasPorClasse[indexClasse];
        System.out.println("\nEscolha a arma:");
        for (int i = 0; i < armasDisponiveis.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + armasDisponiveis[i]);
        }
        return armasDisponiveis[Leitura.lerOpcao(1, armasDisponiveis.length) - 1];
    }

    /**
     * Menu para escolha de até 3 habilidades específicas da classe.
     *
     * @param indexClasse índice da classe
     * @return lista (possivelmente vazia) com as habilidades escolhidas
     */
    public static List<String> escolherHabilidades(int indexClasse) {
        String[] habilidadesGuerreiro = {"Golpe Brutal", "Grito de Guerra", "Furia Berserker"};
        String[] habilidadesMago = {"Bola de Fogo", "Raio", "Drenar Vida"};
        String[] habilidadesArqueiro = {"Tiro Preciso", "Chuva de Flechas", "Olho de Aguia"};
        String[] habilidadesLadino = {"Ataque Furtivo", "Veneno", "Golpe Duplo"};
        String[] habilidadesPaladino = {"Golpe Sagrado", "Cura Divina", "Aura Protetora"};
        String[][] todasHabilidades = {
                habilidadesGuerreiro, habilidadesMago, habilidadesArqueiro,
                habilidadesLadino, habilidadesPaladino
        };
        String[] habilidadesDisponiveis = todasHabilidades[indexClasse];
        System.out.println("\nEscolha ate 3 habilidades (0 para encerrar):");
        for (int i = 0; i < habilidadesDisponiveis.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + habilidadesDisponiveis[i]);
        }
        System.out.println("  [0] Encerrar");

        List<String> escolhidas = new ArrayList<>();
        while (escolhidas.size() < 3) {
            int opcao = Leitura.lerOpcao(0, habilidadesDisponiveis.length);
            if (opcao == 0) break;
            String habilidade = habilidadesDisponiveis[opcao - 1];
            if (!escolhidas.contains(habilidade)) {
                escolhidas.add(habilidade);
                System.out.println("  Adicionada: " + habilidade + " (" + escolhidas.size() + "/3)");
            } else {
                System.out.println("  Habilidade ja escolhida.");
            }
        }
        return escolhidas;
    }

    /**
     * Menu para escolha do background (história de origem).
     *
     * @return background escolhido: "Nobre", "Orfao", "Mercenario" ou "Eremita"
     */
    public static String escolherBackground() {
        System.out.println("\nEscolha o background:");
        System.out.println("  [1] Nobre");
        System.out.println("  [2] Orfao");
        System.out.println("  [3] Mercenario");
        System.out.println("  [4] Eremita");
        String[] backgrounds = {"Nobre", "Orfao", "Mercenario", "Eremita"};
        return backgrounds[Leitura.lerOpcao(1, 4) - 1];
    }
}