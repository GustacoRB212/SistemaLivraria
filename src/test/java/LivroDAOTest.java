package com.gustavo.dao;

import com.gustavo.model.Livro;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LivroDAOTest {

    private LivroDAO livroDAO = new LivroDAO();

    @Test
    void deveSalvarLivro() {

        Livro livro = new Livro(
                "Java",
                "Gustavo",
                5,
                0
        );

        assertDoesNotThrow(() -> {
            livroDAO.salvar(livro);
        });

        assertTrue(livro.getId() > 0);
    }

    @Test
    void deveListarLivros() {

        List<Livro> livros = livroDAO.listarTodos();

        assertNotNull(livros);
    }

    @Test
    void deveAtualizarLivro() {

        Livro livro = new Livro(
                "Livro Teste",
                "Autor",
                3,
                0
        );

        livroDAO.salvar(livro);

        livro.setNomeLivro("Livro Atualizado");
        livro.setAutor("Novo Autor");
        livro.setQntdDisponivel(10);

        assertDoesNotThrow(() -> {
            livroDAO.atualizar(livro);
        });

        List<Livro> livros = livroDAO.listarTodos();

        boolean encontrou = livros.stream().anyMatch(l ->
                l.getId() == livro.getId()
                        && l.getNomeLivro().equals("Livro Atualizado")
                        && l.getAutor().equals("Novo Autor")
                        && l.getQntdDisponivel() == 10
        );

        assertTrue(encontrou);
    }

    @Test
    void deveDeletarLivro() {

        Livro livro = new Livro(
                "Livro Delete",
                "Autor",
                1,
                0
        );

        livroDAO.salvar(livro);

        int id = livro.getId();

        assertDoesNotThrow(() -> {
            livroDAO.deletar(id);
        });

        List<Livro> livros = livroDAO.listarTodos();

        boolean existe = livros.stream()
                .anyMatch(l -> l.getId() == id);

        assertFalse(existe);
    }

    @Test
    void naoDeveSalvarLivroComDadosInvalidos() {

        Livro livro = new Livro(
                null,
                null,
                -1,
                0
        );

        assertThrows(
                RuntimeException.class,
                () -> {
                    livroDAO.salvar(livro);
                }
        );
    }
}