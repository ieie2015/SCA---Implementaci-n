package org.seminario.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Datos persistidos en la tabla AUDIENCIA.
 *
 * Los participantes se mantienen en AUDIENCIA_PARTICIPANTE y no forman
 * parte de las columnas directas de esta entidad.
 */
public class Audiencia {

    private long idAudiencia;
    private long idSolicitudAgendamiento;
    private Long cuij;
    private String caratula;
    private long idTipoAudiencia;
    private long idEstadoAudiencia;
    private Integer idSala;
    private long nroAudiencia;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaProgramacion;
    private LocalTime horaInicio;
    private int duracionEstimada;
    private EstadoAudiencia estado;
    private String observaciones;

    public Audiencia(
            long idAudiencia,
            long idSolicitudAgendamiento,
            Long cuij,
            String caratula,
            long idTipoAudiencia,
            long idEstadoAudiencia,
            Integer idSala,
            long nroAudiencia,
            LocalDateTime fechaCreacion,
            LocalDate fechaProgramacion,
            LocalTime horaInicio,
            int duracionEstimada,
            EstadoAudiencia estado,
            String observaciones) {
        this.idAudiencia = idAudiencia;
        this.idSolicitudAgendamiento = idSolicitudAgendamiento;
        this.cuij = cuij;
        this.caratula = caratula;
        this.idTipoAudiencia = idTipoAudiencia;
        this.idEstadoAudiencia = idEstadoAudiencia;
        this.idSala = idSala;
        this.nroAudiencia = nroAudiencia;
        this.fechaCreacion = fechaCreacion;
        this.fechaProgramacion = fechaProgramacion;
        this.horaInicio = horaInicio;
        this.duracionEstimada = duracionEstimada;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public long getIdAudiencia() {
        return idAudiencia;
    }

    public void setIdAudiencia(long idAudiencia) {
        this.idAudiencia = idAudiencia;
    }

    public long getIdSolicitudAgendamiento() {
        return idSolicitudAgendamiento;
    }

    public void setIdSolicitudAgendamiento(long idSolicitudAgendamiento) {
        this.idSolicitudAgendamiento = idSolicitudAgendamiento;
    }

    public Long getCuij() {
        return cuij;
    }

    public void setCuij(Long cuij) {
        this.cuij = cuij;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public long getIdTipoAudiencia() {
        return idTipoAudiencia;
    }

    public void setIdTipoAudiencia(long idTipoAudiencia) {
        this.idTipoAudiencia = idTipoAudiencia;
    }

    public long getIdEstadoAudiencia() {
        return idEstadoAudiencia;
    }

    public void setIdEstadoAudiencia(long idEstadoAudiencia) {
        this.idEstadoAudiencia = idEstadoAudiencia;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
        this.idSala = idSala;
    }

    public long getNroAudiencia() {
        return nroAudiencia;
    }

    public void setNroAudiencia(long nroAudiencia) {
        this.nroAudiencia = nroAudiencia;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(LocalDate fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public EstadoAudiencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoAudiencia estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
