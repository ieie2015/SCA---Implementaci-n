package org.seminario.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.seminario.model.EstadoSolicitud;
import org.seminario.model.PrioridadSolicitud;
import org.seminario.model.SolicitudAgendamiento;
import org.seminario.service.SolicitudAgendamientoService;

public class SolicitudAgendamientoController {

    private final SolicitudAgendamientoService service =
            new SolicitudAgendamientoService();

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private ComboBox<String> cbPrioridad;

    @FXML
    private TextField txfCuij;

    @FXML
    private TextField txfCaratula;
    @FXML
    private TextField txfNroSolicitud;

    @FXML
    private TextField txfBuscarcuij;

    @FXML
    private TextField txfTipoAudiencia;

    @FXML TextField txfObservaciones;



    @FXML
    private TableView<SolicitudAgendamiento>
            tvSolicitudes;

    @FXML
    private TableColumn<SolicitudAgendamiento, Long> colNroSolicitud;

    @FXML
    private TableColumn<SolicitudAgendamiento, Long> colCuij;

    @FXML
    private TableColumn<SolicitudAgendamiento, String> colCaratula;

    @FXML
    private TableColumn<SolicitudAgendamiento, EstadoSolicitud> colEstado;

    @FXML
    private TableColumn<SolicitudAgendamiento, PrioridadSolicitud> colPrioridad;

    @FXML
    private TableColumn<SolicitudAgendamiento, String> colObservaciones;
    @FXML
    private TableColumn<SolicitudAgendamiento, String> colTipoAudiencia;
    @FXML
    private TableColumn<SolicitudAgendamiento, Boolean> colSeleccionar;
    @FXML
    Button btnAceptar;
    @FXML
    Button btnRechazar;
    @FXML
    Button btnObservar;
    @FXML
    Button btnVolver;


    @FXML
    public void initialize() {

        System.out.println(
                "[PANTALLA SOLICITUDES] Inicializando controlador."
        );
        System.out.println(
                "[PANTALLA SOLICITUDES] TableView: " + tvSolicitudes
        );
        System.out.println(
                "[PANTALLA SOLICITUDES] Columnas principales: CUIJ="
                        + colCuij
                        + ", carátula="
                        + colCaratula
        );

        colNroSolicitud.setCellValueFactory(new PropertyValueFactory<>("nroSolicitud"));

        colCuij.setCellValueFactory(new PropertyValueFactory<>("cuij"));

        colCaratula.setCellValueFactory(new PropertyValueFactory<>("caratula"));

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        colObservaciones.setCellValueFactory(
                new PropertyValueFactory<>("observaciones")
        );

        cargarCombos();

        btnAceptar.setDisable(true);
        btnRechazar.setDisable(true);
        btnObservar.setDisable(true);

        refrescarTabla();
        tvSolicitudes.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         solicitudAnterior,
                         solicitudSeleccionada) -> {

                            boolean haySolicitudSeleccionada =
                                    solicitudSeleccionada != null;
                            btnAceptar.setDisable(
                                    !haySolicitudSeleccionada
                            );
                            btnRechazar.setDisable(
                                    !haySolicitudSeleccionada
                            );
                            btnObservar.setDisable(
                                    !haySolicitudSeleccionada
                            );

                            if (solicitudSeleccionada != null) {

                                System.out.println(
                                        "[PANTALLA SOLICITUDES] Solicitud seleccionada: "
                                                + solicitudSeleccionada
                                );

                                cargarSolicitudSeleccionada(
                                        solicitudSeleccionada
                                );
                            }
                        }
                );
    }

    private void cargarCombos() {
        cbEstado.getItems().add("Todos");

        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            cbEstado.getItems().add(estado.name());
        }

        cbEstado.getSelectionModel().selectFirst();

        cbPrioridad.getItems().add("Todas");

        for (PrioridadSolicitud prioridad : PrioridadSolicitud.values()){
            cbPrioridad.getItems().add(prioridad.name());
        }

        cbPrioridad.getSelectionModel().selectFirst();

    }

    private void refrescarTabla() {
        System.out.println(
                "[PANTALLA SOLICITUDES] Cargando solicitudes en la tabla."
        );

        var lista = service.listarPendientesSinAudiencia();

        System.out.println(
                "[PANTALLA SOLICITUDES] Solicitudes recuperadas: "
                        + lista.size()
        );

        tvSolicitudes.setItems(FXCollections.observableArrayList(lista));

        System.out.println(
                "[PANTALLA SOLICITUDES] Filas cargadas en TableView: "
                        + tvSolicitudes.getItems().size()
        );

    }

    @FXML
    private void buscar() {

        EstadoSolicitud estado = null;

        if (!"Todos".equals(
                cbEstado.getValue())) {

            estado =
                    EstadoSolicitud.valueOf(
                            cbEstado.getValue()
                    );
        }

        PrioridadSolicitud prioridad =
                null;

        if (!"Todas".equals(
                cbPrioridad.getValue())) {

            prioridad =
                    PrioridadSolicitud.valueOf(
                            cbPrioridad.getValue()
                    );
        }

        Long cuij = null;

        String textoCuij = txfBuscarcuij.getText().trim();
        if (!textoCuij.isEmpty()) {
            try {
                cuij = Long.parseLong(textoCuij);
            } catch (NumberFormatException e) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "CUIJ inválido",
                        "Ingrese únicamente un número válido para el CUIJ."
                );
                return;
            }
        }

        Long nroSolicitud = null;

        String textoNroSolicitud = txfNroSolicitud.getText().trim();
        if (!textoNroSolicitud.isEmpty()) {
            try {
                nroSolicitud = Long.parseLong(textoNroSolicitud);
            } catch (NumberFormatException e) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Número de solicitud inválido",
                        "Ingrese únicamente un número válido para la solicitud."
                );
                return;
            }
        }

        if (estado == null
                && prioridad == null
                && cuij == null
                && nroSolicitud == null) {
            refrescarTabla();
            return;
        }

        System.out.println(
                "Estado = " + estado
        );

        System.out.println(
                "Prioridad = " + prioridad
        );
        tvSolicitudes.setItems(

                FXCollections
                        .observableArrayList(
                                service.buscarSolicitudes(
                                        estado,
                                        prioridad,
                                        cuij,
                                        nroSolicitud
                                )

                        )
        );
    }

    @FXML
    private void limpiarFiltros() {

    }

    @FXML
    private void procesarSolicitud() {
        System.out.println("Se presionó Aceptar");

        SolicitudAgendamiento seleccionada =
                tvSolicitudes
                        .getSelectionModel()
                        .getSelectedItem();

        System.out.println("Solicitud seleccionada: " + seleccionada);

        if (seleccionada == null) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Aceptar solicitud",
                    "Debe seleccionar una solicitud."
            );
            System.out.println("No hay una solicitud seleccionada");
            return;
        }

        boolean resultado = service.procesarSolicitud(
                seleccionada
                        .getIdSolicitudAgendamiento()
        );

        System.out.println("Resultado de procesar solicitud: " + resultado);
        refrescarTabla();

        mostrarMensaje(
                resultado
                        ? Alert.AlertType.INFORMATION
                        : Alert.AlertType.ERROR,
                resultado
                        ? "Solicitud procesada"
                        : "No se pudo procesar la solicitud",
                resultado
                        ? "Se creó la audiencia pendiente de planificación."
                        : "La solicitud no pudo convertirse en audiencia."
        );
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
    private void rechazarSolicitud() {

        SolicitudAgendamiento seleccionada =
                tvSolicitudes.getSelectionModel()
                        .getSelectedItem();

        if (seleccionada == null) {
            return;
        }

        service.rechazarSolicitud(
                seleccionada.getIdSolicitudAgendamiento()
        );

        refrescarTabla();
    }

    @FXML
    private void observarSolicitud() {

        SolicitudAgendamiento seleccionada =
                tvSolicitudes.getSelectionModel()
                        .getSelectedItem();

        if (seleccionada == null) {
            return;
        }

        String observaciones = txfObservaciones.getText().trim();
        if (observaciones.isEmpty()) {
            txfObservaciones.requestFocus();
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Observación requerida",
                    "Debe ingresar una observación antes de observar la solicitud."
            );
            return;
        }

        boolean observacionesActualizadas =
                service.actualizarObservaciones(
                        seleccionada.getIdSolicitudAgendamiento(),
                        observaciones
                );

        if (!observacionesActualizadas) {
            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "No se pudo observar la solicitud",
                    "No fue posible guardar las observaciones."
            );
            return;
        }

        boolean estadoActualizado = service.observarSolicitud(
                seleccionada.getIdSolicitudAgendamiento()
        );

        if (!estadoActualizado) {
            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "No se pudo observar la solicitud",
                    "No fue posible actualizar el estado de la solicitud."
            );
            return;
        }

        refrescarTabla();
    }

    @FXML
    private void actualizarObservaciones() {

    }

    @FXML


    private void actualizarEstado() {
    }
    private void cargarSolicitudSeleccionada(
            SolicitudAgendamiento solicitud) {

        System.out.println(
                "[PANTALLA SOLICITUDES] Cargando datos en los campos para la solicitud: "
                        + solicitud.getIdSolicitudAgendamiento()
        );

        txfCuij.setText(
                String.valueOf(
                        solicitud.getCuij()
                )
        );

        txfCaratula.setText(
                solicitud.getCaratula()
        );

        txfTipoAudiencia.setText(String.valueOf(solicitud.getIdtipoAudiencia()));

        txfObservaciones.clear();
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
                (Stage) btnVolver
                        .getScene()
                        .getWindow();

        ventanaActual.close();

    }

}
