package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application3D extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        final var fxmlLoader = new FXMLLoader(Controller3D.class.getResource("Controller3D.fxml"));
        final var scene = new Scene(fxmlLoader.load(), 700, 500);
        Controller3D controller = fxmlLoader.getController();
        controller.setUpScene(scene);

        stage.setTitle("Timkin Dmitry, seminar 5");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}