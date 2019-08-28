package com.jpro.hellojpro.storage;

import com.google.gson.internal.$Gson$Types;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserLocalStoreDesktop {

    public static final String PREF_NAME = "arsuserstore";
    Preferences preferences;

    public UserLocalStoreDesktop() {
        preferences = Preferences.userRoot().node(PREF_NAME);
    }

    public void storeUser(SuperUser superUser) {
        preferences.put("name", superUser.getName());
        preferences.put("scc", superUser.getScc());
        preferences.put("username", superUser.getUsername());
        preferences.put("vessel", superUser.getVesselid());
        preferences.put("messengerid", superUser.getMessengerid());
        preferences.put("report", superUser.getReport());
        preferences.put("uuid", superUser.getUuid());
    }

    public void updateReport(String report) {
        if (!isLoggedIn())
            return;
        preferences.remove("report");
        preferences.put("report", report);
    }

    public void updateVessel(String vessel) {
        if (!isLoggedIn())
            return;
        preferences.remove("vessel");
        preferences.put("vessel", vessel);
    }

    public SuperUser getLoggedUser() {
        return new SuperUser(preferences.get("name", ""), preferences.get("scc", ""), preferences.get("vessel", ""), preferences.get("report", ""), preferences.get("uuid", ""), preferences.get("messengerid", ""), preferences.get("username", ""));
    }

    public void setUserLoggedIn(boolean loggedIn) {
        preferences.putBoolean("loggedIn", loggedIn);
    }

    public void clearUser() {
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("loggedIn", false);
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
