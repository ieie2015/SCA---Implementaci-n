package org.seminario.dao;

import org.seminario.model.Audiencia;
import org.seminario.model.EstadoAudiencia;
import org.seminario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AudienciaDAO {

    private static final String SELECT_BASE =
            """
            SELECT a.idAUDIENCIA, a.idSOLICITUD_AGENDAMIENTO,
                   a.idTIPO_AUDIENCIA, a.idESTADO_AUDIENCIA,
                   a.idSALA, a.NRO_AUDIENCIA, a.FECHA_CREACION,
                   a.FECHA_PROGRAMACION, a.HORA_INICIO,
                   a.DURACION_ESTIMADA, a.OBSERVACIONES,
                   ea.CODIGO AS ESTADO_CODIGO
            FROM AUDIENCIA a
            JOIN ESTADO_AUDIENCIA ea
              ON ea.idESTADO_AUDIENCIA = a.idESTADO_AUDIENCIA
            """;

    public boolean guardar(Audiencia audiencia) {
        String sql =
                """
                INSERT INTO AUDIENCIA (
                    idSOLICITUD_AGENDAMIENTO, idTIPO_AUDIENCIA,
                    idESTADO_AUDIENCIA, idSALA, NRO_AUDIENCIA,
                    FECHA_CREACION, FECHA_PROGRAMACION, HORA_INICIO,
                    DURACION_ESTIMADA, OBSERVACIONES
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, audiencia.getIdSolicitudAgendamiento());
            ps.setLong(2, audiencia.getIdTipoAudiencia());
            ps.setLong(3, audiencia.getIdEstadoAudiencia());

            if (audiencia.getIdSala() == null) {
                ps.setNull(4, Types.BIGINT);
            } else {
                ps.setInt(4, audiencia.getIdSala());
            }

            ps.setLong(5, audiencia.getNroAudiencia());
            ps.setObject(6, audiencia.getFechaCreacion());
            ps.setObject(7, audiencia.getFechaProgramacion());
            ps.setObject(8, audiencia.getHoraInicio());
            ps.setInt(9, audiencia.getDuracionEstimada());
            ps.setString(10, audiencia.getObservaciones());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Crea la audiencia inicial a partir de una solicitud aceptada por CU01.
     * El estado y el tipo se resuelven desde sus tablas, sin asumir IDs fijos.
     */
    public boolean crearDesdeSolicitud(long idSolicitud) {
        String sql =
                """
                INSERT INTO AUDIENCIA (
                    idSOLICITUD_AGENDAMIENTO, idTIPO_AUDIENCIA,
                    idESTADO_AUDIENCIA, NRO_AUDIENCIA, FECHA_CREACION,
                    DURACION_ESTIMADA
                )
                SELECT s.idSOLICITUD_AGENDAMIENTO,
                       s.idTIPO_AUDIENCIA,
                       (SELECT idESTADO_AUDIENCIA
                          FROM ESTADO_AUDIENCIA
                         WHERE CODIGO = 'PENDIENTE_PLANIFICACION'
                         LIMIT 1),
                       COALESCE((SELECT MAX(NRO_AUDIENCIA) + 1
                                   FROM AUDIENCIA), 1),
                       NOW(),
                       ta.DURACION_REFERENCIA
                  FROM SOLICITUD_AGENDAMIENTO s
                  JOIN TIPO_AUDIENCIA ta
                    ON ta.idTIPO_AUDIENCIA = s.idTIPO_AUDIENCIA
                 WHERE s.idSOLICITUD_AGENDAMIENTO = ?
                   AND s.ESTADO = 'PENDIENTE'
                   AND NOT EXISTS (
                       SELECT 1
                         FROM AUDIENCIA a
                        WHERE a.idSOLICITUD_AGENDAMIENTO =
                              s.idSOLICITUD_AGENDAMIENTO
                   )
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idSolicitud);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tieneAudienciaAsociada(long idSolicitud) {
        String sql =
                """
                SELECT 1
                  FROM AUDIENCIA
                 WHERE idSOLICITUD_AGENDAMIENTO = ?
                 LIMIT 1
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modificar(Audiencia audiencia) {
        String sql =
                """
                UPDATE AUDIENCIA
                   SET idSALA = ?, FECHA_PROGRAMACION = ?,
                       HORA_INICIO = ?, DURACION_ESTIMADA = ?,
                       OBSERVACIONES = ?
                 WHERE idAUDIENCIA = ?
                """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (audiencia.getIdSala() == null) {
                ps.setNull(1, Types.BIGINT);
            } else {
                ps.setInt(1, audiencia.getIdSala());
            }
            ps.setObject(2, audiencia.getFechaProgramacion());
            ps.setObject(3, audiencia.getHoraInicio());
            ps.setInt(4, audiencia.getDuracionEstimada());
            ps.setString(5, audiencia.getObservaciones());
            ps.setLong(6, audiencia.getIdAudiencia());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int idAudiencia) {
        return actualizarEstado(idAudiencia, EstadoAudiencia.CANCELADA);
    }

    public boolean suspender(int idAudiencia) {
        return actualizarEstado(idAudiencia, EstadoAudiencia.SUSPENDIDA);
    }

    public boolean confirmar(int idAudiencia) {
        return actualizarEstado(idAudiencia, EstadoAudiencia.CONFIRMADA);
    }

    public Audiencia buscarPorId(int idAudiencia) {
        String sql = SELECT_BASE + " WHERE a.idAUDIENCIA = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAudiencia);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Audiencia> listarTodos() {
        return consultar(SELECT_BASE + " ORDER BY a.FECHA_CREACION DESC");
    }

    public List<Audiencia> listarPendientesPlanificacion() {
        return buscarPorEstado(
                EstadoAudiencia.PENDIENTE_PLANIFICACION.name()
        );
    }

    public List<Audiencia> listarPendientesPlanificacionSql()
            throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ea.CODIGO = ? ORDER BY a.NRO_AUDIENCIA";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, EstadoAudiencia.PENDIENTE_PLANIFICACION.name());
            return listar(ps);
        }
    }

    public boolean existeConflictoSala(
            long idAudiencia,
            int idSala,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) throws SQLException {
        String sql = """
                SELECT 1
                  FROM AUDIENCIA a
                  JOIN ESTADO_AUDIENCIA ea
                    ON ea.idESTADO_AUDIENCIA = a.idESTADO_AUDIENCIA
                 WHERE a.idSALA = ?
                   AND a.FECHA_PROGRAMACION = ?
                   AND a.idAUDIENCIA <> ?
                   AND ea.CODIGO NOT IN ('CANCELADA', 'SUSPENDIDA')
                   AND a.HORA_INICIO < ?
                   AND ADDTIME(
                           a.HORA_INICIO,
                           SEC_TO_TIME(a.DURACION_ESTIMADA * 60)
                       ) > ?
                 LIMIT 1
                """;
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setObject(2, fecha);
            ps.setLong(3, idAudiencia);
            ps.setObject(4, horaFin);
            ps.setObject(5, horaInicio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean programar(
            long idAudiencia,
            int idSala,
            LocalDate fecha,
            LocalTime horaInicio) throws SQLException {
        String sql = """
                UPDATE AUDIENCIA a
                  JOIN ESTADO_AUDIENCIA ep
                    ON ep.CODIGO = 'PENDIENTE_PLANIFICACION'
                  JOIN ESTADO_AUDIENCIA eg
                    ON eg.CODIGO = 'PROGRAMADA'
                   SET a.idSALA = ?,
                       a.FECHA_PROGRAMACION = ?,
                       a.HORA_INICIO = ?,
                       a.idESTADO_AUDIENCIA = eg.idESTADO_AUDIENCIA
                 WHERE a.idAUDIENCIA = ?
                   AND a.idESTADO_AUDIENCIA = ep.idESTADO_AUDIENCIA
                """;
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setObject(2, fecha);
            ps.setObject(3, horaInicio);
            ps.setLong(4, idAudiencia);
            return ps.executeUpdate() == 1;
        }
    }

    public List<Audiencia> buscarPorEstado(String estado) {
        String sql = SELECT_BASE + " WHERE ea.CODIGO = ? ORDER BY a.NRO_AUDIENCIA";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            return listar(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Audiencia> buscarPorCuij(String cuij) {
        String sql =
                SELECT_BASE
                        + " JOIN SOLICITUD_AGENDAMIENTO s"
                        + " ON s.idSOLICITUD_AGENDAMIENTO = a.idSOLICITUD_AGENDAMIENTO"
                        + " WHERE s.CUIJ = ? ORDER BY a.NRO_AUDIENCIA";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuij);
            return listar(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private boolean actualizarEstado(
            long idAudiencia,
            EstadoAudiencia estado) {
        String sql =
                """
                UPDATE AUDIENCIA a
                JOIN ESTADO_AUDIENCIA ea
                  ON ea.CODIGO = ?
                   SET a.idESTADO_AUDIENCIA = ea.idESTADO_AUDIENCIA
                 WHERE a.idAUDIENCIA = ?
                """;
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setLong(2, idAudiencia);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Audiencia> consultar(String sql) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return listar(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Audiencia> listar(PreparedStatement ps)
            throws SQLException {
        List<Audiencia> audiencias = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                audiencias.add(mapear(rs));
            }
        }
        return audiencias;
    }

    private Audiencia mapear(ResultSet rs) throws SQLException {
        LocalDateTime fechaCreacion =
                rs.getTimestamp("FECHA_CREACION").toLocalDateTime();
        LocalDate fechaProgramacion =
                rs.getDate("FECHA_PROGRAMACION") == null
                        ? null
                        : rs.getDate("FECHA_PROGRAMACION").toLocalDate();
        LocalTime horaInicio =
                rs.getTime("HORA_INICIO") == null
                        ? null
                        : rs.getTime("HORA_INICIO").toLocalTime();
        int idSala = rs.getInt("idSALA");

        return new Audiencia(
                rs.getLong("idAUDIENCIA"),
                rs.getLong("idSOLICITUD_AGENDAMIENTO"),
                rs.getLong("idTIPO_AUDIENCIA"),
                rs.getLong("idESTADO_AUDIENCIA"),
                rs.wasNull() ? null : idSala,
                rs.getLong("NRO_AUDIENCIA"),
                fechaCreacion,
                fechaProgramacion,
                horaInicio,
                rs.getInt("DURACION_ESTIMADA"),
                EstadoAudiencia.valueOf(rs.getString("ESTADO_CODIGO")),
                rs.getString("OBSERVACIONES")
        );
    }
}
