package org.seminario.app;

import org.seminario.dao.SolicitudAgendamientoDAO;
import org.seminario.model.*;

public class TestInsertSolicitud {

    public static void main(String[] args) {

        TipoSala tipoSala =
                new TipoSala(
                        1,
                        "PENAL",
                        "Sala Penal",
                        true
                );

        TipoAudiencia tipoAudiencia =
                new TipoAudiencia(
                        1,
                        tipoSala,
                        "JUICIO",
                        "Juicio Oral",
                        120,
                        true
                );

        SolicitudAgendamiento solicitud =
                new SolicitudAgendamiento(
                        1,1,
                        1001L,
                        20260001L,
                        "Fiscal c/ Imputado",
                        EstadoSolicitud.PENDIENTE,
                        PrioridadSolicitud.MEDIA,
                        "Registro creado desde JDBC"
                );

        /*SolicitudAgendamientoDAO dao =
                new SolicitudAgendamientoDAO();

        boolean guardado =
                dao.guardar(solicitud);

        System.out.println(
                "Guardado = " + guardado
        );*/
    }
}