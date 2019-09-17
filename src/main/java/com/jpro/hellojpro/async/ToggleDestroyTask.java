package com.jpro.hellojpro.async;

import com.victorlaerte.asynctask.AsyncTask;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;

public class ToggleDestroyTask extends AsyncTask<Object, Void, Void> {
    @Override
    public void onPreExecute() {

    }

    @Override
    public Void doInBackground(Object... buttons) {
        try {
            Thread.sleep(7000);
            ((Button)  buttons[0]).setDisable(false);
            ((Button) buttons[1]).setDisable(true);
            ((MediaPlayer) buttons[2]).stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {

    }

    @Override
    public void progressCallback(Void... voids) {

    }
}
