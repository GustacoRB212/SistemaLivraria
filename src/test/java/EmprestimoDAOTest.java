package com.gustavo.dao;

import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoDAOTest {

    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @Test
    void deveRegistrarEmprestimo() {

        Usuario usuario = new Usuario("Gustavo", "123");
        usuario.setId(1);

        Livro livro = new Livro("Java", "Autor", 5, 1);

        assertDoesNotThrow(() -> {
            emprestimoDAO.registrarEmprestimo(usuario, livro);
        });
    }

    @Test
    void deveRegistrarDevolucao() {

        Usuario usuario = new Usuario("Gustavo", "123");
        usuario.setId(1);

        Livro livro = new Livro("Java", "Autor", 5, 1);

        emprestimoDAO.registrarEmprestimo(usuario, livro);

        assertDoesNotThrow(() -> {
            emprestimoDAO.registrarDevolucao(1, 1);
        });
    }

    @Test
    void deveLancarErroAoDevolverLivroInexistente() {

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> {
                    emprestimoDAO.registrarDevolucao(9999, 9999);
                }
        );

        assertTrue(
                exception.getMessage()
                        .contains("Erro ao registrar devolução")
        );
    }

    @Test
    void deveLancarErroAoEmprestarLivroComUsuarioInvalido() {

        Usuario usuario = new Usuario("Teste", "000");
        usuario.setId(-1);

        Livro livro = new Livro("Livro", "Autor", 1, 1);

        assertThrows(
                RuntimeException.class,
                () -> {
                    emprestimoDAO.registrarEmprestimo(usuario, livro);
                }
        );
    }
}