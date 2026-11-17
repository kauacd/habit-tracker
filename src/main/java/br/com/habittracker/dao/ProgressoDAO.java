package br.com.habittracker.dao;

import br.com.habittracker.model.Habito;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProgressoDAO implements ProgressoRepositorio {

    @Override
    public Habito buscarHabitoDoUsuario(int habitoId, int usuarioId) {
        String sql = "SELECT id, usuario_id, nome, descricao FROM habitos WHERE id = ? AND usuario_id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            ps.setInt(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Habito(rs.getInt("id"), rs.getInt("usuario_id"),
                            rs.getString("nome"), rs.getString("descricao"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar hábito", e);
        }
    }

    @Override
    public Set<LocalDate> datasConcluidas(int habitoId, LocalDate de, LocalDate ate) {
        String sql = "SELECT data FROM conclusoes WHERE habito_id = ? AND data BETWEEN ? AND ?";
        Set<LocalDate> datas = new HashSet<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            ps.setDate(2, Date.valueOf(de));
            ps.setDate(3, Date.valueOf(ate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    datas.add(rs.getDate(1).toLocalDate());
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao consultar histórico", e);
        }
        return datas;
    }

    @Override
    public int totalConclusoes(int habitoId) {
        String sql = "SELECT COUNT(*) FROM conclusoes WHERE habito_id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao contar conclusões", e);
        }
    }

    @Override
    public Map<Integer, Set<LocalDate>> datasConcluidasPorHabito(int usuarioId, LocalDate de, LocalDate ate) {
        String sql = "SELECT c.habito_id, c.data FROM conclusoes c "
                   + "JOIN habitos h ON h.id = c.habito_id "
                   + "WHERE h.usuario_id = ? AND c.data BETWEEN ? AND ?";
        Map<Integer, Set<LocalDate>> mapa = new HashMap<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(de));
            ps.setDate(3, Date.valueOf(ate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mapa.computeIfAbsent(rs.getInt(1), k -> new HashSet<>()).add(rs.getDate(2).toLocalDate());
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao consultar conclusões", e);
        }
        return mapa;
    }

    @Override
    public Map<LocalDate, Integer> concluidasPorDia(int usuarioId, LocalDate de, LocalDate ate) {
        String sql = "SELECT c.data, COUNT(*) FROM conclusoes c "
                   + "JOIN habitos h ON h.id = c.habito_id "
                   + "WHERE h.usuario_id = ? AND c.data BETWEEN ? AND ? GROUP BY c.data";
        Map<LocalDate, Integer> mapa = new HashMap<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(de));
            ps.setDate(3, Date.valueOf(ate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mapa.put(rs.getDate(1).toLocalDate(), rs.getInt(2));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao consultar o progresso", e);
        }
        return mapa;
    }

    @Override
    public int contarHabitos(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM habitos WHERE usuario_id = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao contar hábitos", e);
        }
    }
}
