package br.com.habittracker.dao;

import br.com.habittracker.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    /** Retorna o usuário com esse e-mail, ou null se não existir. */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, senha_hash FROM usuarios WHERE email = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id"), rs.getString("nome"),
                            rs.getString("email"), rs.getString("senha_hash"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar usuário", e);
        }
    }

    /** Grava o usuário e preenche o id gerado. Lança exceção se o e-mail já existir (RN03). */
    public void inserir(Usuario usuario) throws EmailJaCadastradoException {
        String sql = "INSERT INTO usuarios (nome, email, senha_hash) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            // SQLState 23xxx = violação de integridade (UNIQUE do e-mail)
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                throw new EmailJaCadastradoException();
            }
            throw new DaoException("Erro ao cadastrar usuário", e);
        }
    }
}
