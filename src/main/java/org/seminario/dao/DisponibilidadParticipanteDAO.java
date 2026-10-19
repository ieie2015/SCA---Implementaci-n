package org.seminario.dao;

import org.seminario.model.DisponibilidadParticipante;

import java.time.LocalDate;
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
            LocalDate fecha) {
        return null;
    }

    public List<DisponibilidadParticipante>
    buscarPorParticipante(
            int idParticipante) {
        return null;
    }
}