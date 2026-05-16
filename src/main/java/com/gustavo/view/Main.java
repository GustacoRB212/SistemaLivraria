package com.gustavo.view;

import com.gustavo.controller.Controlador;
import com.gustavo.dao.LivroDAO;
import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;
import com.gustavo.dao.Conexao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = Conexao.getConnection()) {
            if (connection != null) {
                System.out.println("Conexão com PostgreSQL realizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro crítico: Não foi possível conectar ao banco.");
            return; // Encerra se não houver banco
        }

        Scanner sc = new Scanner(System.in);
        Controlador controlador = new Controlador(null);
        LivroDAO livroDAO = new LivroDAO(); // Usaremos para a opção 5

        int opcao;
        do {
            System.out.println("\n--- MENU BIBLIOTECA ---");
            System.out.println("[1] Pegar livro emprestado");
            System.out.println("[2] Devolver livro");
            System.out.println("[4] Cadastrar Livro");
            System.out.println("[5] Visualizar acervo da biblioteca");
            System.out.println("[6] Visualizar Meus Livros (pegos)");
            System.out.println("[7] Cadastrar/Logar Usuário");
            System.out.println("[9] Consultar multa");
            System.out.println("[10] Pagar multa");
            System.out.println("[3] Sair");
            System.out.print("Sua opção: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    controlador.pegarLivroEmprestado();
                    break;
                case 2:
                    controlador.devolverLivro();
                    break;
                case 3:
                    System.out.println("Programa Encerrado");
                    break;
                case 4:
                    controlador.cadastrarLivro();
                    break;
                case 5:
                    List<Livro> todosLivros = livroDAO.listarTodos();
                    if (todosLivros.isEmpty()) {
                        System.out.println("Sem Livros Disponíveis no Banco!");
                    } else {
                        for (Livro l : todosLivros) System.out.println(l);
                    }
                    break;
                case 6:
                    if (controlador.getUsuarioAtual() == null) {
                        System.out.println("Faça o cadastro/login primeiro (Opção 7).");
                    } else {
                        controlador.visualizarLivros();
                    }
                    break;
                case 7:
                    controlador.cadastrarUsuario();
                    break;
                case 9:
                    controlador.consultarMulta();
                    break;
                case 10:
                    controlador.pagarMulta();
                    break;
                default:
                    System.out.println("Opção Inválida");
            }
        } while (opcao != 3);
    }
}