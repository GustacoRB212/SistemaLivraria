package com.gustavo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LivroTest {

    @Test
    void deveCriarLivroComConstrutorVazio() {

        Livro livro = new Livro();

        assertNotNull(livro);
    }

    @Test
    void deveCriarLivroComConstrutorCompleto() {

        Livro livro = new Livro(
                "Java",
                "Gustavo",
                5,
                1
        );

        assertEquals("Java", livro.getNomeLivro());
        assertEquals("Gustavo", livro.getAutor());
        assertEquals(5, livro.getQntdDisponivel());
        assertEquals(1, livro.getId());
    }

    @Test
    void deveAlterarNomeLivro() {

        Livro livro = new Livro();

        livro.setNomeLivro("Clean Code");

        assertEquals(
                "Clean Code",
                livro.getNomeLivro()
        );
    }

    @Test
    void deveAlterarAutor() {

        Livro livro = new Livro();

        livro.setAutor("Robert Martin");

        assertEquals(
                "Robert Martin",
                livro.getAutor()
        );
    }

    @Test
    void deveAlterarId() {

        Livro livro = new Livro();

        livro.setId(10);

        assertEquals(10, livro.getId());
    }

    @Test
    void deveAlterarQuantidadeDisponivel() {

        Livro livro = new Livro();

        livro.setQntdDisponivel(20);

        assertEquals(
                20,
                livro.getQntdDisponivel()
        );
    }

    @Test
    void deveRetornarToStringCorreto() {

        Livro livro = new Livro(
                "Java",
                "Gustavo",
                5,
                1
        );

        String esperado =
                "Nome do livro: Java\n" +
                        "Id: 1\n" +
                        "Quantidade disponivel: 5\n" +
                        "=====================================";

        assertEquals(
                esperado,
                livro.toString()
        );
    }

    @Test
    void deveRetornarToStringUsuarioCorreto() {

        Livro livro = new Livro(
                "Java",
                "Gustavo",
                5,
                1
        );

        String esperado =
                "Nome do livro: Java\n" +
                        "Id: 1\n" +
                        "=====================================";

        assertEquals(
                esperado,
                livro.toStringUsuario()
        );
    }
}