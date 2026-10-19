package org.seminario.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Audiencia {

    private int idAudiencia;
    private String CUIJ;
    private String caratula;
    private TipoAudiencia tipoAudiencia;

    private LocalDate fecha;
    private LocalTime horaInicio;

    private Integer duracionMinutos;

    private String estado;

    private Sala sala;

    private Juez juez;
    private Fiscal fiscal;
    private Defensor defensor;
    private Imputado imputado;

    private String observaciones;

    public Audiencia(
            int idAudiencia,
            String CUIJ,
            String caratula,
            TipoAudiencia tipoAudiencia,
            LocalDate fecha,
            LocalTime horaInicio,
            Integer duracionMinutos,
            String estado,
            Sala sala,
            Juez juez,
            Fiscal fiscal,
            Defensor defensor,
            Imputado imputado,
            String observaciones) {

        this.idAudiencia = idAudiencia;
        this.CUIJ = CUIJ;
        this.caratula = caratula;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
        this.sala = sala;
        this.juez = juez;
        this.fiscal = fiscal;
        this.defensor = defensor;
        this.imputado = imputado;
        this.observaciones = observaciones;
    }

    public int getIdAudiencia() {
        return idAudiencia;
    }

    public void setIdAudiencia(int idAudiencia) {
        this.idAudiencia = idAudiencia;
    }

    public String getCUIJ() {
        return CUIJ;
    }

    public void setCUIJ(String numeroExpediente) {
        this.CUIJ = numeroExpediente;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Juez getJuez() {
        return juez;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public Fiscal getFiscal() {
        return fiscal;
    }

    public void setFiscal(Fiscal fiscal) {
        this.fiscal = fiscal;
    }

    public Defensor getDefensor() {
        return defensor;
    }

    public void setDefensor(Defensor defensor) {
        this.defensor = defensor;
    }

    public Imputado getImputado() {
        return imputado;
    }

    public void setImputado(Imputado imputado) {
        this.imputado = imputado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
