package com.jpro.hellojpro;

import com.jpro.webapi.JProApplication;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class HelloJProFXML extends JProApplication {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        //load user interface as FXML file
        long time = System.currentTimeMillis();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jpro/hellojpro/fxml/HelloJPro.fxml"));
        Scene scene = null;
        HelloJProFXMLController controller;
        try {
            Parent root = loader.load();
            controller = loader.getController();
            controller.init(this);
            stage.addEventHandler(WindowEvent.WINDOW_SHOWING, event -> {
                controller.initializeBaseLook();
            });
            //create JavaFX scene
            scene = new Scene(root);


        } catch (IOException e) {
            e.printStackTrace();
        }

        scene.getStylesheets().add("com/jpro/hellojpro/css/HelloJPro.css");

        Rectangle2D r = Screen.getPrimary().getBounds();
        stage.setTitle("ARS Client");

        stage.setScene(scene);
        stage.setResizable(true);
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.show();
        System.out.println("Total loading time : " + (System.currentTimeMillis() - time));
    }
}
