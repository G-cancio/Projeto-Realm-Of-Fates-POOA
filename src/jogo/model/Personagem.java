package jogo.model;

import java.util.List;
import jogo.annotation.Validacao;

/**
 * Representa um personagem jogável no jogo Realm of Fates.
 * Encapsula atributos como nome, classe, raça, atributos numéricos,
 * equipamentos, habilidades, pontos de vida e métodos de combate.
 * <p>
 * A construção é feita através do padrão Builder ({@link PersonagemBuilder}),
 * garantindo imutabilidade após a criação.
 */
public class Personagem {

    @Validacao(descricao = "Nome do personagem")
    private String nome;

    @Validacao(descricao = "Classe")
    private String classe;

    @Validacao(descricao = "Raça")
    private String raca;

    @Validacao(descricao = "Força")
    private int forca;

    @Validacao(descricao = "Inteligencia")
    private int inteligencia;

    @Validacao(descricao = "Destreza")
    private int destreza;

    @Validacao(descricao = "Resistencia")
    private int resistencia;

    @Validacao(descricao = "Armadura")
    private String armadura;

    private String armaPrincipal;
    private List<String> habilidades;

    @Validacao(descricao = "Background")
    private String background;

    private int hpMaximo;
    private int hpAtual;

    /**
     * Construtor privado acessado apenas pelo Builder.
     *
     * @param personagemBuilder o builder contendo todos os atributos
     */
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

    // ========== GETTERS ==========

    /** @return nome do personagem */
    public String getNome() { return nome; }
    /** @return classe do personagem (Guerreiro, Mago, etc.) */
    public String getClasse() { return classe; }
    /** @return raça do personagem (Humano, Elfo, etc.) */
    public String getRaca() { return raca; }
    /** @return valor de força (influencia dano) */
    public int getForca() { return forca; }
    /** @return valor de inteligência (influencia habilidades mágicas) */
    public int getInteligencia() { return inteligencia; }
    /** @return valor de destreza (influencia dano e chance de crítico) */
    public int getDestreza() { return destreza; }
    /** @return valor de resistência (influencia HP máximo) */
    public int getResistencia() { return resistencia; }
    /** @return nome da arma principal */
    public String getArmaPrincipal() { return armaPrincipal; }
    /** @return tipo de armadura (Pesada, Media, Leve, Nenhuma) */
    public String getArmadura() { return armadura; }
    /** @return lista de habilidades especiais */
    public List<String> getHabilidades() { return habilidades; }
    /** @return background do personagem (história) */
    public String getBackground() { return background; }
    /** @return HP máximo (vida total) */
    public int getHpMaximo() { return hpMaximo; }
    /** @return HP atual (vida restante) */
    public int getHpAtual() { return hpAtual; }

    /**
     * Verifica se o personagem ainda está vivo.
     *
     * @return {@code true} se HP atual > 0, caso contrário {@code false}
     */
    public boolean estaVivo() {
        return hpAtual > 0;
    }

    /**
     * Aplica dano ao personagem, reduzindo seu HP atual.
     * O HP nunca fica negativo (mínimo 0).
     *
     * @param dano quantidade de dano a ser recebida (valor positivo)
     */
    public void receberDano(int dano) {
        hpAtual = hpAtual - dano;
        if (hpAtual < 0) hpAtual = 0;
    }

    /**
     * Cura o personagem, aumentando seu HP atual até no máximo o HP máximo.
     *
     * @param cura quantidade de pontos de cura (valor positivo)
     */
    public void receberCura(int cura) {
        hpAtual = hpAtual + cura;
        if (hpAtual > hpMaximo) hpAtual = hpMaximo;
    }

    /**
     * Calcula o dano base do personagem em um ataque normal.
     * A fórmula considera: força + (destreza/2) + bônus da classe.
     *
     * @return valor do dano (não inclui reduções de defesa do oponente)
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
     * Calcula o valor de defesa fornecido pela armadura.
     *
     * @return redução de dano (8 para Pesada, 5 para Media, 3 para Leve, 0 caso contrário)
     */
    public int calcularDefesa() {
        if (armadura == null) return 0;
        return switch (armadura) {
            case "Pesada" -> 8;
            case "Media" -> 5;
            case "Leve" -> 3;
            default -> 0;
        };
    }

    /**
     * Gera uma representação visual da barra de HP, com blocos cheios e vazios.
     * A barra tem 20 caracteres de largura.
     *
     * @return string formatada como "[██████░░░░] 120/200"
     */
    public String barraHP() {
        int total = 20;
        int cheios = (int) ((double) hpAtual / hpMaximo * total);
        String barra = "";
        for (int i = 0; i < cheios; i++) barra += "█";
        for (int i = cheios; i < total; i++) barra += "░";
        return "[" + barra + "] " + hpAtual + "/" + hpMaximo;
    }

    /**
     * Retorna uma descrição textual completa do personagem.
     *
     * @return string com nome, classe, raça, atributos, equipamentos e habilidades
     */
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

    // ========== CLASSE BUILDER ==========

    /**
     * Builder para criação de objetos {@link Personagem}.
     * Permite configurar cada atributo de forma fluente e validar
     * os campos obrigatórios antes da construção.
     */
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

        /** Define o nome do personagem. */
        public PersonagemBuilder nome(String nome) { this.nome = nome; return this; }
        /** Define a classe. */
        public PersonagemBuilder classe(String classe) { this.classe = classe; return this; }
        /** Define a raça. */
        public PersonagemBuilder raca(String raca) { this.raca = raca; return this; }
        /** Define o valor de força. */
        public PersonagemBuilder forca(int forca) { this.forca = forca; return this; }
        /** Define a inteligência. */
        public PersonagemBuilder inteligencia(int inteligencia) { this.inteligencia = inteligencia; return this; }
        /** Define a destreza. */
        public PersonagemBuilder destreza(int destreza) { this.destreza = destreza; return this; }
        /** Define a resistência. */
        public PersonagemBuilder resistencia(int resistencia) { this.resistencia = resistencia; return this; }
        /** Define a arma principal. */
        public PersonagemBuilder armaPrincipal(String armaPrincipal) { this.armaPrincipal = armaPrincipal; return this; }
        /** Define a armadura. */
        public PersonagemBuilder armadura(String armadura) { this.armadura = armadura; return this; }
        /** Define a lista de habilidades. */
        public PersonagemBuilder habilidades(List<String> habilidades) { this.habilidades = habilidades; return this; }
        /** Define o background. */
        public PersonagemBuilder background(String background) { this.background = background; return this; }

        /**
         * Calcula o HP máximo com base na resistência (100 + resistencia * 5)
         * e define o HP atual como o máximo.
         *
         * @return o próprio builder para encadeamento
         */
        public PersonagemBuilder hp() {
            this.hpMaximo = 100 + (resistencia * 5);
            this.hpAtual = this.hpMaximo;
            return this;
        }

        /**
         * Constrói o objeto {@link Personagem} validando os campos obrigatórios.
         *
         * @return instância de Personagem
         * @throws IllegalStateException se nome, classe ou raça forem nulos,
         *         ou se algum atributo numérico for zero.
         */
        public Personagem build() {
            if (nome == null || classe == null || raca == null) {
                throw new IllegalStateException("Nome, classe e raça são obrigatórios!");
            }
            if (forca == 0 || inteligencia == 0 || destreza == 0 || resistencia == 0) {
                throw new IllegalStateException("Atributos devem ser >= 1!");
            }
            return new Personagem(this);
        }
    }
}