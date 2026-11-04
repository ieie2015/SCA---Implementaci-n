package org.seminario.service;

import org.seminario.dao.AudienciaDAO;
import org.seminario.dao.AudienciaParticipanteDAO;
import org.seminario.dao.DisponibilidadParticipanteDAO;
import org.seminario.dao.DisponibilidadSalaDAO;
import org.seminario.dao.ParticipanteDAO;
import org.seminario.dao.SalaDAO;
import org.seminario.model.Audiencia;
import org.seminario.model.DisponibilidadSala;
import org.seminario.model.Participante;
import org.seminario.model.Sala;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanificacionAudienciaService {

    private final AudienciaDAO audienciaDAO = new AudienciaDAO();
    private final SalaDAO salaDAO = new SalaDAO();
    private final DisponibilidadSalaDAO disponibilidadSalaDAO =
            new DisponibilidadSalaDAO();
    private final DisponibilidadParticipanteDAO disponibilidadParticipanteDAO =
            new DisponibilidadParticipanteDAO();
    private final ParticipanteDAO participanteDAO = new ParticipanteDAO();
    private final AudienciaParticipanteDAO audienciaParticipanteDAO =
            new AudienciaParticipanteDAO();

    public List<Audiencia> listarPendientes() throws SQLException {
        return audienciaDAO.listarPendientesPlanificacionSql();
    }

    public List<Participante> listarParticipantes() throws SQLException {
        return participanteDAO.listarTodos();
    }

    public List<Sala> listarSalas(
            Audiencia audiencia,
            LocalDate fecha) throws SQLException {
        validarFecha(audiencia, fecha);
        return salaDAO.listarDisponibles(
                audiencia.getIdTipoAudiencia(),
                fecha
        );
    }

    public Set<LocalDate> listarFechasConSalas(
            Audiencia audiencia) throws SQLException {
        if (audiencia == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una audiencia."
            );
        }
        Set<LocalDate> fechas = salaDAO.listarFechasDisponibles(
                audiencia.getIdTipoAudiencia()
        );
        System.out.println(
                "[CU02][Service] Audiencia "
                        + audiencia.getNroAudiencia()
                        + ", idTipoAudiencia="
                        + audiencia.getIdTipoAudiencia()
                        + ", fechasConSalas=" + fechas);
        return fechas;
    }

    public List<LocalTime> listarHorarios(
            Audiencia audiencia,
            Sala sala,
            LocalDate fecha,
            List<Participante> participantes) throws SQLException {
        validarFecha(audiencia, fecha);
        if (sala == null) {
            throw new IllegalArgumentException("La sala es obligatoria.");
        }
        validarParticipantesObligatorios(participantes);

        List<DisponibilidadSala> disponibilidades =
                disponibilidadSalaDAO.buscarPorFechaYSalas(
                        fecha,
                        sala.getIdSala()
                );
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime horaFin;

        for (DisponibilidadSala disponibilidad : disponibilidades) {
            LocalTime inicio = disponibilidad.getHoraDesde();
            while (!inicio.plusMinutes(audiencia.getDuracionEstimada())
                    .isAfter(disponibilidad.getHoraHasta())) {
                horaFin = inicio.plusMinutes(
                        audiencia.getDuracionEstimada()
                );
                if (participantesDisponibles(
                        participantes,
                        fecha,
                        inicio,
                        horaFin
                ) && !audienciaDAO.existeConflictoSala(
                        audiencia.getIdAudiencia(),
                        sala.getIdSala(),
                        fecha,
                        inicio,
                        horaFin
                )) {
                    horarios.add(inicio);
                }
                inicio = inicio.plusMinutes(30);
            }
        }
        return horarios.stream().distinct().toList();
    }

    public boolean programar(
            Audiencia audiencia,
            LocalDate fecha,
            Sala sala,
            LocalTime hora,
            List<Participante> participantes) throws SQLException {
        if (listarHorarios(
                audiencia, sala, fecha, participantes
        ).stream().noneMatch(hora::equals)) {
            throw new IllegalArgumentException(
                    "El horario seleccionado ya no está disponible."
            );
        }

        if (!audienciaDAO.programar(
                audiencia.getIdAudiencia(),
                sala.getIdSala(),
                fecha,
                hora
        )) {
            return false;
        }
        return audienciaParticipanteDAO.guardarTodos(
                audiencia.getIdAudiencia(),
                participantes
        );
    }

    private boolean participantesDisponibles(
            List<Participante> participantes,
            LocalDate fecha,
            LocalTime inicio,
            LocalTime fin) throws SQLException {
        for (Participante participante : participantes) {
            if (!disponibilidadParticipanteDAO.estaDisponible(
                    participante.getIdParticipante(),
                    fecha,
                    inicio,
                    fin
            )) {
                return false;
            }
        }
        return true;
    }

    private void validarParticipantesObligatorios(
            List<Participante> participantes) {
        if (participantes == null || participantes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un participante."
            );
        }

        Set<String> tiposSeleccionados = participantes.stream()
                .map(Participante::getTipoParticipante)
                .map(String::toUpperCase)
                .collect(java.util.stream.Collectors.toSet());
        List<String> tiposFaltantes = List.of(
                "JUEZ", "FISCAL", "DEFENSOR", "IMPUTADO"
        ).stream()
                .filter(tipo -> !tiposSeleccionados.contains(tipo))
                .toList();
        if (!tiposFaltantes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe seleccionar al menos un JUEZ, FISCAL, DEFENSOR "
                            + "e IMPUTADO. Faltan: "
                            + String.join(", ", tiposFaltantes) + "."
            );
        }
    }

    private void validarFecha(
            Audiencia audiencia,
            LocalDate fecha) {
        if (audiencia == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una audiencia."
            );
        }
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una fecha actual o futura."
            );
        }
    }
}
