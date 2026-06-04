# Realm of Fates

## Descrição

Realm of Fates é um jogo de batalha em turnos desenvolvido em Java. O sistema permite que dois jogadores criem seus personagens, personalizando nome, classe, raça, atributos e habilidades. Após a criação, os personagens participam de uma batalha até que apenas um permaneça vivo.

O projeto foi desenvolvido utilizando conceitos de Programação Orientada a Objetos, padrão de projeto Builder, anotações personalizadas e reflexão (Reflection API) para validação automática dos personagens.

---

## Funcionalidades

- Criação personalizada de personagens
  - Escolha de classe e raça
  - Distribuição de atributos
  - Seleção de habilidades
- Sistema de combate em turnos
  - Ataque normal
  - Ataque pesado
  - Defesa
  - Uso de habilidades especiais
- Sistema de HP e cálculo de dano
- Validação automática de campos obrigatórios utilizando anotações e reflexão

---

## Tecnologias Utilizadas

- Java 17+ (ou versão compatível)
- Programação Orientada a Objetos (POO)
- Padrão Builder
- Reflection API
- Anotações Customizadas

---

## Estrutura do Projeto

```
src/
└── jogo/
    ├── annotation/
    │   └── Validacao.java
    │
    ├── model/
    │   └── Personagem.java
    │
    ├── service/
    │   ├── Batalha.java
    │   └── CriadorPersonagem.java
    │
    ├── util/
    │   ├── Leitura.java
    │   └── Validador.java
    │
    └── Main.java
```

---

## Como Executar

### Pré-requisitos

- Java JDK instalado (versão 17 ou superior recomendada)
- IDE Java (IntelliJ IDEA, Eclipse ou VS Code) ou terminal

### Executando pela IDE

1. Abra o projeto na IDE.
2. Localize a classe `Main`.
3. Execute o método `main()`.

### Executando pelo Terminal

1. Abra o terminal na pasta raiz do projeto.

2. Navegue até a pasta de código-fonte e compile os arquivos:

```bash
cd src
javac jogo/**/*.java
```
*(Nota: no Windows, dependendo do terminal, você pode precisar compilar listando os arquivos ou usando `javac jogo/*.java jogo/*/*.java` caso o `**` não seja suportado)*

3. Execute a aplicação:

```bash
java jogo.Main
```

---

## Fluxo de Utilização

1. Inicie o programa.
2. Escolha a opção "Nova Batalha".
3. O Jogador 1 cria seu personagem.
4. O Jogador 2 cria seu personagem.
5. Os personagens são validados automaticamente.
6. A batalha é iniciada.
7. Cada jogador escolhe ações durante seu turno:
   - Atacar
   - Ataque Pesado
   - Defender
   - Usar Habilidade
8. O combate continua até que um dos personagens seja derrotado.
9. O vencedor é exibido na tela.

---

## Uso de Anotações e Reflexão

O projeto utiliza a anotação `@Validacao` para marcar campos obrigatórios da classe `Personagem`.

A classe `Validador` utiliza Reflection API para percorrer os atributos anotados em tempo de execução e verificar automaticamente se possuem valores válidos.

Essa abordagem reduz o acoplamento entre as classes, facilita a manutenção e permite adicionar novas validações sem alterar a lógica principal do sistema.

---

## Autores

- Elizandra Maria
- Leonardo da Cruz
- João Gabriel
- Gabriel Cancio

Projeto acadêmico desenvolvido para aplicação dos conceitos de:

- Programação Orientada a Objetos
- Padrões de Projeto
- Anotações Java
- Reflection API
- Validação de Objetos