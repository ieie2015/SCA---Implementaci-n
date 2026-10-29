package org.seminario.service;

import org.seminario.dao.AudienciaDAO;
import org.seminario.model.Audiencia;

import java.util.List;

public class AudienciaService {

    private final AudienciaDAO dao = new AudienciaDAO();

    public List<Audiencia> listarPendientesPlanificacion() {
        return dao.listarPendientesPlanificacion();
    }
}
