package org.seminario.service;

import org.seminario.dao.SolicitudAgendamientoDAO;
import org.seminario.model.EstadoSolicitud;
import org.seminario.model.PrioridadSolicitud;
import org.seminario.model.SolicitudAgendamiento;

import java.util.List;

public class SolicitudAgendamientoService {

    private final SolicitudAgendamientoDAO dao =
            new SolicitudAgendamientoDAO();

    public List<SolicitudAgendamiento>
    buscarSolicitudes(
            EstadoSolicitud estado,
            PrioridadSolicitud prioridad,
            Long cuij,
            Long nroSolicitud) {

        return dao.buscarConFiltros(
                estado,
                prioridad,
                cuij,
                nroSolicitud
        );
    }

    public boolean procesarSolicitud(
            long idSolicitud) {

        return dao.actualizarEstado(
                idSolicitud,
                EstadoSolicitud.PROCESADA
        );
    }

    public boolean rechazarSolicitud(
            long idSolicitud) {

        return dao.actualizarEstado(
                idSolicitud,
                EstadoSolicitud.RECHAZADA
        );
    }
    public boolean observarSolicitud(
            long idSolicitud) {

        return dao.actualizarEstado(
                idSolicitud,
                EstadoSolicitud.OBSERVADA
        );
    }

    public boolean actualizarObservaciones(
            long idSolicitud,
            String observaciones) {

        return dao.actualizarObservaciones(
                idSolicitud,
                observaciones
        );
    }
}