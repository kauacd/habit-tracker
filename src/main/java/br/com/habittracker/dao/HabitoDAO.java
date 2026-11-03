package br.com.habittracker.dao;

import br.com.habittracker.model.Habito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /** Lista SOMENTE os hábitos do usuário informado (RN05). */
    public List<Habito> listarPorUsuario(int usuarioId) {
        String sql = "SELECT id, usuario_id, nome, descricao FROM habitos WHERE usuario_id = ? ORDER BY id";
        List<Habito> lista = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Habito(rs.getInt("id"), rs.getInt("usuario_id"),
                            rs.getString("nome"), rs.getString("descricao")));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar hábitos", e);
        }
        return lista;
    }
}
