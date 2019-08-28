package com.jpro.hellojpro.storage;

import com.jpro.webapi.WebAPI;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserLocalStoreWeb {

    public static final String PREF_NAME = "arsuserstore";
    WebPreferences preferences;

    public UserLocalStoreWeb(WebAPI webAPI) {
        preferences = new WebPreferences(webAPI);
    }

    public void storeUser(SuperUser superUser) {
        preferences.putString("names", superUser.getName());
        preferences.putString("scc", superUser.getScc());
        preferences.putString("username", superUser.getUsername());
        preferences.putString("vessel", superUser.getVesselid());
        preferences.putString("messengerid", superUser.getMessengerid());
        preferences.putString("report", superUser.getReport());
        preferences.putString("uuid", superUser.getUuid());
    }

    public void updateReport(String report) {
        if (!isLoggedIn())
            return;
        preferences.remove("report");
        preferences.putString("report", report);
    }

    public void updateVessel(String vessel) {
        if (!isLoggedIn())
            return;
        preferences.remove("vessel");
        preferences.putString("vessel", vessel);
    }

    public SuperUser getLoggedUser() {
        return new SuperUser(preferences.getString("names", ""), preferences.getString("scc", ""), preferences.getString("vessel", ""), preferences.getString("report", ""), preferences.getString("uuid", ""), preferences.getString("messengerid", ""), preferences.getString("username", ""));
    }

    public void setUserLoggedIn(boolean loggedIn) {
        preferences.putBoolean("loggedIn", loggedIn);
    }

    public void clearUser() {
        preferences.clear();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("loggedIn", false);
    }


    public boolean isEditMode() {
        return preferences.getBoolean("editmode", false);
    }

    public WebPreferences getPreferences() {
        return preferences;
    }
}
