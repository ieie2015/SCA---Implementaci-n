package org.seminario.dao;

import org.seminario.model.DisponibilidadSala;

import java.time.LocalDate;
import java.util.List;

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
            LocalDate fecha) {
        return null;
    }

    public List<DisponibilidadSala> buscarPorSala(
            int idSala) {
        return null;
    }
}