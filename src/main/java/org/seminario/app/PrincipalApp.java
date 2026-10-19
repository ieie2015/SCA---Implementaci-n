package org.seminario.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrincipalApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(
                getClass().getResource(
                        "/fxml/menuprincipal.fxml"
                )
        );

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/fxml/menuprincipal.fxml"
                        )
                );

        Scene scene = null;
        try {
            scene = new Scene(
                    loader.load()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.setTitle(
                "SCA - Sistema Centralizado Audiencias"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


