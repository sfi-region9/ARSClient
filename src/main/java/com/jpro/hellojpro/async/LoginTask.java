package com.jpro.hellojpro.async;

import com.jpro.hellojpro.HelloJProFXMLController;
import com.jpro.hellojpro.auth.AuthSDK;
import com.jpro.hellojpro.storage.SuperUser;
import com.jpro.hellojpro.storage.UserLocalStoreWeb;
import com.jpro.hellojpro.storage.WebPreferences;
import com.jpro.webapi.WebAPI;
import com.victorlaerte.asynctask.AsyncTask;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginTask extends AsyncTask<String, String, String[]> {

    private HelloJProFXMLController helloJProFXMLController;

    public LoginTask(HelloJProFXMLController controller) {
        this.helloJProFXMLController = controller;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public String[] doInBackground(String... strings) {
        if (strings.length < 2) {
            return new String[]{"false", "Require two arguments"};
        }
        AuthSDK a = new AuthSDK();
        try {
            return a.loginUser(strings[0], strings[1]);
        } catch (IOException e) {
            return new String[]{"false", "Error, please retry"};
        }
    }

    @Override
    public void onPostExecute(String[] s) {
        boolean b = Boolean.parseBoolean(s[0]);
        String message = s[1];
        if (b) {
            String[] m = message.split(Pattern.quote("}_}"));
            if (m.length < 5) {
                message = "Error";
                helloJProFXMLController.sendPopUp("Log In", "Error While Log In", message, Alert.AlertType.ERROR);
                return;
            }
            String username = m[0];
            String scc = m[1];
            String vessel = m[2];
            String name = m[3];
            String messengerid = m[4];
            String uuid = m[5];
            SuperUser user = new SuperUser(name, scc, vessel, "", uuid, messengerid, username);
            if (!WebAPI.isBrowser()) {
                helloJProFXMLController.userLocalStoreDesktop.setUserLoggedIn(true);
                helloJProFXMLController.userLocalStoreDesktop.storeUser(user);
                helloJProFXMLController.sendPopUp("Log In", "Success", "You are successfully login " + name + " !", Alert.AlertType.INFORMATION);
            } else {
                WebAPI webAPI = helloJProFXMLController.getjProApplication().getWebAPI();
                UserLocalStoreWeb web = new UserLocalStoreWeb(webAPI);
                web.setUserLoggedIn(true);
                web.storeUser(user);
                helloJProFXMLController.sendPopUp("Log In", "Success", "You are successfully login " + name + " !", Alert.AlertType.INFORMATION);
                webAPI.executeScript("document.location.reload()");
            }
        } else {
            helloJProFXMLController.sendPopUp("Log In", "Error While Log In", message, Alert.AlertType.ERROR);
        }
        helloJProFXMLController.initializeBaseLook();
    }

    @Override
    public void progressCallback(String... strings) {

    }
}
