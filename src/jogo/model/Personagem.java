package jogo.model;

import java.util.List;

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
        this.hpAtual = personagemBuilder.hpAtual;
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


    public boolean estaVivo() {
        return hpAtual > 0;
    }

    public void receberDano(int dano) {
        hpAtual = hpAtual - dano;
        if (hpAtual < 0) hpAtual = 0;
    }

    public void receberCura(int cura) {
        hpAtual = hpAtual + cura;
        if (hpAtual > hpMaximo) hpAtual = hpMaximo;
    }

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

    public int calcularDefesa() {
        if (armadura == null) return 0;
        return switch (armadura) {
            case "Pesada" -> 8;
            case "Media" -> 5;
            case "Leve" -> 3;
            default -> 0;
        };
    }

    public String barraHP() {
        int total = 20;

        int cheios = (int) ((double) hpAtual / hpMaximo * total);


        String barra = "";
        for (int i = 0; i < cheios; i++) barra += "█"; // blocos cheios
        for (int i = cheios; i < total; i++) barra += "░"; // blocos vazios

        return "[" + barra + "] " + hpAtual + "/" + hpMaximo;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " | Classe: " + classe + " | Raca: " + raca + "\n"
             + "Forca: " + forca + " | Inteligencia: " + inteligencia
             + " | Destreza: " + destreza + " | Resistencia: " + resistencia + "\n"
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

        private String armaPrincipal;
        private String armadura;
        private List<String> habilidades;
        private String background;


        private int hpMaximo;
        private int hpAtual;

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
                throw new IllegalStateException("Este campo precisa ser preenchido!");
            }
            if (forca == 0 || inteligencia == 0 || destreza == 0 || resistencia == 0) {
                throw new IllegalStateException("Nenhum atributo pode ser menor que 1!");
            }
            return new Personagem(this);
        }
    }
}
