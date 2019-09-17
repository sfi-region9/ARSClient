package com.jpro.hellojpro.auth;

import com.google.gson.Gson;
import com.jpro.hellojpro.async.TestO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import fr.colin.arssdk.ARSdk;

import java.io.IOException;

public class AuthSDK {

    public AuthSDK() {

    }

    /**
     * @param name
     * @param username
     * @param password
     * @param vaisseau
     * @param email
     * @param scc
     * @return
     * @throws IOException
     */
    public String[] registerUser(String name, String username, String password, String vaisseau, String email, String scc) throws IOException {

        //  password = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(10, password.toCharArray());

        Register register = new Register(name, username, password, vaisseau, email, scc);
        OkHttpClient o = new OkHttpClient();
        Request r = new Request.Builder().url("https://auth.nwa2coco.fr/register").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(register))).build();

        String lig = o.newCall(r).execute().body().string();
        if (lig.contains("Error while register, ")) {
            return new String[]{"false", lig};
        }
        return new String[]{"true", lig};
    }

    public String[] loginUser(String username, String password) throws IOException {

        Login login = new Login(username, password);
        OkHttpClient o = new OkHttpClient();
        Request r = new Request.Builder().url("https://auth.nwa2coco.fr/login").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(login))).build();
        System.out.println(new Gson().toJson(login));
        String lig = o.newCall(r).execute().body().string();


        if (lig.contains("Error while login, ")) {
            return new String[]{"false", lig};
        }
        return new String[]{"true", lig};
    }

    public static void main(String... args) throws IOException {
        String s = ARSdk.HTTP_CLIENT.newCall(new Request.Builder().url("https://documentation.nwa2coco.fr/ars/version.php").get().build()).execute().body().string();
        System.out.println(s);
    }

    public static void reS(TestO v, boolean d) {
        v.value = d;
    }


}
