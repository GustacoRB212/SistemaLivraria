package com.gustavo.controller;

import com.gustavo.dao.LivroDAO;
import com.gustavo.dao.UsuarioDAO;
import com.gustavo.dao.EmprestimoDAO;
import com.gustavo.exceptions.LivroIndisponivelException;
import com.gustavo.exceptions.LivroNaoEncontradoException;
import com.gustavo.exceptions.MultaPendenteException;
import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;

import java.util.Scanner;
import java.util.List;

public class Controlador {

    private Usuario usuarioAtual;
    private static final double VALOR_MULTA_POR_DIA = 2.50;
    private Scanner sc = new Scanner(System.in);

    private LivroDAO livroDAO = new LivroDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    public Controlador(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

    public void cadastrarLivro() {
        System.out.print("Digite o nome do livro: ");
        String nome = sc.nextLine();
        System.out.print("Digite o nome do autor: ");
        String autor = sc.nextLine();

        int qntd;
        do {
            System.out.print("Quantidade disponível: ");
            qntd = sc.nextInt();
            if (qntd < 1) System.out.println("Quantidade Inválida!");
        } while (qntd < 1);
        sc.nextLine();

        Livro novoLivro = new Livro(nome, autor, qntd, 0);
        livroDAO.salvar(novoLivro); // O ID é gerado pelo Postgres SERIAL
        System.out.println("Livro salvo no banco de dados!");
    }

    public void pegarLivroEmprestado() {
        if (usuarioAtual == null) { System.out.println("Nenhum usuário logado!"); return; }

        try {
            verificarMultaPendente();

            List<Livro> livrosNoBanco = livroDAO.listarTodos();
            for (Livro l : livrosNoBanco) System.out.println(l);

            System.out.print("Digite o id do Livro: ");
            int id = sc.nextInt(); sc.nextLine();

            Livro livro = buscarLivroNoBanco(id);
            verificarDisponibilidade(livro);

            emprestimoDAO.registrarEmprestimo(usuarioAtual, livro);

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    public void devolverLivro() {
        if (usuarioAtual == null) return;

        // Carrega livros que o usuário realmente tem no banco
        usuarioDAO.carregarLivrosPegados(usuarioAtual);
        visualizarLivros();

        System.out.print("Digite o id do livro para devolver: ");
        int idLivro = sc.nextInt();

        System.out.print("Dias de atraso: ");
        int dias = sc.nextInt(); sc.nextLine();

        if (dias > 0) {
            usuarioAtual.adicionarMulta(dias * VALOR_MULTA_POR_DIA);
            usuarioDAO.atualizar(usuarioAtual); // Salva a multa no banco
        }

        emprestimoDAO.registrarDevolucao(usuarioAtual.getId(), idLivro);
    }

    public void cadastrarUsuario() {
        System.out.print("Digite o CPF para entrar ou cadastrar: ");
        String cpf = sc.nextLine();

        // Tenta buscar no banco primeiro
        Usuario existente = usuarioDAO.buscarPorCpf(cpf);

        if (existente != null) {
            this.usuarioAtual = existente;
            System.out.println("Bem-vindo de volta, " + existente.getNomeUsuario() + "!");
        } else {
            // Se não existir, aí sim pede o nome e cadastra
            System.out.print("Usuário não encontrado. Digite o nome para novo cadastro: ");
            String nome = sc.nextLine();
            Usuario novo = new Usuario(nome, cpf);
            usuarioDAO.salvar(novo);
            this.usuarioAtual = novo;
            System.out.println("Novo usuário cadastrado com sucesso!");
        }
    }

    public void pagarMulta() {
        if (usuarioAtual == null || !usuarioAtual.temMulta()) return;

        usuarioAtual.pagarMulta();
        usuarioDAO.atualizar(usuarioAtual); // Atualiza o status no banco
        System.out.println("Multa zerada no banco de dados.");
    }

    private Livro buscarLivroNoBanco(int id) throws LivroNaoEncontradoException {
        return livroDAO.listarTodos().stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElseThrow(() -> new LivroNaoEncontradoException(id));
    }

    private void verificarDisponibilidade(Livro livro) throws LivroIndisponivelException {
        if (livro.getQntdDisponivel() <= 0)
            throw new LivroIndisponivelException(livro.getNomeLivro());
    }

    private void verificarMultaPendente() throws MultaPendenteException {
        if (usuarioAtual.getMultaPendente() > 0)
            throw new MultaPendenteException(usuarioAtual.getMultaPendente());
    }

    public void visualizarLivros() {
        System.out.println("--- Seus Livros Atuais ---");
        usuarioAtual.getLivrosPegados().clear(); // Limpa para não duplicar
        usuarioDAO.carregarLivrosPegados(usuarioAtual);
        usuarioAtual.getLivrosPegados().forEach(l -> System.out.println(l.toStringUsuario()));
    }
    public Usuario getUsuarioAtual() {
        return this.usuarioAtual;
    }

    public void consultarMulta() {
        if (usuarioAtual == null) {
            System.out.println("Nenhum usuário logado! Use a opção 7.");
            return;
        }
        if (usuarioAtual.temMulta()) {
            System.out.printf("Multa pendente para %s: R$ %.2f%n",
                    usuarioAtual.getNomeUsuario(),
                    usuarioAtual.getMultaPendente());
        } else {
            System.out.println("Você não possui multas pendentes.");
        }
    }

    public boolean temUsuarioCadastrado() {
        return this.usuarioAtual != null;
    }
}