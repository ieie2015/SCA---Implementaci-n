package org.seminario.dao;

import org.seminario.model.DisponibilidadParticipante;
import org.seminario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadParticipanteDAO {

    public boolean guardar(
            DisponibilidadParticipante disponibilidad) {
        return false;
    }

    public boolean modificar(
            DisponibilidadParticipante disponibilidad) {
        return false;
    }

    public List<DisponibilidadParticipante>
    buscarPorFecha(
            LocalDate fecha) throws SQLException {
        return new ArrayList<>();
    }

    public List<DisponibilidadParticipante>
    buscarPorParticipante(
            int idParticipante) throws SQLException {
        return new ArrayList<>();
    }

    public boolean estaDisponible(
            int idParticipante,
            LocalDate fecha,
            java.time.LocalTime horaInicio,
            java.time.LocalTime horaFin) throws SQLException {
        String sql = """
                SELECT 1
                  FROM DISPONIBILIDAD_PARTICIPANTE
                 WHERE PARTICIPANTE_idPARTICIPANTE = ?
                   AND FECHA = ?
                   AND ESTADO = 'DISPONIBLE'
                   AND HORA_DESDE <= ?
                   AND HORA_HASTA >= ?
                 LIMIT 1
                """;
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idParticipante);
            ps.setObject(2, fecha);
            ps.setObject(3, horaInicio);
            ps.setObject(4, horaFin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}