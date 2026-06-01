package jogo; // declara que esta classe pertence ao pacote "jogo"

import java.util.List; // importa a interface List para usar listas

/**
 * Representa um personagem do jogo Realm of Fates.
 *
 * Esta classe demonstra dois problemas que o padrao Builder resolve:
 *
 * PROBLEMA 1 — Construtor telescopico:
 *   Tem 11 parametros, o que torna a criacao confusa e dificil de manter.
 *   Atributos opcionais (arma, armadura, etc.) obrigam o uso de null.
 *
 * PROBLEMA 2 — Construtor vazio + setters livres:
 *   O objeto e criado sem nenhum dado. Ele existe na memoria mas esta
 *   incompleto. Qualquer codigo que usar esse objeto antes de todos os
 *   setters serem chamados vai encontrar valores nulos ou zeros.
 */
public class Personagem {

    // Atributos obrigatorios — todo personagem precisa ter esses valores
    private String nome;
    private String classe;
    private String raca;
    private int forca;
    private int inteligencia;
    private int destreza;
    private int resistencia;

    // Atributos opcionais — o personagem pode existir sem esses valores
    private String armaPrincipal;
    private String armadura;
    private List<String> habilidades; // lista de ate 3 habilidades especiais
    private String background;

    // HP (pontos de vida) calculado a partir da resistencia
    private int hpMaximo; // valor maximo de HP do personagem
    private int hpAtual;  // HP atual durante a batalha

    // ---------------------------------------------------------------
    // PROBLEMA 1: Construtor telescopico com 11 parametros.
    // Quem for criar um personagem precisa passar todos os valores na
    // ordem certa, incluindo null para os campos opcionais.
    // Exemplo do problema: new Personagem("Gandalf","Mago","Humano",
    //   8,20,10,12,"Cajado",null,null,null) — os nulls nao tem significado
    // ---------------------------------------------------------------
    public Personagem(String nome, String classe, String raca,
                      int forca, int inteligencia, int destreza, int resistencia,
                      String armaPrincipal, String armadura,
                      List<String> habilidades, String background) {
        // atribui cada parametro ao campo correspondente da classe
        this.nome = nome;
        this.classe = classe;
        this.raca = raca;
        this.forca = forca;
        this.inteligencia = inteligencia;
        this.destreza = destreza;
        this.resistencia = resistencia;
        this.armaPrincipal = armaPrincipal;
        this.armadura = armadura;
        this.habilidades = habilidades;
        this.background = background;

        // calcula o HP maximo: base de 100 + 5 pontos por cada ponto de resistencia
        this.hpMaximo = 100 + (resistencia * 5);
        this.hpAtual = this.hpMaximo; // comeca com HP cheio
    }

    // ---------------------------------------------------------------
    // PROBLEMA 2: Construtor vazio — o objeto nasce sem nenhum dado.
    // Todos os campos ficam null (para String) ou 0 (para int).
    // O personagem existe na memoria mas esta completamente invalido.
    // ---------------------------------------------------------------
    public Personagem() {
        // corpo vazio intencional — demonstra o problema
    }

    /**
     * Inicializa o HP do personagem com base na resistencia.
     *
     * PROBLEMA: este metodo precisa ser chamado manualmente apos os setters.
     * Se o programador esquecer de chamar, hpMaximo fica 0 e o personagem
     * nao consegue lutar. Nao ha nenhum aviso ou erro automatico.
     */
    public void inicializarHP() {
        this.hpMaximo = 100 + (resistencia * 5); // mesmo calculo do construtor cheio
        this.hpAtual = this.hpMaximo;
    }

    // ---------------------------------------------------------------
    // SETTERS — metodos para definir o valor de cada campo.
    // Usados no PROBLEMA 2, junto com o construtor vazio.
    // ---------------------------------------------------------------
    public void setNome(String nome)                     { this.nome = nome; }
    public void setClasse(String classe)                 { this.classe = classe; }
    public void setRaca(String raca)                     { this.raca = raca; }
    public void setForca(int forca)                      { this.forca = forca; }
    public void setInteligencia(int inteligencia)        { this.inteligencia = inteligencia; }
    public void setDestreza(int destreza)                { this.destreza = destreza; }
    public void setResistencia(int resistencia)          { this.resistencia = resistencia; }
    public void setArmaPrincipal(String armaPrincipal)   { this.armaPrincipal = armaPrincipal; }
    public void setArmadura(String armadura)             { this.armadura = armadura; }
    public void setHabilidades(List<String> habilidades) { this.habilidades = habilidades; }
    public void setBackground(String background)         { this.background = background; }

    // ---------------------------------------------------------------
    // GETTERS — metodos para ler o valor de cada campo.
    // Usados pela Batalha para acessar os dados do personagem.
    // ---------------------------------------------------------------
    public String getNome()              { return nome; }
    public String getClasse()            { return classe; }
    public String getRaca()              { return raca; }
    public int getForca()                { return forca; }
    public int getInteligencia()         { return inteligencia; }
    public int getDestreza()             { return destreza; }
    public int getResistencia()          { return resistencia; }
    public String getArmaPrincipal()     { return armaPrincipal; }
    public String getArmadura()          { return armadura; }
    public List<String> getHabilidades() { return habilidades; }
    public String getBackground()        { return background; }
    public int getHpMaximo()             { return hpMaximo; }
    public int getHpAtual()              { return hpAtual; }

    /**
     * Retorna true se o personagem ainda tem HP acima de zero.
     * Usado pela Batalha para saber se a luta continua.
     */
    public boolean estaVivo() {
        return hpAtual > 0;
    }

    /**
     * Reduz o HP atual pelo valor do dano recebido.
     * Se o HP cair abaixo de 0, e fixado em 0 (nao pode ser negativo).
     */
    public void receberDano(int dano) {
        hpAtual = hpAtual - dano;
        if (hpAtual < 0) hpAtual = 0; // garante que HP nao fique negativo
    }

    /**
     * Aumenta o HP atual pelo valor da cura.
     * Se o HP ultrapassar o maximo, e fixado no maximo.
     */
    public void receberCura(int cura) {
        hpAtual = hpAtual + cura;
        if (hpAtual > hpMaximo) hpAtual = hpMaximo; // nao pode passar do maximo
    }

    /**
     * Calcula o dano que este personagem causa por ataque.
     * Formula: forca + metade da destreza + bonus da classe.
     * Cada classe tem um bonus diferente de dano.
     */
    public int calcularDano() {
        int bonus = 0; // bonus adicional baseado na classe

        // verifica qual e a classe e define o bonus correspondente

        if (classe != null) {
            switch (classe) {
                case "Guerreiro" -> bonus = 5;
                case "Mago" -> bonus = 8;
                case "Arqueiro" -> bonus = 4;
                case "Ladino" -> bonus = 6;
                case "Paladino" -> bonus = 3;
            }
        }

        // dano = forca + metade da destreza + bonus da classe
        return forca + (destreza / 2) + bonus;
    }

    /**
     * Retorna quanto de dano este personagem absorve por ataque.
     * O valor depende do tipo de armadura equipada.
     * Se nao tiver armadura, a defesa e zero.
     */
    public int calcularDefesa() {
        return switch (armadura) {
            case "Pesada" -> 8;
            case "Media" -> 5;
            case "Leve" -> 3;
            default -> 0;
        };
    }

    /**
     * Gera uma barra visual de HP para exibir no terminal.
     * Exemplo: [████████████░░░░░░░░] 60/100
     * Os blocos cheios (█) representam o HP atual.
     * Os blocos vazios (░) representam o HP perdido.
     */
    public String barraHP() {
        // protecao: se HP nao foi inicializado, avisa em vez de dividir por zero
        if (hpMaximo == 0) return "[HP nao inicializado]";

        int total = 20; // tamanho total da barra em caracteres

        // calcula quantos blocos cheios devem aparecer
        // ex: se hpAtual=60 e hpMaximo=100, cheios = (60/100) * 20 = 12
        int cheios = (int) ((double) hpAtual / hpMaximo * total);

        // monta a barra caractere por caractere
        String barra = "";
        for (int i = 0; i < cheios; i++) barra += "█"; // blocos cheios
        for (int i = cheios; i < total; i++) barra += "░"; // blocos vazios

        return "[" + barra + "] " + hpAtual + "/" + hpMaximo;
    }

    /**
     * Retorna uma representacao em texto do personagem.
     * Chamado automaticamente quando se faz System.out.println(personagem).
     * Usa o operador ternario (? :) para exibir "Nenhuma/Nenhum"
     * quando um campo opcional nao foi preenchido.
     */
    @Override
    public String toString() {
        return "Nome: " + nome + " | Classe: " + classe + " | Raca: " + raca + "\n"
             + "Forca: " + forca + " | Intel: " + inteligencia
             + " | Destreza: " + destreza + " | Resistencia: " + resistencia + "\n"
             // se armaPrincipal for null, exibe "Nenhuma"; senao exibe o valor
             + "Arma: "      + (armaPrincipal != null ? armaPrincipal : "Nenhuma")
             + " | Armadura: " + (armadura    != null ? armadura      : "Nenhuma")
             + " | Background: " + (background != null ? background   : "Nenhum") + "\n"
             + "Habilidades: " + (habilidades != null ? habilidades   : "Nenhuma");
    }
}
