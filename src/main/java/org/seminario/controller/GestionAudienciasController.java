package org.seminario.controller;

import javafx.collections.FXCollections;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.seminario.model.Audiencia;
import org.seminario.model.EstadoAudiencia;
import org.seminario.model.Participante;
import org.seminario.model.Sala;
import org.seminario.service.PlanificacionAudienciaService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestionAudienciasController {

    private final PlanificacionAudienciaService service =
            new PlanificacionAudienciaService();
    private Audiencia audienciaSeleccionada;
    private Set<LocalDate> fechasConSalas = Set.of();
    private final Set<Integer> participantesSeleccionados = new HashSet<>();
    private final List<Audiencia> programadasDuranteSesion =
            new ArrayList<>();

    @FXML private TableView<Audiencia> tvAudiencias;
    @FXML private TableColumn<Audiencia, Long> colNroAudiencia;
    @FXML private TableColumn<Audiencia, Long> colIdSolicitud;
    @FXML private TableColumn<Audiencia, Long> colTipoAudiencia;
    @FXML private TableColumn<Audiencia, String> colEstado;
    @FXML private TableColumn<Audiencia, Integer> colDuracion;
    @FXML private Label lblAudienciaSeleccionada;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Sala> cbSala;
    @FXML private ComboBox<LocalTime> cbHora;
    @FXML private Button btnProgramar;
    @FXML private TableView<Participante> tvParticipantes;
    @FXML private TableColumn<Participante, String> colTipoParticipante;
    @FXML private TableColumn<Participante, String> colNombreParticipante;
    @FXML private TableColumn<Participante, String> colDocumentoParticipante;
    @FXML private TableColumn<Participante, Participante> colSeleccionar;

    @FXML
    public void initialize() {
        colNroAudiencia.setCellValueFactory(
                new PropertyValueFactory<>("nroAudiencia"));
        colIdSolicitud.setCellValueFactory(
                new PropertyValueFactory<>("idSolicitudAgendamiento"));
        colTipoAudiencia.setCellValueFactory(
                new PropertyValueFactory<>("idTipoAudiencia"));
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));
        colDuracion.setCellValueFactory(
                new PropertyValueFactory<>("duracionEstimada"));
        colTipoParticipante.setCellValueFactory(
                new PropertyValueFactory<>("tipoParticipante"));
        colNombreParticipante.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto"));
        colDocumentoParticipante.setCellValueFactory(
                new PropertyValueFactory<>("dni"));
        colSeleccionar.setCellValueFactory(
                cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        colSeleccionar.setCellFactory(column -> new CheckBoxTableCell());
        tvParticipantes.setRowFactory(table -> new TableRow<>() {
            @Override
            protected void updateItem(Participante participante, boolean empty) {
                super.updateItem(participante, empty);
                if (empty || participante == null) {
                    setStyle("");
                } else if (participantesSeleccionados.contains(
                        participante.getIdParticipante())) {
                    setStyle("-fx-background-color: #b7e4c7;"
                            + "-fx-text-fill: #1b4332;");
                } else {
                    setStyle("");
                }
            }
        });
        tvAudiencias.setRowFactory(table -> new TableRow<>() {
            @Override
            protected void updateItem(Audiencia audiencia, boolean empty) {
                super.updateItem(audiencia, empty);
                if (empty || audiencia == null) {
                    setStyle("");
                } else if (audiencia.getEstado()
                        == EstadoAudiencia.PROGRAMADA) {
                    setStyle("-fx-background-color: #b7e4c7;"
                            + "-fx-text-fill: #1b4332;");
                } else {
                    setStyle("");
                }
            }
        });

        tvAudiencias.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, actual) ->
                        seleccionarAudiencia(actual));
        cbHora.valueProperty().addListener(
                (observable, anterior, actual) ->
                        actualizarEstadoProgramar());
        configurarColoresFechas();
        refrescarTabla();
    }

    private void seleccionarAudiencia(Audiencia audiencia) {
        audienciaSeleccionada = audiencia;
        limpiarPlanificacion();
        if (audiencia == null) {
            lblAudienciaSeleccionada.setText(
                    "No hay audiencia seleccionada.");
            return;
        }
        lblAudienciaSeleccionada.setText(
                "Audiencia " + audiencia.getNroAudiencia()
                        + " - Duración: "
                        + audiencia.getDuracionEstimada() + " minutos");
        if (audiencia.getEstado() != EstadoAudiencia.PENDIENTE_PLANIFICACION) {
            return;
        }
        try {
            fechasConSalas = service.listarFechasConSalas(audiencia);
            System.out.println(
                    "[CU02][Controller] Calendario actualizado para audiencia "
                            + audiencia.getNroAudiencia()
                            + ": " + fechasConSalas);
            configurarColoresFechas();
            tvParticipantes.getItems().clear();
        } catch (SQLException e) {
            System.err.println(
                    "[CU02][Controller] Error cargando disponibilidad "
                            + "para audiencia "
                            + audiencia.getNroAudiencia());
            e.printStackTrace();
            mostrarErrorBaseDatos("No fue posible cargar los participantes.");
        }
    }

    private void configurarColoresFechas() {
        dpFecha.setDayCellFactory(datePicker ->
                new DateCell() {
                    @Override
                    public void updateItem(
                            LocalDate fecha,
                            boolean empty) {
                        super.updateItem(fecha, empty);
                        if (empty || fecha == null) {
                            setStyle("");
                        } else if (fechasConSalas.contains(fecha)) {
                            setStyle(
                                    "-fx-background-color: #9ecbff;"
                                            + "-fx-text-fill: #003366;"
                            );
                        } else {
                            setStyle(
                                    "-fx-background-color: #ffc7c7;"
                                            + "-fx-text-fill: #660000;"
                            );
                        }
                    }
                });
    }

    @FXML
    private void cargarSalas() {
        if (audienciaSeleccionada == null || dpFecha.getValue() == null) {
            return;
        }
        limpiarOpcionesDependientes();
        try {
            participantesSeleccionados.clear();
            tvParticipantes.setItems(FXCollections.observableArrayList(
                    service.listarParticipantesDisponibles(dpFecha.getValue())));
            List<Sala> salas = service.listarSalas(
                    audienciaSeleccionada,
                    dpFecha.getValue());
            if (salas.isEmpty()) {
                mostrarMensaje(Alert.AlertType.INFORMATION,
                        "Sin salas disponibles",
                        "No hay salas disponibles para la fecha seleccionada.");
                return;
            }
            cbSala.setItems(FXCollections.observableArrayList(salas));
            cbSala.setDisable(false);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Datos inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No fue posible consultar las salas.");
        }
    }

    @FXML
    private void cargarHorarios() {
        Sala sala = cbSala.getValue();
        if (sala == null) {
            return;
        }
        List<Participante> participantes =
                participantesSeleccionados();
        cbHora.getItems().clear();
        cbHora.setDisable(true);
        btnProgramar.setDisable(true);
        if (!tieneRolesObligatorios(participantes)) {
            return;
        }
        try {
            List<LocalTime> horarios = service.listarHorarios(
                    audienciaSeleccionada,
                    sala,
                    dpFecha.getValue(),
                    participantes);
            if (horarios.isEmpty()) {
                mostrarMensaje(Alert.AlertType.INFORMATION,
                        "Sin horarios disponibles",
                        "No existe un horario compatible con la sala y los participantes.");
                return;
            }
            cbHora.setItems(FXCollections.observableArrayList(horarios));
            cbHora.setDisable(false);
        } catch (IllegalArgumentException e) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Participantes incompletos", e.getMessage());
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No fue posible consultar los horarios.");
        }
    }

    @FXML
    private void programarAudiencia() {
        try {
            boolean programada = service.programar(
                    audienciaSeleccionada,
                    dpFecha.getValue(),
                    cbSala.getValue(),
                    cbHora.getValue(),
                    participantesSeleccionados());
            if (!programada) {
                mostrarMensaje(Alert.AlertType.ERROR,
                        "No se pudo programar",
                        "La audiencia no fue modificada.");
                return;
            }
            audienciaSeleccionada.setIdSala(cbSala.getValue().getIdSala());
            audienciaSeleccionada.setFechaProgramacion(dpFecha.getValue());
            audienciaSeleccionada.setHoraInicio(cbHora.getValue());
            audienciaSeleccionada.setEstado(EstadoAudiencia.PROGRAMADA);
            programadasDuranteSesion.add(audienciaSeleccionada);
            tvAudiencias.refresh();
            mostrarMensaje(Alert.AlertType.INFORMATION,
                    "Audiencia programada",
                    "La audiencia fue programada correctamente.");
            limpiarPlanificacion();
            refrescarTabla();
        } catch (IllegalArgumentException e) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Datos inválidos", e.getMessage());
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No fue posible guardar la programación.");
        }
    }

    private void refrescarTabla() {
        try {
            List<Audiencia> audienciasPendientes = service.listarPendientes();
            List<Audiencia> audiencias = new ArrayList<>(
                    programadasDuranteSesion);
            audiencias.addAll(audienciasPendientes);
            tvAudiencias.setItems(FXCollections.observableArrayList(audiencias));
            if (audiencias.isEmpty()) {
                lblAudienciaSeleccionada.setText(
                        "No hay audiencias pendientes de planificación.");
            }
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No fue posible cargar las audiencias.");
        }
    }

    @FXML
    private void refrescar() {
        refrescarTabla();
    }

    private void limpiarOpcionesDependientes() {
        cbSala.getItems().clear();
        cbHora.getItems().clear();
        cbSala.setDisable(true);
        cbHora.setDisable(true);
        btnProgramar.setDisable(true);
    }

    private void limpiarPlanificacion() {
        dpFecha.setValue(null);
        limpiarOpcionesDependientes();
        participantesSeleccionados.clear();
        tvParticipantes.getItems().clear();
        tvParticipantes.refresh();
    }

    private void actualizarEstadoProgramar() {
        btnProgramar.setDisable(
                audienciaSeleccionada == null
                        || dpFecha.getValue() == null
                        || cbSala.getValue() == null
                        || cbHora.getValue() == null
                        || participantesSeleccionados().isEmpty());
    }

    private List<Participante> participantesSeleccionados() {
        List<Participante> seleccionados = new ArrayList<>();
        for (Participante participante : tvParticipantes.getItems()) {
            if (participantesSeleccionados.contains(
                            participante.getIdParticipante())) {
                        seleccionados.add(participante);
            }
        }
        return seleccionados;
    }

    private boolean tieneRolesObligatorios(
            List<Participante> participantes) {
        Set<String> tipos = new HashSet<>();
        for (Participante participante : participantes) {
            tipos.add(participante.getTipoParticipante().toUpperCase());
        }
        return tipos.contains("JUEZ")
                && tipos.contains("FISCAL")
                && tipos.contains("DEFENSOR")
                && tipos.contains("IMPUTADO");
    }

    private final class CheckBoxTableCell
            extends TableCell<Participante, Participante> {
        private final CheckBox checkBox = new CheckBox();

        private CheckBoxTableCell() {
            checkBox.setOnAction(event -> {
                        Participante participante = getTableRow().getItem();
                        if (participante == null) {
                            return;
                        }
                        int id = participante.getIdParticipante();
                        if (checkBox.isSelected()) {
                            participantesSeleccionados.add(id);
                        } else {
                            participantesSeleccionados.remove(id);
                        }
                        tvParticipantes.refresh();
                        actualizarEstadoProgramar();
                        if (cbSala.getValue() != null) {
                            cargarHorarios();
                        }
            });
        }

        @Override
        protected void updateItem(
                        Participante participante,
                        boolean empty) {
            super.updateItem(participante, empty);
            if (empty || participante == null) {
                        setGraphic(null);
            } else {
                        checkBox.setSelected(participantesSeleccionados.contains(
                                participante.getIdParticipante()));
                        setGraphic(checkBox);
            }
        }
    }

    private void mostrarErrorBaseDatos(String mensaje) {
        mostrarMensaje(Alert.AlertType.ERROR,
                "Error de base de datos",
                mensaje + "\nLa audiencia no fue modificada.");
    }

    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volverMenuPrincipal() {
        cerrarVentana();
    }

    @FXML
    private void cerrarVentana() {
        if (DashBoardController.dashboardStage != null) {
            DashBoardController.dashboardStage.show();
        }
        Stage ventanaActual =
                (Stage) tvAudiencias.getScene().getWindow();
        ventanaActual.close();
    }
}
