package jogo.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import jogo.annotation.Validacao;

/**
 * Classe utilitária que fornece validação genérica de objetos usando reflexão.
 * Verifica se os campos anotados com {@link Validacao} não possuem valor {@code null}.
 */
public class Validador {

    /**
     * Valida um objeto, percorrendo todos os seus campos declarados (inclusive privados)
     * e coletando as descrições daqueles que estão anotados com {@code @Validacao} e
     * cujo valor é {@code null}.
     *
     * @param objeto o objeto a ser validado (não pode ser {@code null})
     * @return uma lista de strings contendo as descrições dos campos obrigatórios que estão nulos.
     *         Se todos os campos anotados estiverem preenchidos, retorna uma lista vazia.
     *
     * @throws NullPointerException se {@code objeto} for {@code null}
     * @throws IllegalAccessException se ocorrer erro de acesso a campo (não tratado, mas capturado internamente)
     *
     * @see Validacao
     */
    public static List<String> validar(Object objeto) {
        List<String> erros = new ArrayList<>();
        Class<?> classe = objeto.getClass();
        Field[] campos = classe.getDeclaredFields();

        for (Field campo : campos) {
            if (campo.isAnnotationPresent(Validacao.class)) {
                campo.setAccessible(true); // permite acessar atributos privados
                try {
                    Object valor = campo.get(objeto);
                    if (valor == null) {
                        Validacao validacao = campo.getAnnotation(Validacao.class);
                        erros.add(validacao.descricao());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // em produção, log seria mais apropriado
                }
            }
        }
        return erros;
    }
}