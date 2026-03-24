package com.goias;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        Livro livro = new Livro();
        Usuario usuario = new Usuario();
        Controlador controlador = new Controlador(usuario);
        int opcao;
        do {
            System.out.println("[1] Pegar livro emprestado");
            System.out.println("[2] Devolver livro");
            System.out.println("[3] Sair");
            System.out.println("[4] Cadastrar Livro");
            System.out.println("[5] Visualizar Livros da biblioteca");
            System.out.println("[6] Visualizar Livros pegos");
            System.out.println("[7] Cadastrar Usuario");
            System.out.println("[9]  Consultar multa");
            System.out.println("[10] Pagar multa");
            System.out.print("Sua opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: {
                    controlador.pegarLivroEmprestado();
                    break;
                }
                case 2: {
                    controlador.devolverLivro();
                    System.out.println("Livro devolvido");
                    break;
                }
                case 3:
                    System.out.println("Programa Encerrado");
                    break;
                case 4: {
                    controlador.cadastrarLivro();
                    break;
                }
                case 5: {
                    if (controlador.livros.isEmpty()) {
                        System.out.println("Sem Livros Disponiveis!");
                    }
                    else {
                        for (Livro mostrarLivros : controlador.livros) {
                            System.out.println(mostrarLivros);
                        }
                    }
                    break;
                }
                case 6: {
                        if (controlador.getUsuarioAtual().getLivrosPegados().isEmpty()) {
                            System.out.println("Você ainda não tem livros!");
                        }
                    else {
                        controlador.visualizarLivros();
                    }
                    break;
                }
                case 7: {
                    controlador.cadastrarUsuario();
                    break;
                }
                case 9: controlador.consultarMulta(); break;
                case 10: controlador.pagarMulta(); break;
                default:
                    System.out.println("Opcao Invalida");
            }
        } while (opcao != 3);






    }
}
