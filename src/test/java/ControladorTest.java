package com.gustavo.controller;

import com.gustavo.dao.EmprestimoDAO;
import com.gustavo.dao.LivroDAO;
import com.gustavo.dao.UsuarioDAO;
import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControladorTest {

    private Controlador controlador;
    private Usuario usuario;

    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;
    private EmprestimoDAO emprestimoDAO;

    @BeforeEach
    void setup() throws Exception {

        usuario = new Usuario("Gustavo", "12345678900");

        controlador = new Controlador(usuario);

        livroDAO = mock(LivroDAO.class);
        usuarioDAO = mock(UsuarioDAO.class);
        emprestimoDAO = mock(EmprestimoDAO.class);

        injetarDependencia("livroDAO", livroDAO);
        injetarDependencia("usuarioDAO", usuarioDAO);
        injetarDependencia("emprestimoDAO", emprestimoDAO);
    }

    private void injetarDependencia(String nomeCampo, Object objeto) throws Exception {

        Field field = Controlador.class.getDeclaredField(nomeCampo);
        field.setAccessible(true);
        field.set(controlador, objeto);
    }

    private void injetarScanner(String dados) throws Exception {

        Scanner scanner = new Scanner(
                new ByteArrayInputStream(dados.getBytes())
        );

        Field field = Controlador.class.getDeclaredField("sc");
        field.setAccessible(true);
        field.set(controlador, scanner);
    }

    @Test
    void deveRetornarUsuarioAtual() {

        assertEquals(usuario, controlador.getUsuarioAtual());
    }

    @Test
    void deveRetornarTrueQuandoUsuarioExistir() {

        assertTrue(controlador.temUsuarioCadastrado());
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoExistir() {

        Controlador ctrl = new Controlador(null);

        assertFalse(ctrl.temUsuarioCadastrado());
    }

    @Test
    void deveCadastrarLivro() throws Exception {

        String entrada = "Java\nGustavo\n5\n";

        injetarScanner(entrada);

        controlador.cadastrarLivro();

        verify(livroDAO, times(1)).salvar(any(Livro.class));
    }

    @Test
    void deveCadastrarNovoUsuario() throws Exception {

        when(usuarioDAO.buscarPorCpf("123"))
                .thenReturn(null);

        String entrada = "123\nGustavo\n";

        injetarScanner(entrada);

        controlador.cadastrarUsuario();

        verify(usuarioDAO, times(1))
                .salvar(any(Usuario.class));
    }

    @Test
    void deveLogarUsuarioExistente() throws Exception {

        Usuario existente = new Usuario("Joao", "123");

        when(usuarioDAO.buscarPorCpf("123"))
                .thenReturn(existente);

        String entrada = "123\n";

        injetarScanner(entrada);

        controlador.cadastrarUsuario();

        assertEquals(existente, controlador.getUsuarioAtual());
    }

    @Test
    void devePagarMulta() {

        usuario.adicionarMulta(10);

        controlador.pagarMulta();

        assertEquals(0, usuario.getMultaPendente());

        verify(usuarioDAO, times(1)).atualizar(usuario);
    }

    @Test
    void naoDevePagarMultaSeUsuarioNaoPossuir() {

        controlador.pagarMulta();

        verify(usuarioDAO, never()).atualizar(any());
    }

    @Test
    void deveConsultarMulta() {

        usuario.adicionarMulta(20);

        assertDoesNotThrow(() -> controlador.consultarMulta());
    }

    @Test
    void deveConsultarMultaSemErroQuandoNaoPossuir() {

        assertDoesNotThrow(() -> controlador.consultarMulta());
    }

    @Test
    void deveVisualizarLivros() {

        ArrayList<Livro> livros = new ArrayList<>();

        livros.add(new Livro("Java", "Autor", 2, 1));

        usuario.setLivrosPegados(livros);

        assertDoesNotThrow(() -> controlador.visualizarLivros());
    }

    @Test
    void devePegarLivroEmprestado() throws Exception {

        Livro livro = new Livro("Java", "Autor", 5, 1);

        List<Livro> livros = new ArrayList<>();
        livros.add(livro);

        when(livroDAO.listarTodos())
                .thenReturn(livros);

        String entrada = "1\n";

        injetarScanner(entrada);

        controlador.pegarLivroEmprestado();

        verify(emprestimoDAO, times(1))
                .registrarEmprestimo(usuario, livro);
    }

    @Test
    void naoDevePegarLivroComMultaPendente() throws Exception {

        usuario.adicionarMulta(50);

        String entrada = "1\n";

        injetarScanner(entrada);

        assertDoesNotThrow(() -> controlador.pegarLivroEmprestado());

        verify(emprestimoDAO, never())
                .registrarEmprestimo(any(), any());
    }

    @Test
    void naoDeveEmprestarLivroIndisponivel() throws Exception {

        Livro livro = new Livro("Java", "Autor", 0, 1);

        List<Livro> livros = new ArrayList<>();
        livros.add(livro);

        when(livroDAO.listarTodos())
                .thenReturn(livros);

        String entrada = "1\n";

        injetarScanner(entrada);

        controlador.pegarLivroEmprestado();

        verify(emprestimoDAO, never())
                .registrarEmprestimo(any(), any());
    }

    @Test
    void deveDevolverLivroSemMulta() throws Exception {

        String entrada = "1\n0\n";

        injetarScanner(entrada);

        controlador.devolverLivro();

        verify(emprestimoDAO, times(1))
                .registrarDevolucao(usuario.getId(), 1);
    }

    @Test
    void deveDevolverLivroComMulta() throws Exception {

        String entrada = "1\n5\n";

        injetarScanner(entrada);

        controlador.devolverLivro();

        assertTrue(usuario.getMultaPendente() > 0);

        verify(usuarioDAO, times(1))
                .atualizar(usuario);

        verify(emprestimoDAO, times(1))
                .registrarDevolucao(usuario.getId(), 1);
    }

    @Test
    void naoDeveDevolverLivroSemUsuario() {

        Controlador ctrl = new Controlador(null);

        assertDoesNotThrow(ctrl::devolverLivro);
    }

    @Test
    void naoDevePegarLivroSemUsuario() {

        Controlador ctrl = new Controlador(null);

        assertDoesNotThrow(ctrl::pegarLivroEmprestado);
    }
}