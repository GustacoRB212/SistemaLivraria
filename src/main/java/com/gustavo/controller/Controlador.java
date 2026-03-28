package com.gustavo.controller;

import com.gustavo.exceptions.LivroIndisponivelException;
import com.gustavo.exceptions.LivroNaoEncontradoException;
import com.gustavo.exceptions.MultaPendenteException;
import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.ArrayList;

public class Controlador {

    private Usuario usuarioAtual;

    private static final double VALOR_MULTA_POR_DIA = 2.50;

    public Controlador(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

    private final ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    Scanner sc = new Scanner(System.in);


    public ArrayList<Livro> getLivros() {
        return livros;
    }

    private LocalDate diaQueFoiPego;
    private LocalDate prazoEntrega;

    public void cadastrarLivro() {
        System.out.print("Digite o nome do livro: ");
        String nome = sc.nextLine();
        System.out.print("Digite o nome do autor: ");
        String autor = sc.nextLine();

        int qntdDisponivel = 0;
        do {
            System.out.print("Quantidade disponivel: ");
            qntdDisponivel = sc.nextInt();
            if (qntdDisponivel < 1) {
                System.out.println("Quantidade Invalida, Tente Novamente!");
            }
        } while (qntdDisponivel < 1);



        System.out.print("Digite o id do livro: ");
        int id = sc.nextInt();
        sc.nextLine();
        livros.add(new Livro(nome, autor, qntdDisponivel, id));
        System.out.println("Livro cadastrado com sucesso!");
    }

    int qntdSobrando = 0;


    public void pegarLivroEmprestado() {
        if (!temUsuarioCadastrado()) { System.out.println("Sem cadastro!"); return; }
        try {
            verificarMultaPendente();
        } catch (MultaPendenteException e) {
            System.out.println("ERRO: " + e.getMessage()); return;
        }
        for (Livro l : livros) System.out.println(l);
        System.out.print("Digite o id do Livro: ");
        int id = sc.nextInt(); sc.nextLine();
        try {
            Livro livro = buscarLivroPorId(id);
            verificarDisponibilidade(livro);
            if (usuarioAtual.getLivrosPegados().size() >= 3) {
                System.out.println("Limite de 3 livros atingido."); return;
            }
            livro.setQntdDisponivel(livro.getQntdDisponivel() - 1);
            usuarioAtual.getLivrosPegados().add(livro);
            usuarioAtual.acidonarHistorico(livro);
            LocalDate hoje = LocalDate.now();
            System.out.println("Livro pego! Prazo: " + hoje + " até " + hoje.plusDays(7));
        } catch (LivroNaoEncontradoException | LivroIndisponivelException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    public void visualizarLivros() {
        System.out.println("Livros Pegos: ");
        for (Livro livrosPegos : usuarioAtual.getLivrosPegados()) {
            System.out.println(livrosPegos.toStringUsuario());
        }

    }

    public void devolverLivro() {

        if (temUsuarioCadastrado()) {

            System.out.println("Livros Pegos: ");
            for (Livro livrosPegos : usuarioAtual.getLivrosPegados()) {
                System.out.println(livrosPegos.toStringUsuario());
            }
            System.out.print("Digite o id do livro: ");
            int id = sc.nextInt();

            for (int i = 0; i < usuarioAtual.getLivrosPegados().size(); i++) {
                Livro livro = usuarioAtual.getLivrosPegados().get(i);
                if (livro.getId() == id) {
                    usuarioAtual.getLivrosPegados().remove(i);
                    int qntdLivrosUsuarioDeve = usuarioAtual.getLivrosPegados().size();
                    livro.setQntdDisponivel(livro.getQntdDisponivel() + 1);
                    System.out.print("Dias de atraso (0 se nenhum): ");
                    int diasAtraso = sc.nextInt(); sc.nextLine();
                    if (diasAtraso > 0) {
                        double multa = diasAtraso * VALOR_MULTA_POR_DIA;
                        usuarioAtual.adicionarMulta(multa);
                        System.out.printf("Multa de R$ %.2f registrada.%n", multa);
                    }
                    System.out.println("Livro devolvido com sucesso!");
                    break;

                }
            }
        }
    }

    public void cadastrarUsuario() {
        boolean cpfValido;
        System.out.print("DIgite seu nome: ");
        String nome = sc.nextLine();
        String cpf;
        do {
            cpfValido = true;
            System.out.print("Digite seu cpf: ");
            cpf = sc.nextLine();
            if (cpf.length() != 11) {
                cpfValido = false;
                System.out.println("CPF Invalido, tente novamnte");
            }
        } while (!cpfValido);
        Usuario novo = new Usuario(nome, cpf);
        usuarios.add(novo);
        usuarioAtual = novo;

        System.out.println("Cadastro realizado com sucesso");

    }

    public void mostrarHistoricoUsuario() {
        for (Livro historicoLivros : usuarioAtual.getHistorico()) {
            System.out.println(historicoLivros);
        }
    }

    public boolean temUsuarioCadastrado() {
        return !usuarios.isEmpty();
    }

    public void pagarMulta() {
        if (!temUsuarioCadastrado()) { System.out.println("Sem cadastro!"); return; }
        if (!usuarioAtual.temMulta()) { System.out.println("Sem multas pendentes."); return; }
        System.out.printf("Multa pendente: R$ %.2f%n", usuarioAtual.getMultaPendente());
        System.out.print("Confirmar pagamento? (S/N): ");
        String confirmacao = sc.nextLine().trim().toUpperCase();
        if (confirmacao.equals("S")) {
            usuarioAtual.pagarMulta();
            System.out.println("Multa paga com sucesso!");
        } else {
            System.out.println("Pagamento cancelado.");
        }
    }

    public void consultarMulta() {
        if (!temUsuarioCadastrado()) { System.out.println("Sem cadastro!"); return; }
        if (usuarioAtual.temMulta())
            System.out.printf("Multa pendente: R$ %.2f%n", usuarioAtual.getMultaPendente());
        else
            System.out.println("Você não possui multas pendentes.");
    }

    public LocalDate getDiaQueFoiPego() {
        return diaQueFoiPego;
    }

    public LocalDate getPrazoEntrega() {
        return prazoEntrega;
    }

    private Livro buscarLivroPorId(int id) throws LivroNaoEncontradoException {
        for (Livro livro : livros) {
            if (livro.getId() == id) return livro;
        }
        throw new LivroNaoEncontradoException(id);
    }

    private void verificarDisponibilidade(Livro livro) throws LivroIndisponivelException {
        if (livro.getQntdDisponivel() <= 0)
            throw new LivroIndisponivelException(livro.getNomeLivro());
    }

    private void verificarMultaPendente() throws MultaPendenteException {
        if (usuarioAtual.temMulta())
            throw new MultaPendenteException(usuarioAtual.getMultaPendente());
    }

    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

}
