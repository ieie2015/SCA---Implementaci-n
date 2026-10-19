package org.seminario.dao;

import org.seminario.model.*;

import org.seminario.util.ConexionBD;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudAgendamientoDAO {
    public List<SolicitudAgendamiento> listarPendientes() {

        List<SolicitudAgendamiento> listaPendientes = new ArrayList<SolicitudAgendamiento>();


        String sql =
                """
                SELECT *
                FROM SOLICITUD_AGENDAMIENTO WHERE ESTADO='PENDIENTE'
                """;

        try (

                Connection con =
                        ConexionBD.conectar();

                Statement st =
                        con.createStatement();

                ResultSet rs =
                        st.executeQuery(sql)

        ) {

            while (rs.next()) {

                SolicitudAgendamiento solicitud = new SolicitudAgendamiento(
                        rs.getInt("idSOLICITUD_AGENDAMIENTO"),
                        rs.getInt("idTIPO_AUDIENCIA"),
                        rs.getLong("NRO_SOLICITUD"),
                        rs.getLong("CUIJ"),
                        rs.getString("CARATULA"),
                        EstadoSolicitud.valueOf(rs.getString("ESTADO")),
                        PrioridadSolicitud.valueOf(rs.getString("PRIORIDAD")),
                        rs.getString("OBSERVACIONES")
                );

                listaPendientes.add(solicitud);
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return listaPendientes;
    }
    public List<SolicitudAgendamiento>
    buscarConFiltros(
            EstadoSolicitud estado,
            PrioridadSolicitud prioridad,
            Long cuij,
            Long nroSolicitud) {

        List<SolicitudAgendamiento> lista =
                new ArrayList<>();

        StringBuilder sql =
                new StringBuilder(
                        """
                        SELECT *
                        FROM SOLICITUD_AGENDAMIENTO
                        WHERE 1=1
                        """
                );

        List<Object> parametros =
                new ArrayList<>();

        if (estado != null) {

            sql.append(
                    " AND ESTADO = ?"
            );

            parametros.add(
                    estado.name()
            );
        }

        if (prioridad != null) {

            sql.append(
                    " AND PRIORIDAD = ?"
            );

            parametros.add(
                    prioridad.name()
            );
        }

        if (cuij != null) {

            sql.append(
                    " AND CUIJ = ?"
            );

            parametros.add(
                    cuij
            );
        }

        if (nroSolicitud != null) {

            sql.append(
                    " AND NRO_SOLICITUD = ?"
            );

            parametros.add(
                    nroSolicitud
            );
        }

        try (

                Connection con =
                        ConexionBD.conectar();

                PreparedStatement ps =
                        con.prepareStatement(
                                sql.toString()
                        )

        ) {

            for(int i = 0;
                i < parametros.size();
                i++) {

                ps.setObject(
                        i + 1,
                        parametros.get(i)
                );
            }

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                lista.add(
                        mapear(rs)
                );
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    private SolicitudAgendamiento mapear(
            ResultSet rs)
            throws SQLException {

        TipoAudiencia tipoAudiencia =
                new TipoAudiencia(
                        rs.getInt("idTIPO_AUDIENCIA"),
                        null,
                        null,
                        null,
                        0,
                        true
                );

        return new SolicitudAgendamiento(
                rs.getInt("idSOLICITUD_AGENDAMIENTO"),
                rs.getInt("idTIPO_AUDIENCIA"),
                rs.getLong("NRO_SOLICITUD"),
                rs.getLong("CUIJ"),
                rs.getString("CARATULA"),
                EstadoSolicitud.valueOf(
                        rs.getString("ESTADO")
                ),
                PrioridadSolicitud.valueOf(
                        rs.getString("PRIORIDAD")
                ),
                rs.getString("OBSERVACIONES")
        );
    }

    public boolean actualizarEstado(
            long idSolicitudAgendamiento,
            EstadoSolicitud nuevoEstado) {

        String sql =
                """
                UPDATE SOLICITUD_AGENDAMIENTO
                SET ESTADO = ?
                WHERE idSOLICITUD_AGENDAMIENTO = ?
                """;

        try (
                Connection con=ConexionBD.conectar();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    nuevoEstado.name()
            );

            ps.setLong(
                    2,
                    idSolicitudAgendamiento
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean actualizarObservaciones(long idSolicitud, String observaciones) {
        return true;
    }
}