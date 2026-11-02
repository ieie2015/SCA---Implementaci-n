package org.seminario.dao;

import org.seminario.model.DisponibilidadSala;
import org.seminario.model.EstadoDisponibilidad;
import org.seminario.model.Sala;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.seminario.util.ConexionBD;

public class DisponibilidadSalaDAO {

    public boolean guardar(
            DisponibilidadSala disponibilidad) {
        return false;
    }

    public boolean modificar(
            DisponibilidadSala disponibilidad) {
        return false;
    }

    public List<DisponibilidadSala> buscarPorFecha(
            LocalDate fecha) throws SQLException {
        String sql = """
                SELECT ds.idDISPONIBILIDAD, ds.FECHA, ds.HORA_DESDE,
                       ds.HORA_HASTA, ds.ESTADO, ds.OBSERVACIONES,
                       s.idSALA, s.CODIGO, s.NOMBRE, s.UBICACION,
                       s.CAPACIDAD, s.ACTIVA
                  FROM DISPONIBILIDAD_SALA ds
                  JOIN SALA s ON s.idSALA = ds.SALA_idSALA
                 WHERE ds.FECHA = ?
                   AND ds.ESTADO = 'DISPONIBLE'
                 ORDER BY ds.HORA_DESDE
                """;
        List<DisponibilidadSala> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<DisponibilidadSala> buscarPorSala(
            int idSala) throws SQLException {
        return buscarPorFechaYSalas(null, idSala);
    }

    public List<DisponibilidadSala> buscarPorFechaYSalas(
            LocalDate fecha, int idSala) throws SQLException {
        String sql = """
                SELECT ds.idDISPONIBILIDAD, ds.FECHA, ds.HORA_DESDE,
                       ds.HORA_HASTA, ds.ESTADO, ds.OBSERVACIONES,
                       s.idSALA, s.CODIGO, s.NOMBRE, s.UBICACION,
                       s.CAPACIDAD, s.ACTIVA
                  FROM DISPONIBILIDAD_SALA ds
                  JOIN SALA s ON s.idSALA = ds.SALA_idSALA
                 WHERE ds.SALA_idSALA = ?
                   AND ds.ESTADO = 'DISPONIBLE'
                   AND (? IS NULL OR ds.FECHA = ?)
                 ORDER BY ds.FECHA, ds.HORA_DESDE
                """;
        List<DisponibilidadSala> lista = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setObject(2, fecha);
            ps.setObject(3, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private DisponibilidadSala mapear(ResultSet rs) throws SQLException {
        Sala sala = new Sala(
                rs.getInt("idSALA"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("UBICACION"),
                rs.getInt("CAPACIDAD"),
                rs.getBoolean("ACTIVA"),
                null
        );
        Time desde = rs.getTime("HORA_DESDE");
        Time hasta = rs.getTime("HORA_HASTA");
        return new DisponibilidadSala(
                rs.getInt("idDISPONIBILIDAD"),
                rs.getDate("FECHA").toLocalDate(),
                desde.toLocalTime(),
                hasta.toLocalTime(),
                EstadoDisponibilidad.valueOf(rs.getString("ESTADO").toUpperCase()),
                rs.getString("OBSERVACIONES"),
                sala
        );
    }
}