package org.seminario.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.seminario.model.Audiencia;
import org.seminario.service.AudienciaService;

public class GestionAudienciasController {

    private final AudienciaService service = new AudienciaService();

    @FXML
    private TableView<Audiencia> tvAudiencias;

    @FXML
    private TableColumn<Audiencia, Long> colNroAudiencia;

    @FXML
    private TableColumn<Audiencia, Long> colIdSolicitud;

    @FXML
    private TableColumn<Audiencia, Long> colTipoAudiencia;

    @FXML
    private TableColumn<Audiencia, String> colEstado;

    @FXML
    private TableColumn<Audiencia, Integer> colDuracion;

    @FXML
    public void initialize() {
        colNroAudiencia.setCellValueFactory(
                new PropertyValueFactory<>("nroAudiencia")
        );
        colIdSolicitud.setCellValueFactory(
                new PropertyValueFactory<>("idSolicitudAgendamiento")
        );
        colTipoAudiencia.setCellValueFactory(
                new PropertyValueFactory<>("idTipoAudiencia")
        );
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );
        colDuracion.setCellValueFactory(
                new PropertyValueFactory<>("duracionEstimada")
        );

        refrescarTabla();
    }

    @FXML
    private void refrescar() {
        refrescarTabla();
    }

    @FXML
    private void volverMenuPrincipal() {
        if (DashBoardController.dashboardStage != null) {
            DashBoardController.dashboardStage.show();
        }

        Stage ventanaActual =
                (Stage) tvAudiencias.getScene().getWindow();
        ventanaActual.close();
    }

    private void refrescarTabla() {
        tvAudiencias.setItems(
                FXCollections.observableArrayList(
                        service.listarPendientesPlanificacion()
                )
        );
    }
}
