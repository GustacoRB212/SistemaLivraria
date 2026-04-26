package com.gustavo.dao;

import com.gustavo.model.Livro;
import com.gustavo.model.Usuario;
import org.postgresql.core.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome_usuario, cpf, multa_pendente) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setString(2, usuario.getCpf());
            stmt.setDouble(3, usuario.getMultaPendente());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // Você precisará adicionar o setId na sua classe Usuario!
                usuario.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(rs.getString("nome_usuario"), rs.getString("cpf"));
                u.setId(rs.getInt("id"));
                u.adicionarMulta(rs.getDouble("multa_pendente"));
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
        return lista;
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome_usuario = ?, multa_pendente = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeUsuario());
            stmt.setDouble(2, usuario.getMultaPendente());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }
    public void carregarLivrosPegados(Usuario usuario) {
        String sql = "SELECT l.* FROM livros l " +
                "JOIN emprestimos e ON l.id = e.id_livro " +
                "WHERE e.id_usuario = ? AND e.data_devolucao IS NULL";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("nome_livro"),
                        rs.getString("autor"),
                        rs.getInt("qntd_disponivel"),
                        rs.getInt("id")
                );
                usuario.getLivrosPegados().add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario(rs.getString("nome_usuario"), rs.getString("cpf"));
                u.setId(rs.getInt("id"));
                u.adicionarMulta(rs.getDouble("multa_pendente"));
                return u;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
        return null;
    }
}