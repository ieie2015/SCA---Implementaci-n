package org.seminario.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


    public class DashBoardController implements Initializable {

        @FXML
        private Button btnSolicitudes;

        @FXML
        private Button btnAudiencias;

        @FXML
        private Button btnDisponibilidades;

        @FXML
        private Button btnSalas;

        @FXML
        private Button btnReportes;

        @FXML
        private Button btnSalir;
        public static Stage dashboardStage;

        private void mostrarModuloEnDesarrollo() {

            Alert alert =
                    new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("En desarrollo");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Funcionalidad prevista para una próxima iteración."
            );

            alert.showAndWait();

        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {

            System.out.println("Dashboard inicializado");

        }

        @FXML
        private void abrirSolicitudes() {

            try {

                dashboardStage =
                        (Stage) btnSolicitudes
                                .getScene()
                                .getWindow();

                dashboardStage.hide();

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/fxml/SolicitudAgendamiento.fxml"
                                )
                        );

                Scene scene =
                        new Scene(loader.load());

                Stage ventanaSolicitud =
                        new Stage();

                ventanaSolicitud.setScene(scene);

                ventanaSolicitud.show();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }
        @FXML
        private void abrirAudiencias() {

            mostrarModuloEnDesarrollo();
        }
        @FXML
        private void abrirDisponibilidades() {
            mostrarModuloEnDesarrollo();

        }
        @FXML
        private void abrirSalas() {
            mostrarModuloEnDesarrollo();
        }
        @FXML
        private void abrirReportes() {
            mostrarModuloEnDesarrollo();
        }
        @FXML
        private void salir() {

            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alert.setTitle("Salir");
            alert.setHeaderText(null);
            alert.setContentText(
                    "¿Desea salir del sistema?"
            );

            Optional<ButtonType> resultado =
                    alert.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);


            }

        }

    }
