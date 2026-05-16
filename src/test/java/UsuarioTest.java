package com.gustavo.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void deveCriarUsuarioComConstrutorCompleto() {

        Usuario usuario = new Usuario(
                "Gustavo",
                "12345678900"
        );

        assertEquals(
                "Gustavo",
                usuario.getNomeUsuario()
        );

        assertEquals(
                "12345678900",
                usuario.getCpf()
        );
    }

    @Test
    void deveCriarUsuarioComConstrutorVazio() {

        Usuario usuario = new Usuario();

        assertNotNull(usuario);
    }

    @Test
    void deveAlterarCpf() {

        Usuario usuario = new Usuario();

        usuario.setCpf("99999999999");

        assertEquals(
                "99999999999",
                usuario.getCpf()
        );
    }

    @Test
    void deveAlterarId() {

        Usuario usuario = new Usuario();

        usuario.setId(10);

        assertEquals(10, usuario.getId());
    }

    @Test
    void deveAlterarNomeUsuario() {

        Usuario usuario = new Usuario();

        usuario.setNomeUsuario("Joao");

        assertEquals(
                "Joao",
                usuario.getNomeUsuario()
        );
    }

    @Test
    void deveAdicionarLivroAoHistorico() {

        Usuario usuario = new Usuario();

        Livro livro = new Livro(
                "Java",
                "Autor",
                5,
                1
        );

        usuario.acidonarHistorico(livro);

        assertEquals(
                1,
                usuario.getHistorico().size()
        );

        assertEquals(
                livro,
                usuario.getHistorico().get(0)
        );
    }

    @Test
    void deveAdicionarMulta() {

        Usuario usuario = new Usuario();

        usuario.adicionarMulta(25);

        assertEquals(
                25,
                usuario.getMultaPendente()
        );
    }

    @Test
    void deveSomarMultas() {

        Usuario usuario = new Usuario();

        usuario.adicionarMulta(10);
        usuario.adicionarMulta(5);

        assertEquals(
                15,
                usuario.getMultaPendente()
        );
    }

    @Test
    void devePagarMulta() {

        Usuario usuario = new Usuario();

        usuario.adicionarMulta(50);

        usuario.pagarMulta();

        assertEquals(
                0,
                usuario.getMultaPendente()
        );
    }

    @Test
    void deveRetornarTrueQuandoPossuirMulta() {

        Usuario usuario = new Usuario();

        usuario.adicionarMulta(10);

        assertTrue(usuario.temMulta());
    }

    @Test
    void deveRetornarFalseQuandoNaoPossuirMulta() {

        Usuario usuario = new Usuario();

        assertFalse(usuario.temMulta());
    }

    @Test
    void deveAlterarLivrosPegados() {

        Usuario usuario = new Usuario();

        ArrayList<Livro> livros =
                new ArrayList<>();

        livros.add(
                new Livro(
                        "Java",
                        "Autor",
                        1,
                        1
                )
        );

        usuario.setLivrosPegados(livros);

        assertEquals(
                1,
                usuario.getLivrosPegados().size()
        );
    }

    @Test
    void historicoDeveComecarVazio() {

        Usuario usuario = new Usuario();

        assertTrue(
                usuario.getHistorico().isEmpty()
        );
    }

    @Test
    void livrosPegadosDevemComecarVazios() {

        Usuario usuario = new Usuario();

        assertTrue(
                usuario.getLivrosPegados().isEmpty()
        );
    }
}