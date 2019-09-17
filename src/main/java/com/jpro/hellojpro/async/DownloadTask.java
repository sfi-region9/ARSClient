package com.jpro.hellojpro.async;

import com.victorlaerte.asynctask.AsyncTask;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class DownloadTask extends AsyncTask<Object, Double, String> {
    ProgressBar bar;
    Alert sd;

    @Override
    public void onPreExecute() {

    }

    @Override
    public String doInBackground(Object... params) {
        int count;

        bar = (ProgressBar) params[2];
        sd = (Alert) params[3];
        System.out.println(params[1]);
        System.out.println("Download In Progress");
        try {
            URLConnection connection = new URL((String) params[0]).openConnection();
            connection.connect();
            int length = connection.getContentLength();
            InputStream s = new BufferedInputStream(new URL((String) params[0]).openStream(), 8192);
            OutputStream outputStream = new FileOutputStream((String) params[1]);
            byte[] d = new byte[1024];

            double total = 0;
            while ((count = s.read(d)) != -1) {
                total += count;
                outputStream.write(d, 0, count);
                System.out.println("Total downloaded " + total + " on " + length);
                publishProgress((double) (total / length));
            }
            outputStream.flush();
            outputStream.close();

            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (String) params[1];
    }

    @Override
    public void onPostExecute(String params) {
        Alert ls = new Alert(Alert.AlertType.INFORMATION);
        ls.setTitle("Download");
        ls.setHeaderText("Download Complete");
        Label l = new Label("The download is complete, you can find the file in the directory where is the current version, the file is named " + params);
        l.setWrapText(true);
        ls.getDialogPane().setContent(l);
        Optional<ButtonType> b = ls.showAndWait();
        System.exit(0);

    }

    @Override
    public void progressCallback(Double... params) {
        bar.setProgress(params[0]);
    }
}
