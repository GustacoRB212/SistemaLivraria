package com.gustavo.dao;

import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;
import java.sql.*;

public class EmprestimoDAO {

    public void registrarEmprestimo(Usuario usuario, Livro livro) {
        String sqlEmprestimo = "INSERT INTO emprestimos (id_usuario, id_livro, data_emprestimo) VALUES (?, ?, CURRENT_DATE)";
        String sqlUpdateLivro = "UPDATE livros SET qntd_disponivel = qntd_disponivel - 1 WHERE id = ?";

        Connection conn = null;
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Inicia uma transação

            try (PreparedStatement stmtEmp = conn.prepareStatement(sqlEmprestimo)) {
                stmtEmp.setInt(1, usuario.getId());
                stmtEmp.setInt(2, livro.getId());
                stmtEmp.executeUpdate();
            }

            try (PreparedStatement stmtLivro = conn.prepareStatement(sqlUpdateLivro)) {
                stmtLivro.setInt(1, livro.getId());
                stmtLivro.executeUpdate();
            }

            conn.commit();
            System.out.println("Empréstimo realizado com sucesso!");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Erro ao registrar empréstimo. Operação cancelada.", e);
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void registrarDevolucao(int idUsuario, int idLivro) {
        String sqlDevolucao = "UPDATE emprestimos SET data_devolucao = CURRENT_DATE, status = 'DEVOLVIDO' " +
                "WHERE id_usuario = ? AND id_livro = ? AND data_devolucao IS NULL";
        String sqlUpdateLivro = "UPDATE livros SET qntd_disponivel = qntd_disponivel + 1 WHERE id = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDev = conn.prepareStatement(sqlDevolucao)) {
                stmtDev.setInt(1, idUsuario);
                stmtDev.setInt(2, idLivro);
                int rows = stmtDev.executeUpdate();

                if (rows == 0) {
                    throw new SQLException("Nenhum empréstimo ativo encontrado para este usuário e livro.");
                }
            }

            try (PreparedStatement stmtLivro = conn.prepareStatement(sqlUpdateLivro)) {
                stmtLivro.setInt(1, idLivro);
                stmtLivro.executeUpdate();
            }

            conn.commit();
            System.out.println("Devolução registrada com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar devolução.", e);
        }
    }
}