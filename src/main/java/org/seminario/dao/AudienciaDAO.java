package org.seminario.dao;

import org.seminario.model.Audiencia;

import java.util.List;

public class AudienciaDAO {

    public boolean guardar(Audiencia audiencia) {
        return false;
    }

    public boolean modificar(Audiencia audiencia) {
        return false;
    }

    public boolean cancelar(
            int idAudiencia) {
        return false;
    }

    public boolean suspender(
            int idAudiencia) {
        return false;
    }

    public boolean confirmar(
            int idAudiencia) {
        return false;
    }

    public Audiencia buscarPorId(
            int idAudiencia) {
        return null;
    }

    public List<Audiencia> listarTodos() {
        return null;
    }

    public List<Audiencia> buscarPorEstado(
            String estado) {
        return null;
    }

    public List<Audiencia> buscarPorCuij(
            String cuij) {
        return null;
    }
}