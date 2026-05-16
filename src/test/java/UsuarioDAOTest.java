package com.gustavo.dao;

import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private LivroDAO livroDAO = new LivroDAO();
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @Test
    void deveSalvarUsuario() {

        Usuario usuario = new Usuario(
                "Gustavo",
                "12345678901"
        );

        assertDoesNotThrow(() -> {
            usuarioDAO.salvar(usuario);
        });

        assertTrue(usuario.getId() > 0);
    }

    @Test
    void deveListarUsuarios() {

        List<Usuario> usuarios = usuarioDAO.listarTodos();

        assertNotNull(usuarios);
    }

    @Test
    void deveAtualizarUsuario() {

        Usuario usuario = new Usuario(
                "Teste",
                "99999999999"
        );

        usuarioDAO.salvar(usuario);

        usuario.adicionarMulta(20);

        usuario.setNomeUsuario("Atualizado");

        assertDoesNotThrow(() -> {
            usuarioDAO.atualizar(usuario);
        });

        Usuario usuarioAtualizado =
                usuarioDAO.buscarPorCpf("99999999999");

        assertNotNull(usuarioAtualizado);

        assertEquals(
                "Atualizado",
                usuarioAtualizado.getNomeUsuario()
        );

        assertEquals(
                20,
                usuarioAtualizado.getMultaPendente()
        );
    }

    @Test
    void deveDeletarUsuario() {

        Usuario usuario = new Usuario(
                "Delete",
                "88888888888"
        );

        usuarioDAO.salvar(usuario);

        int id = usuario.getId();

        assertDoesNotThrow(() -> {
            usuarioDAO.deletar(id);
        });

        List<Usuario> usuarios = usuarioDAO.listarTodos();

        boolean existe = usuarios.stream()
                .anyMatch(u -> u.getId() == id);

        assertFalse(existe);
    }

    @Test
    void deveBuscarUsuarioPorCpf() {

        Usuario usuario = new Usuario(
                "Busca",
                "77777777777"
        );

        usuarioDAO.salvar(usuario);

        Usuario encontrado =
                usuarioDAO.buscarPorCpf("77777777777");

        assertNotNull(encontrado);

        assertEquals(
                "Busca",
                encontrado.getNomeUsuario()
        );
    }

    @Test
    void deveRetornarNullQuandoCpfNaoExistir() {

        Usuario usuario =
                usuarioDAO.buscarPorCpf("00000000000");

        assertNull(usuario);
    }

    @Test
    void deveCarregarLivrosPegados() {

        Usuario usuario = new Usuario(
                "Emprestimo",
                "66666666666"
        );

        usuarioDAO.salvar(usuario);

        Livro livro = new Livro(
                "Java",
                "Autor",
                5,
                0
        );

        livroDAO.salvar(livro);

        emprestimoDAO.registrarEmprestimo(
                usuario,
                livro
        );

        usuarioDAO.carregarLivrosPegados(usuario);

        assertFalse(
                usuario.getLivrosPegados().isEmpty()
        );
    }

    @Test
    void naoDeveSalvarUsuarioInvalido() {

        Usuario usuario = new Usuario(
                null,
                null
        );

        assertThrows(
                RuntimeException.class,
                () -> {
                    usuarioDAO.salvar(usuario);
                }
        );
    }
}