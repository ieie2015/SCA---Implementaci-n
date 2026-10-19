package org.seminario.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SolicitudAgendamiento {

    private int idSolicitudAgendamiento;

    private long idtipoAudiencia;

    private Long nroSolicitud;

    private Long cuij;

    private String caratula;

    private EstadoSolicitud estado;

    private PrioridadSolicitud prioridad;

    private String observaciones;

    public SolicitudAgendamiento(
            int idSolicitudAgendamiento,
            long idtipoAudiencia,
            Long nroSolicitud,
            Long cuij,
            String caratula,
            EstadoSolicitud estado,
            PrioridadSolicitud prioridad,
            String observaciones) {

        this.idSolicitudAgendamiento = idSolicitudAgendamiento;
        this.idtipoAudiencia = idtipoAudiencia;
        this.nroSolicitud = nroSolicitud;
        this.cuij = cuij;
        this.caratula = caratula;
        this.estado = estado;
        this.prioridad = prioridad;
        this.observaciones = observaciones;
    }

    public int getIdSolicitudAgendamiento() {
        return idSolicitudAgendamiento;
    }

    public void setIdSolicitudAgendamiento(int idSolicitudAgendamiento) {
        this.idSolicitudAgendamiento = idSolicitudAgendamiento;
    }

    public long getIdtipoAudiencia() {
        return idtipoAudiencia;
    }

    public void setIdtipoAudiencia(long idtipoAudiencia) {
        this.idtipoAudiencia = idtipoAudiencia;
    }

    public Long getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(long nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public Long getCuij() {
        return cuij;
    }

    public void setCuij(long cuij) {
        this.cuij = cuij;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public PrioridadSolicitud getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadSolicitud prioridad) {
        this.prioridad = prioridad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return nroSolicitud + " - " + caratula;
    }
    private SolicitudAgendamiento mapear(ResultSet rs)
            throws SQLException {

        TipoSala tipoSala =
                new TipoSala(
                        rs.getInt("idTIPO_SALA"),
                        rs.getString("TS_CODIGO"),
                        rs.getString("TS_DESCRIPCION"),
                        rs.getBoolean("TS_ACTIVA")
                );

        TipoAudiencia tipoAudiencia =
                new TipoAudiencia(
                        rs.getInt("idTIPO_AUDIENCIA"),
                        tipoSala,
                        rs.getString("TA_CODIGO"),
                        rs.getString("TA_DESCRIPCION"),
                        rs.getInt("DURACION_REFERENCIA"),
                        rs.getBoolean("TA_ACTIVA")
                );

        SolicitudAgendamiento solicitud =
                new SolicitudAgendamiento(
                        rs.getInt("idSOLICITUD_AGENDAMIENTO"),
                        rs.getLong("idTipoAudiencia"),
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

        return solicitud;
    }
}