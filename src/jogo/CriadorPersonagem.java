package jogo; // declara que esta classe pertence ao pacote "jogo"

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

    // Scanner compartilhado por toda a classe para ler o teclado
    // "static" significa que pertence a classe, nao a uma instancia especifica
    static Scanner scanner = new Scanner(System.in);

    // PROBLEMA 2: objeto criado vazio com construtor sem argumentos.
    // Neste momento, todos os campos sao null ou 0 — o personagem esta invalido.
    static Personagem personagem = new Personagem();
    static int indexClasse;
    /**
     * Exibe menus no terminal e cria um personagem com base nas escolhas do jogador.
     * Recebe o nome do jogador (ex: "Jogador 1") apenas para exibir no cabecalho.
     * Retorna o personagem criado ao final.
     */
    public static Personagem criarPersonagem(String jogador) {
        System.out.println("\n=== CRIACAO DE PERSONAGEM — " + jogador + " ===");

        // --- NOME ---
        // fica em loop ate o jogador digitar algo que nao seja vazio ou so espacos
        String nome = "";
        while (nome.isEmpty()) {
            System.out.print("\nDigite o nome do personagem: ");
            nome = scanner.nextLine().trim(); // trim() remove espacos no inicio e no fim
            if (nome.isEmpty()) {
                System.out.println("  O nome nao pode ser vazio.");
            }
        }
        personagem.setNome(nome);

        // --- CLASSE ---
        escolherClasse();

        // --- RACA ---
        escolherRaca();

        // --- ATRIBUTOS ---
        // O jogador tem 40 pontos para distribuir entre 4 atributos.
        // Cada atributo precisa ter ao menos 1 ponto.
        // A variavel "pontos" vai diminuindo conforme os atributos sao definidos.
        escolherAtributos();

        // --- ARMA ---
        // Cada classe tem 4 armas disponiveis para escolha.
        // O array bidimensional organiza as armas por classe, na mesma ordem de "classes".
        escolherArma();

        // --- ARMADURA ---
        escolherArmadura();

        // --- HABILIDADES ESPECIAIS ---
        // Cada classe tem 3 habilidades diferentes disponiveis.
        // O jogador pode escolher ate 3, ou encerrar antes com opcao 0.
        escolherHabilidades();

        // --- BACKGROUND ---
        escolherBackground();

        // exibe o resumo do personagem criado
        System.out.println("\nPersonagem criado!");
        System.out.println(personagem); // chama automaticamente o toString() de Personagem
        System.out.println("\nPressione ENTER para continuar...");
        scanner.nextLine();

        return personagem; // retorna o personagem pronto para ser usado na batalha
    }

    public static void escolherClasse(){
        System.out.println("\nEscolha a classe:");
        System.out.println("  [1] Guerreiro — Alto dano fisico, boa resistencia");
        System.out.println("  [2] Mago      — Magia poderosa, fraco fisicamente");
        System.out.println("  [3] Arqueiro  — Precisao e velocidade");
        System.out.println("  [4] Ladino    — Furtivo, ataques criticos");
        System.out.println("  [5] Paladino  — Equilibrio entre ataque e defesa");

        // array com os nomes das classes na mesma ordem dos numeros do menu
        String[] classes = {"Guerreiro", "Mago", "Arqueiro", "Ladino", "Paladino"};

        // le a opcao do jogador (1 a 5) e subtrai 1 para virar indice do array (0 a 4)
        // ex: jogador digita 2 (Mago) -> lerOpcao retorna 2 -> 2-1=1 -> classes[1]="Mago"
        indexClasse = lerOpcao(1, 5) - 1;
        personagem.setClasse(classes[indexClasse]);
    }

    public static void escolherRaca(){
        System.out.println("\nEscolha a raca:");
        System.out.println("  [1] Humano   — Versatil, sem bonus especifico");
        System.out.println("  [2] Elfo     — +4 Destreza e Inteligencia");
        System.out.println("  [3] Anao     — +6 Resistencia, +4 Forca");
        System.out.println("  [4] Orc      — +8 Forca");
        System.out.println("  [5] Halfling — +6 Destreza");

        String[] racas = {"Humano", "Elfo", "Anao", "Orc", "Halfling"};
        personagem.setRaca(racas[lerOpcao(1, 5) - 1]); // mesma logica da classe
    }

    public static void escolherAtributos(){
        System.out.println("\nDistribua 40 pontos entre os 4 atributos (minimo 1 cada):");
        int pontos = 40;

        // pontos - 3: reserva 1 ponto minimo para cada um dos 3 atributos restantes
        int forca        = lerAtributo("Forca",        pontos - 3); pontos -= forca;
        // pontos - 2: reserva 1 ponto minimo para cada um dos 2 atributos restantes
        int inteligencia = lerAtributo("Inteligencia", pontos - 2); pontos -= inteligencia;
        // pontos - 1: reserva 1 ponto minimo para o ultimo atributo
        int destreza     = lerAtributo("Destreza",     pontos - 1); pontos -= destreza;
        // o ultimo atributo recebe automaticamente os pontos que sobraram
        int resistencia  = pontos;
        System.out.println("  Resistencia: " + resistencia + " (pontos restantes)");

        // define os atributos no personagem via setters
        personagem.setForca(forca);
        personagem.setInteligencia(inteligencia);
        personagem.setDestreza(destreza);
        personagem.setResistencia(resistencia);

    }

    public static void escolherArmadura(){
        System.out.println("\nEscolha a armadura:");
        System.out.println("  [1] Pesada  — Reduz 8 de dano");
        System.out.println("  [2] Media   — Reduz 5 de dano");
        System.out.println("  [3] Leve    — Reduz 3 de dano");
        System.out.println("  [4] Nenhuma — Sem reducao");

        // null na posicao 3 representa "sem armadura"
        String[] armaduras = {"Pesada", "Media", "Leve", null};
        personagem.setArmadura(armaduras[lerOpcao(1, 4) - 1]);
    }

    public static void escolherArma(){
        String[][] armasPorClasse = {
                {"Espada Longa", "Machado", "Lanca", "Martelo"},           // Guerreiro
                {"Cajado Arcano", "Varinha", "Tomo Sombrio", "Orbe"},      // Mago
                {"Arco Longo", "Arco Curto", "Besta", "Arco de Caca"},     // Arqueiro
                {"Adaga Envenenada", "Punhal", "Garra Dupla", "Estoque"},  // Ladino
                {"Espada Sagrada", "Maca Benta", "Lanca da Luz", "Escudo"} // Paladino
        };

        // pega as armas disponiveis para a classe escolhida pelo jogador
        String[] armasDisponiveis = armasPorClasse[indexClasse];

        System.out.println("\nEscolha a arma:");
        for (int i = 0; i < armasDisponiveis.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + armasDisponiveis[i]);
        }

        // le a escolha e define a arma no personagem
        personagem.setArmaPrincipal(armasDisponiveis[lerOpcao(1, armasDisponiveis.length) - 1]);
    }

    public static void escolherHabilidades(){
        String[] habilidadesGuerreiro = {"Golpe Brutal", "Grito de Guerra", "Furia Berserker"};
        String[] habilidadesMago      = {"Bola de Fogo", "Raio", "Drenar Vida"};
        String[] habilidadesArqueiro  = {"Tiro Preciso", "Chuva de Flechas", "Olho de Aguia"};
        String[] habilidadesLadino    = {"Ataque Furtivo", "Veneno", "Golpe Duplo"};
        String[] habilidadesPaladino  = {"Golpe Sagrado", "Cura Divina", "Aura Protetora"};

        // array bidimensional: cada linha e o conjunto de habilidades de uma classe
        // a linha 0 e do Guerreiro, linha 1 do Mago, etc. — mesma ordem de "classes"
        String[][] todasHabilidades = {
                habilidadesGuerreiro, habilidadesMago, habilidadesArqueiro,
                habilidadesLadino, habilidadesPaladino
        };

        // pega as habilidades da classe escolhida pelo jogador
        String[] habilidadesDisponiveis = todasHabilidades[indexClasse];

        System.out.println("\nEscolha ate 3 habilidades (0 para encerrar):");
        for (int i = 0; i < habilidadesDisponiveis.length; i++) {
            System.out.println("  [" + (i + 1) + "] " + habilidadesDisponiveis[i]);
        }
        System.out.println("  [0] Encerrar");

        // lista dinamica para guardar as habilidades escolhidas
        List<String> escolhidas = new ArrayList<>();

        // loop continua ate o jogador escolher 3 habilidades ou digitar 0
        while (escolhidas.size() < 3) {
            int opcao = lerOpcao(0, habilidadesDisponiveis.length);

            if (opcao == 0) break; // jogador encerrou a selecao

            String habilidade = habilidadesDisponiveis[opcao - 1]; // pega o nome da habilidade escolhida

            // verifica se a habilidade ja foi escolhida antes
            if (!escolhidas.contains(habilidade)) {
                escolhidas.add(habilidade); // adiciona a lista
                System.out.println("  Adicionada: " + habilidade + " (" + escolhidas.size() + "/3)");
            } else {
                System.out.println("  Habilidade ja escolhida.");
            }
        }

        personagem.setHabilidades(escolhidas); // define a lista de habilidades no personagem
    }

    public static void escolherBackground(){
        System.out.println("\nEscolha o background:");
        System.out.println("  [1] Nobre");
        System.out.println("  [2] Orfao");
        System.out.println("  [3] Mercenario");
        System.out.println("  [4] Eremita");

        String[] backgrounds = {"Nobre", "Orfao", "Mercenario", "Eremita"};
        personagem.setBackground(backgrounds[lerOpcao(1, 4) - 1]);

        // PROBLEMA: HP precisa ser inicializado manualmente apos todos os setters.
        // Se este metodo nao for chamado, hpMaximo = 0 e o personagem nao consegue lutar.
        // O Builder resolveria isso garantindo que o objeto so existiria completo.
        personagem.inicializarHP();
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
                int valor = scanner.nextInt(); //Integer.parseInt(scanner.nextLine()); // converte texto para numero
                if (valor >= 1 && valor <= maximo) return valor;     // valor valido: retorna
                System.out.println("  Valor invalido.");        // fora do intervalo
            } catch (NumberFormatException e) {
                // NumberFormatException ocorre quando o texto nao pode ser convertido para int
                // ex: jogador digitou "abc" em vez de um numero
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
                int opcao = scanner.nextInt();//Integer.parseInt(scanner.nextLine()); // converte texto para numero
                if (opcao >= minimo && opcao <= maximo) return opcao;         // opcao valida: retorna
                System.out.println("  Opcao invalida.");
            } catch (NumberFormatException e) {
                // mesmo tratamento: jogador digitou algo que nao e numero
                System.out.println("  Digite um numero.");
            }
        }
    }
}
