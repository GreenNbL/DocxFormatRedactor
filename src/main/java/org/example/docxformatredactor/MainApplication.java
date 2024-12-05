package org.example.docxformatredactor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 479, 210);
        stage.setTitle("Word Format Redactor");
        stage.setScene(scene);
        stage.show();
        Controller controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
    }

    public static void main(String[] args) {

        launch();
    }
}



