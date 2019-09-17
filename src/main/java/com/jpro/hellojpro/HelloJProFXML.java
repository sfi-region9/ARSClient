package com.jpro.hellojpro;

import com.jpro.hellojpro.async.DownloadTask;
import com.jpro.webapi.JProApplication;
import com.jpro.webapi.WebAPI;
import com.squareup.okhttp.Request;
import fr.colin.arssdk.ARSdk;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class HelloJProFXML extends JProApplication {
    public static void main(String[] args) {
        launch(args);
    }

    public static final String VERSION = "v1.6";


    @Override
    public void start(Stage stage) throws IOException {
        //load user interface as FXML file

        if (!WebAPI.isBrowser()) {
            String s = ARSdk.HTTP_CLIENT.newCall(new Request.Builder().url("https://documentation.nwa2coco.fr/ars/version.php").get().build()).execute().body().string().replace(" ", "");
            s = removeUTF8BOM(s);
            if (!s.equalsIgnoreCase(VERSION) && !VERSION.contains("bêta")) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Update");
                a.setHeaderText("A New update is available");
                String link = removeUTF8BOM("https://documentation.nwa2coco.fr/ars/download/ARSClient-" + s + ".jar");
                Label label = new Label("Your version ( " + VERSION + " ) is out of date, please download the update here : " + link);
                label.setWrapText(true);
                a.getDialogPane().setContent(label);
                ButtonType download = new ButtonType("Download JAR");
                ButtonType downloade = new ButtonType("Download EXE");
                a.getButtonTypes().add(download);
                a.getButtonTypes().add(downloade);
                Optional<ButtonType> options = a.showAndWait();
                if (options.get() == download) {
                    //OPEN BROWSER
                    Alert sd = new Alert(Alert.AlertType.INFORMATION);
                    sd.getButtonTypes().clear();
                    sd.setTitle("Download");
                    sd.setHeaderText("Download in progress");
                    System.out.println("Start Download");
                    ProgressBar bar = new ProgressBar(0);
                    bar.setPrefWidth(1000);
                    sd.getDialogPane().setContent(bar);


                    new DownloadTask().execute(link, "ARSClient-" + s + ".jar", bar, sd);
                    sd.showAndWait();
                } else if (options.get() == downloade) {
                    Alert sd = new Alert(Alert.AlertType.INFORMATION);
                    sd.getButtonTypes().clear();

                    sd.setTitle("Download");
                    sd.setHeaderText("Download in progress");
                    System.out.println("Start Download");
                    ProgressBar bar = new ProgressBar(0);
                    bar.setPrefWidth(1000);
                    sd.getDialogPane().setContent(bar);


                    new DownloadTask().execute("https://documentation.nwa2coco.fr/ars/download/ARSClient-" + s + ".exe", "ARSClient-" + s + ".exe", bar, sd);
                    sd.showAndWait();
                }
            }
        }

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
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
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
        stage.getIcons().add(new Image("https://documentation.nwa2coco.fr/favicon.png"));
        stage.setWidth(1280);
        stage.show();
        System.out.println("Total loading time : " + (System.currentTimeMillis() - time));
    }

    public static final String UTF8_BOM = "\uFEFF";

    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
}
