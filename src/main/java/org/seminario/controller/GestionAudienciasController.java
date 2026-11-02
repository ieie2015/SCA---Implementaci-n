package org.seminario.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.seminario.model.Audiencia;
import org.seminario.model.Participante;
import org.seminario.model.Sala;
import org.seminario.service.PlanificacionAudienciaService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GestionAudienciasController {

    private final PlanificacionAudienciaService service =
            new PlanificacionAudienciaService();
    private Audiencia audienciaSeleccionada;

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

        tvAudiencias.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, actual) ->
                        seleccionarAudiencia(actual));
        tvParticipantes.getSelectionModel().setSelectionMode(
                javafx.scene.control.SelectionMode.MULTIPLE);
        tvParticipantes.getSelectionModel().getSelectedItems()
                .addListener((javafx.collections.ListChangeListener<Participante>)
                        change -> {
                            actualizarEstadoProgramar();
                            if (cbSala.getValue() != null) {
                                cargarHorarios();
                            }
                        });
        cbHora.valueProperty().addListener(
                (observable, anterior, actual) ->
                        actualizarEstadoProgramar());
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
        try {
            tvParticipantes.setItems(FXCollections.observableArrayList(
                    service.listarParticipantes()));
        } catch (SQLException e) {
            mostrarErrorBaseDatos("No fue posible cargar los participantes.");
        }
    }

    @FXML
    private void cargarSalas() {
        if (audienciaSeleccionada == null || dpFecha.getValue() == null) {
            return;
        }
        limpiarOpcionesDependientes();
        try {
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
        cbHora.getItems().clear();
        cbHora.setDisable(true);
        btnProgramar.setDisable(true);
        try {
            List<LocalTime> horarios = service.listarHorarios(
                    audienciaSeleccionada,
                    sala,
                    dpFecha.getValue(),
                    tvParticipantes.getSelectionModel().getSelectedItems());
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
                    "Datos incompletos", e.getMessage());
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
                    tvParticipantes.getSelectionModel().getSelectedItems());
            if (!programada) {
                mostrarMensaje(Alert.AlertType.ERROR,
                        "No se pudo programar",
                        "La audiencia no fue modificada.");
                return;
            }
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
            List<Audiencia> audiencias = service.listarPendientes();
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
        tvParticipantes.getSelectionModel().clearSelection();
    }

    private void actualizarEstadoProgramar() {
        btnProgramar.setDisable(
                audienciaSeleccionada == null
                        || dpFecha.getValue() == null
                        || cbSala.getValue() == null
                        || cbHora.getValue() == null
                        || tvParticipantes.getSelectionModel()
                        .getSelectedItems().isEmpty());
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
