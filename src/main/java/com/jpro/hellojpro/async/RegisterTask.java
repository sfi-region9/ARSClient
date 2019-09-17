package com.jpro.hellojpro.async;

import com.jpro.hellojpro.HelloJProFXMLController;
import com.victorlaerte.asynctask.AsyncTask;
import fr.colin.arssdk.ARSdk;
import javafx.scene.control.Alert;

import java.io.IOException;

public class RegisterTask extends AsyncTask<String, String, String[]> {

    private HelloJProFXMLController helloJProFXMLController;

    public RegisterTask(HelloJProFXMLController controller) {
        this.helloJProFXMLController = controller;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public String[] doInBackground(String... strings) {
        if (strings.length < 6) {
            return new String[]{"false", "Require six arguments"};
        }
        try {
            String[] c = ARSdk.DEFAULT_INSTANCE.registerUser(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
            return c;
        } catch (IOException e) {
            return new String[]{"false", "Error, please retry"};
        }
    }


    @Override
    public void onPostExecute(String[] s) {
        boolean b = Boolean.parseBoolean(s[0]);
        String message = s[1];
        if (b) {
            helloJProFXMLController.sendPopUp("Register", "Register", "You are successfully register, you can now login", Alert.AlertType.INFORMATION);
        } else {
            helloJProFXMLController.sendPopUp("Register", "Error While Log In", message, Alert.AlertType.ERROR);
        }
        helloJProFXMLController.initializeBaseLook();
    }

    @Override
    public void progressCallback(String... strings) {

    }
}
