package br.com.habittracker.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ConclusaoDAO implements ConclusaoRepositorio {

    @Override
    public boolean habitoPertenceAoUsuario(int habitoId, int usuarioId) {
        String sql = "SELECT 1 FROM habitos WHERE id = ? AND usuario_id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            ps.setInt(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao verificar o dono do hábito", e);
        }
    }

    @Override
    public boolean existeConclusao(int habitoId, LocalDate data) {
        String sql = "SELECT 1 FROM conclusoes WHERE habito_id = ? AND data = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            ps.setDate(2, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao consultar conclusão", e);
        }
    }

    @Override
    public void inserir(int habitoId, LocalDate data) throws ConclusaoJaExisteException {
        String sql = "INSERT INTO conclusoes (habito_id, data) VALUES (?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            ps.setDate(2, Date.valueOf(data));
            ps.executeUpdate();
        } catch (SQLException e) {
            // SQLState 23xxx = violação da UNIQUE (habito_id, data): duplo clique, outra aba, etc.
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                throw new ConclusaoJaExisteException();
            }
            throw new DaoException("Erro ao registrar conclusão", e);
        }
    }
}
