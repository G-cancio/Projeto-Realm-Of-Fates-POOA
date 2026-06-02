package jogo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação utilizada para marcar campos de um objeto que devem ser validados
 * quanto à presença de valor (não nulos) durante o processo de validação por reflexão.
 *
 * A anotação é mantida em tempo de execução e pode ser aplicada apenas a campos.
 *
 * @see jogo.util.Validador
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validacao {

    /**
     * Descrição amigável do campo que será exibida em mensagens de erro
     * quando o campo estiver vazio ou nulo.
     *
     * @return descrição textual do campo obrigatório
     */
    String descricao();
}