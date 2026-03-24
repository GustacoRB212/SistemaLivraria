package com.goias;

public class LivroIndisponivelException extends Exception {
    public LivroIndisponivelException(String nomeLivro) {
        super("O livro \"" + nomeLivro + "\" não está disponível no momento.");
    }
}