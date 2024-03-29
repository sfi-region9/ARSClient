package com.jpro.hellojpro.storage;

import com.jpro.webapi.WebAPI;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebPreferences {

    private WebAPI webAPI;

    public WebPreferences(WebAPI webAPI) {
        this.webAPI = webAPI;
        try {
            if (WebAPI.isBrowser())
                webAPI.loadJSFile(new URL("https://documentation.nwa2coco.fr/js/cookies.js"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void putString(String key, String value) {
        webAPI.executeScript("createCookie('" + key + "','" + value.replace(" ", "[@_@]}").replace("\n", "}newline}") + "',31)");
    }

    public void putBoolean(String key, boolean value) {
        webAPI.executeScript("createCookie('" + key + "'," + value + ",31)");
    }

    public String getString(String key, String... def) {
        Map<String, String> m = webAPI.getCookies();
        return m.getOrDefault(key, "").replace("[@_@]}", " ").replace("}newline}", "\n");
    }


    public boolean getBoolean(String key, boolean... b) {
        Map<String, String> m = webAPI.getCookies();
        if (m.containsKey(key)) {
            return Boolean.parseBoolean(m.get(key));
        } else {
            return false;
        }

    }

    public void clear() {
        webAPI.executeScript("clearCookies();");
        System.out.println("ClearCalled" +
                "");
    }

    public void remove(String key) {
        webAPI.executeScript("eraseCookie('" + key + "')");
    }


}
