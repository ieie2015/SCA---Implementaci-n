package org.seminario.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilidadParticipante {

    private int idDisponibilidad;

    private LocalDate fecha;

    private LocalTime horaDesde;

    private LocalTime horaHasta;

    private EstadoDisponibilidad estado;

    private String observaciones;

    private Participante participante;

    public DisponibilidadParticipante(
            int idDisponibilidad,
            LocalDate fecha,
            LocalTime horaDesde,
            LocalTime horaHasta,
            EstadoDisponibilidad estado,
            String observaciones,
            Participante participante) {

        this.idDisponibilidad = idDisponibilidad;
        this.fecha = fecha;
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
        this.estado = estado;
        this.observaciones = observaciones;
        this.participante = participante;
    }

    public int getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(int idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(LocalTime horaDesde) {
        this.horaDesde = horaDesde;
    }

    public LocalTime getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(LocalTime horaHasta) {
        this.horaHasta = horaHasta;
    }

    public EstadoDisponibilidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoDisponibilidad estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }
}