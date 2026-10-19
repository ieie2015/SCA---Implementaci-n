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

        System.out.println("Controller inicializado");
        System.out.println(tvSolicitudes);
        System.out.println(colCuij);
        System.out.println(colCaratula);

        System.out.println("Controller inicializado");

        colNroSolicitud.setCellValueFactory(new PropertyValueFactory<>("nroSolicitud"));

        colCuij.setCellValueFactory(new PropertyValueFactory<>("cuij"));

        colCaratula.setCellValueFactory(new PropertyValueFactory<>("caratula"));

        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));

        cargarCombos();

        refrescarTabla();
        tvSolicitudes.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         solicitudAnterior,
                         solicitudSeleccionada) -> {

                            if (solicitudSeleccionada != null) {

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
        tvSolicitudes.setItems(
                FXCollections.observableArrayList(service.buscarSolicitudes(
                        null, null, null, null)));

        var lista = service.buscarSolicitudes(
                null,
                null,
                null,
                null
        );

        System.out.println(
                "Solicitudes recuperadas: "
                        + lista.size()
        );

        tvSolicitudes.setItems(
                FXCollections.observableArrayList(
                        lista
                )
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

        if (!txfBuscarcuij.getText().isBlank()) {

            cuij =
                    Long.parseLong(
                            txfBuscarcuij.getText()
                    );
        }

        Long nroSolicitud = null;

        if (!txfNroSolicitud.getText().isBlank()) {
            nroSolicitud = Long.parseLong(txfNroSolicitud.getText());
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

        SolicitudAgendamiento seleccionada =
                tvSolicitudes
                        .getSelectionModel()
                        .getSelectedItem();

        if(seleccionada == null) {
            return;
        }

        service.procesarSolicitud(
                seleccionada
                        .getIdSolicitudAgendamiento()
        );

        refrescarTabla();
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

        service.observarSolicitud(
                seleccionada.getIdSolicitudAgendamiento()
        );

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

        txfCuij.setText(
                String.valueOf(
                        solicitud.getCuij()
                )
        );

        txfCaratula.setText(
                solicitud.getCaratula()
        );

        txfTipoAudiencia.setText(String.valueOf(solicitud.getIdtipoAudiencia()));

        txfObservaciones.setText(solicitud.getObservaciones());
    }
    @FXML
    private void volverMenuPrincipal() {

        DashBoardController.dashboardStage.show();

        Stage ventanaActual =
                (Stage) btnVolver
                        .getScene()
                        .getWindow();

        ventanaActual.close();

    }

}

