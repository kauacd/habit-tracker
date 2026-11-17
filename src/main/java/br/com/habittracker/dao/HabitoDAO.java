package br.com.habittracker.dao;

import br.com.habittracker.model.Habito;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitoDAO {

    public void inserir(Habito habito) {
        String sql = "INSERT INTO habitos (usuario_id, nome, descricao) VALUES (?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habito.getUsuarioId());
            ps.setString(2, habito.getNome());
            ps.setString(3, habito.getDescricao());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar hábito", e);
        }
    }

    /**
     * Lista SOMENTE os hábitos do usuário informado (RN05), já indicando
     * quais foram concluídos na data "hoje" (US03).
     */
    public List<Habito> listarPorUsuario(int usuarioId, LocalDate hoje) {
        String sql = "SELECT h.id, h.usuario_id, h.nome, h.descricao, (c.id IS NOT NULL) AS concluido_hoje "
                   + "FROM habitos h "
                   + "LEFT JOIN conclusoes c ON c.habito_id = h.id AND c.data = ? "
                   + "WHERE h.usuario_id = ? ORDER BY h.id";
        List<Habito> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(hoje));
            ps.setInt(2, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Habito h = new Habito(rs.getInt("id"), rs.getInt("usuario_id"),
                            rs.getString("nome"), rs.getString("descricao"));
                    h.setConcluidoHoje(rs.getBoolean("concluido_hoje"));
                    lista.add(h);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar hábitos", e);
        }
        return lista;
    }
}
