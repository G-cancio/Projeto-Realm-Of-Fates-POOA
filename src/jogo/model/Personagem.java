package jogo.model; // declara que esta classe pertence ao pacote "jogo"

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

    private String nome;
    private String classe;
    private String raca;
    private int forca;
    private int inteligencia;
    private int destreza;
    private int resistencia;
    private String armaPrincipal;
    private String armadura;
    private List<String> habilidades;
    private String background;
    private int hpMaximo;
    private int hpAtual;

    private Personagem(PersonagemBuilder personagemBuilder) {
        this.nome = personagemBuilder.nome;
        this.classe = personagemBuilder.classe;
        this.raca = personagemBuilder.raca;
        this.forca = personagemBuilder.forca;
        this.inteligencia = personagemBuilder.inteligencia;
        this.destreza = personagemBuilder.destreza;
        this.resistencia = personagemBuilder.resistencia;
        this.armaPrincipal = personagemBuilder.armaPrincipal;
        this.armadura = personagemBuilder.armadura;
        this.habilidades = personagemBuilder.habilidades;
        this.background = personagemBuilder.background;
        this.hpMaximo = personagemBuilder.hpMaximo;
    }

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
        int bonus = 0;

        if (classe != null) {
            switch (classe) {
                case "Guerreiro" -> bonus = 5;
                case "Mago" -> bonus = 8;
                case "Arqueiro" -> bonus = 4;
                case "Ladino" -> bonus = 6;
                case "Paladino" -> bonus = 3;
            }
        }
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
             + "Forca: " + forca + " | Inteligencia: " + inteligencia
             + " | Destreza: " + destreza + " | Resistencia: " + resistencia + "\n"
             // se armaPrincipal for null, exibe "Nenhuma"; senao exibe o valor
             + "Arma: "      + (armaPrincipal != null ? armaPrincipal : "Nenhuma")
             + " | Armadura: " + (armadura    != null ? armadura      : "Nenhuma")
             + " | Background: " + (background != null ? background   : "Nenhum") + "\n"
             + "Habilidades: " + (habilidades != null ? habilidades   : "Nenhuma");
    }

    public static class PersonagemBuilder {

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

        public PersonagemBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public PersonagemBuilder classe(String classe) {
            this.classe = classe;
            return this;
        }
        public PersonagemBuilder raca(String raca) {
            this.raca = raca;
            return this;
        }
        public PersonagemBuilder forca(int forca) {
            this.forca = forca;
            return this;
        }
        public PersonagemBuilder inteligencia(int inteligencia) {
            this.inteligencia = inteligencia;
            return this;
        }
        public PersonagemBuilder destreza(int destreza) {
            this.destreza = destreza;
            return this;
        }
        public PersonagemBuilder resistencia(int resistencia) {
            this.resistencia = resistencia;
            return this;
        }

        // Atributos opcionais — o personagem pode existir sem esses valores
        public PersonagemBuilder armaPrincipal(String armaPrincipal) {
            this.armaPrincipal = armaPrincipal;
            return this;
        }

        public PersonagemBuilder armadura(String armadura) {
            this.armadura = armadura;
            return this;
        }

        public PersonagemBuilder habilidades(List<String> habilidades) {
            this.habilidades = habilidades;
            return this;
        }

        public PersonagemBuilder background(String background) {
            this.background = background;
            return this;
        }

        public PersonagemBuilder hp() {
            this.hpMaximo = 100 + (resistencia * 5);
            this.hpAtual = this.hpMaximo;
            return this;
        }

        public Personagem build() {
            if (nome == null || classe == null || raca == null) {
                throw new IllegalMonitorStateException("Este campo precisa ser preenchido!");
            }
            if (forca == 0 || inteligencia == 0 || destreza == 0 || resistencia == 0) {
                throw new IllegalMonitorStateException("Nenhum atributo pode ser menor que 1!");
            }
            return new Personagem(this);
        }
    }
}
