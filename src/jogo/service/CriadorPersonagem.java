package jogo.service;

import jogo.model.Personagem;

import java.util.ArrayList; // importa ArrayList, uma implementacao de lista dinamica
import java.util.List;      // importa a interface List
import java.util.Scanner;   // importa Scanner para ler entradas do teclado

/**
 * Responsavel por criar personagens de forma interativa no terminal.
 *
 * PROBLEMA demonstrado aqui:
 * O personagem e criado com o construtor vazio (new Personagem()) e
 * seus campos sao preenchidos um a um via setters. Isso significa que
 * o objeto existe na memoria antes de estar pronto — qualquer acesso
 * antes do ultimo setter resulta em dados invalidos (null ou zero).
 */
public class CriadorPersonagem {

    static Scanner scanner = new Scanner(System.in);
    static Personagem personagem;
    static int indexClasse;
    /**
     * Exibe menus no terminal e cria um personagem com base nas escolhas do jogador.
     * Recebe o nome do jogador (ex: "Jogador 1") apenas para exibir no cabecalho.
     * Retorna o personagem criado ao final.
     */
    public static Personagem criarPersonagem(String jogador) {
        System.out.println("\n=== CRIACAO DE PERSONAGEM — " + jogador + " ===");

        String nome = "";
        while (nome.isEmpty()) {
            System.out.print("\nDigite o nome do personagem: ");
            nome = scanner.nextLine().trim(); // trim() remove espacos no inicio e no fim
            if (nome.isEmpty()) {
                System.out.println("  O nome nao pode ser vazio.");
            }
        }

        List<Integer> atributos = escolherAtributos();

        personagem = new Personagem.PersonagemBuilder()
                .nome(nome)
                .classe(escolherClasse())
                .raca(escolherRaca())
                .forca(atributos.get(0))
                .inteligencia(atributos.get(1))
                .destreza(atributos.get(2))
                .resistencia(atributos.get(3))
                .armaPrincipal(escolherArma())
                .armadura(escolherArmadura())
                .habilidades(escolherHabilidades())
                .background(escolherBackground())
                .hp()
                .build();

        System.out.println("\nPersonagem criado!\n" + personagem + "\nPressione ENTER para continuar...");
        scanner.nextLine();

        return personagem;
    }

    public static String escolherClasse(){
        System.out.println("\nEscolha a classe:");
        System.out.println("  [1] Guerreiro — Alto dano fisico, boa resistencia");
        System.out.println("  [2] Mago      — Magia poderosa, fraco fisicamente");
        System.out.println("  [3] Arqueiro  — Precisao e velocidade");
        System.out.println("  [4] Ladino    — Furtivo, ataques criticos");
        System.out.println("  [5] Paladino  — Equilibrio entre ataque e defesa");

        String[] classes = {"Guerreiro", "Mago", "Arqueiro", "Ladino", "Paladino"};

        indexClasse = lerOpcao(1, 5) - 1;
        return classes[indexClasse];
    }

    public static String  escolherRaca(){
        System.out.println("\nEscolha a raca:");
        System.out.println("  [1] Humano   — Versatil, sem bonus especifico");
        System.out.println("  [2] Elfo     — +4 Destreza e Inteligencia");
        System.out.println("  [3] Anao     — +6 Resistencia, +4 Forca");
        System.out.println("  [4] Orc      — +8 Forca");
        System.out.println("  [5] Halfling — +6 Destreza");

        String[] racas = {"Humano", "Elfo", "Anao", "Orc", "Halfling"};
        return racas[lerOpcao(1, 5) - 1];
    }

    public static List<Integer> escolherAtributos(){
        System.out.println("\nDistribua 40 pontos entre os 4 atributos (minimo 1 cada):");
        int pontos = 40;

        int forca        = lerAtributo("Forca",        pontos - 3); pontos -= forca;
        int inteligencia = lerAtributo("Inteligencia", pontos - 2); pontos -= inteligencia;
        int destreza     = lerAtributo("Destreza",     pontos - 1); pontos -= destreza;
        int resistencia  = pontos;

        System.out.println("  Resistencia: " + resistencia + " (pontos restantes)");

        return List.of(forca, inteligencia, destreza, resistencia);
    }

    public static String escolherArmadura(){
        System.out.println("\nEscolha a armadura:");
        System.out.println("  [1] Pesada  — Reduz 8 de dano");
        System.out.println("  [2] Media   — Reduz 5 de dano");
        System.out.println("  [3] Leve    — Reduz 3 de dano");
        System.out.println("  [4] Nenhuma — Sem reducao");

        String[] armaduras = {"Pesada", "Media", "Leve", null};
        return armaduras[lerOpcao(1, 4) - 1];
    }

    public static String escolherArma(){
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

        return armasDisponiveis[lerOpcao(1, armasDisponiveis.length) - 1];
    }

    public static List<String> escolherHabilidades(){
        String[] habilidadesGuerreiro = {"Golpe Brutal", "Grito de Guerra", "Furia Berserker"};
        String[] habilidadesMago      = {"Bola de Fogo", "Raio", "Drenar Vida"};
        String[] habilidadesArqueiro  = {"Tiro Preciso", "Chuva de Flechas", "Olho de Aguia"};
        String[] habilidadesLadino    = {"Ataque Furtivo", "Veneno", "Golpe Duplo"};
        String[] habilidadesPaladino  = {"Golpe Sagrado", "Cura Divina", "Aura Protetora"};

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
            int opcao = lerOpcao(0, habilidadesDisponiveis.length);

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

    public static String escolherBackground(){
        System.out.println("\nEscolha o background:");
        System.out.println("  [1] Nobre");
        System.out.println("  [2] Orfao");
        System.out.println("  [3] Mercenario");
        System.out.println("  [4] Eremita");

        String[] backgrounds = {"Nobre", "Orfao", "Mercenario", "Eremita"};
        return backgrounds[lerOpcao(1, 4) - 1];
    }

    /**
     * Pede ao jogador que digite um valor para um atributo.
     * O valor precisa estar entre 1 e o maximo informado.
     * Fica em loop ate o jogador digitar um valor valido.
     */
    private static int lerAtributo(String nome, int maximo) {
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
}
