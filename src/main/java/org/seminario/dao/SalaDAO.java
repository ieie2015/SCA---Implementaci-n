package org.seminario.dao;

import org.seminario.model.Sala;
import org.seminario.model.TipoSala;
import org.seminario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SalaDAO {

    public List<Sala> listarDisponibles(
            long idTipoAudiencia,
            LocalDate fecha) throws SQLException {
        String sql = """
                SELECT DISTINCT s.idSALA, s.CODIGO, s.NOMBRE,
                       s.UBICACION, s.CAPACIDAD, s.ACTIVA,
                       ts.idTIPO_SALA, ts.CODIGO AS TS_CODIGO,
                       ts.DESCRIPCION AS TS_DESCRIPCION,
                       ts.ACTIVA AS TS_ACTIVA
                  FROM SALA s
                  JOIN TIPO_SALA ts ON ts.idTIPO_SALA = s.idTIPO_SALA
                  JOIN TIPO_AUDIENCIA ta
                    ON ta.idTIPO_SALA = s.idTIPO_SALA
                  JOIN DISPONIBILIDAD_SALA ds
                    ON ds.SALA_idSALA = s.idSALA
                 WHERE ta.idTIPO_AUDIENCIA = ?
                   AND s.ACTIVA = 1
                   AND ts.ACTIVA = 1
                   AND ds.FECHA = ?
                   AND ds.ESTADO = 'DISPONIBLE'
                 ORDER BY s.NOMBRE
                """;
        List<Sala> salas = new ArrayList<>();

                System.out.println(
                        "[CU02][SalaDAO] Consultando disponibilidad de salas. "
                                + "idTipoAudiencia=" + idTipoAudiencia
                                + ", fecha=" + fecha);
                try (Connection con = ConexionBD.conectar();
                     PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setLong(1, idTipoAudiencia);
                    ps.setObject(2, fecha);
                    try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    salas.add(mapear(rs));
                }
            }
        }
        System.out.println(
                "[CU02][SalaDAO] Resultado para fecha " + fecha
                        + ": " + salas.size() + " sala(s) disponible(s)"
                        + (salas.isEmpty() ? " []" : " " + salas));
        return salas;
    }

    public Set<LocalDate> listarFechasDisponibles(
            long idTipoAudiencia) throws SQLException {
        String sql = """
                SELECT DISTINCT ds.FECHA
                  FROM DISPONIBILIDAD_SALA ds
                  JOIN SALA s ON s.idSALA = ds.SALA_idSALA
                  JOIN TIPO_AUDIENCIA ta
                    ON ta.idTIPO_SALA = s.idTIPO_SALA
                  JOIN TIPO_SALA ts
                    ON ts.idTIPO_SALA = s.idTIPO_SALA
                 WHERE ta.idTIPO_AUDIENCIA = ?
                   AND s.ACTIVA = 1
                   AND ts.ACTIVA = 1
                   AND ds.ESTADO = 'DISPONIBLE'
                   AND ds.FECHA >= CURRENT_DATE()
                 ORDER BY ds.FECHA
                """;

        Set<LocalDate> fechas = new HashSet<>();

        System.out.println(
                "[CU02][SalaDAO] Consultando fechas disponibles. "
                        + "idTipoAudiencia=" + idTipoAudiencia);
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idTipoAudiencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fechas.add(rs.getDate("FECHA").toLocalDate());
                }
            }
        }
        System.out.println(
                "[CU02][SalaDAO] Fechas devueltas por la consulta: "
                        + fechas);
        return fechas;
    }

    public boolean guardar(Sala sala) {
        return false;
    }

    public boolean modificar(Sala sala) {
        return false;
    }

    public boolean activar(
            int idSala) {
        return false;
    }

    public boolean desactivar(
            int idSala) {
        return false;
    }

    public Sala buscarPorId(
            int idSala) {
        return null;
    }

    public List<Sala> listarTodas() {
        return new ArrayList<>();
    }

    private Sala mapear(ResultSet rs) throws SQLException {
        TipoSala tipoSala = new TipoSala(
                rs.getInt("idTIPO_SALA"),
                rs.getString("TS_CODIGO"),
                rs.getString("TS_DESCRIPCION"),
                rs.getBoolean("TS_ACTIVA")
        );
        return new Sala(
                rs.getInt("idSALA"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getString("UBICACION"),
                rs.getInt("CAPACIDAD"),
                rs.getBoolean("ACTIVA"),
                tipoSala
        );
    }
}
