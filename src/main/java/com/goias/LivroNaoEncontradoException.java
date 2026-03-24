package com.goias;

public class LivroNaoEncontradoException extends Exception {
    public LivroNaoEncontradoException(int id) {
        super("Livro com ID " + id + " não encontrado no sistema.");
    }
}