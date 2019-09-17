package com.jpro.hellojpro;

import com.google.gson.Gson;
import com.jpro.hellojpro.async.LoginTask;
import com.jpro.hellojpro.async.RegisterTask;
import com.jpro.hellojpro.async.ToggleDestroyTask;
import com.jpro.hellojpro.sdk.CheckVessel;
import com.jpro.hellojpro.sdk.CheckVesselName;
import com.jpro.hellojpro.storage.SuperUser;
import com.jpro.hellojpro.storage.UserLocalStoreDesktop;
import com.jpro.hellojpro.storage.UserLocalStoreWeb;
import com.jpro.webapi.JProApplication;
import com.jpro.webapi.WebAPI;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import fr.colin.arssdk.ARSdk;
import fr.colin.arssdk.UserNotFoundException;
import fr.colin.arssdk.objects.User;
import fr.colin.arssdk.objects.Vessel;
import javafx.animation.ParallelTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by TB on 25.02.16.
 */
public class HelloJProFXMLController implements Initializable {

    @FXML
    private GridPane basepane;

    private Media media;
    private MediaPlayer player;

    public static HashMap<String, Boolean> webEditMode = new HashMap<>();

    public UserLocalStoreDesktop userLocalStoreDesktop;
    UserLocalStoreWeb webPreferences;
    private JProApplication jProApplication;

    private static ArrayList<Vessel> allVessels;
    private static HashMap<String, Vessel> vesselsById = new HashMap<>();


    protected ParallelTransition pt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            //URI uri = getClass().getResource("/sounds/soundbig.mp3").toURI();
            URI uri;
            URL u = new URL("https://documentation.nwa2coco.fr/res/sounds.wav");
            uri = u.toURI();
            media = new Media(uri.toString());
            player = new MediaPlayer(media);
            player.setStartTime(Duration.ZERO);
            //     player.setStopTime(new Duration(1044));
            //    player.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        if (!WebAPI.isBrowser())
            userLocalStoreDesktop = new UserLocalStoreDesktop();
        try {
            allVessels = ARSdk.DEFAULT_INSTANCE.allVessels();
            allVessels.sort(Comparator.comparing(Vessel::getName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Vessel v : allVessels) {
            vesselsById.put(v.getVesselid(), v);
        }
    }


    public JProApplication getjProApplication() {
        return jProApplication;
    }

    public void initializeLoginLook() {
        basepane.getChildren().clear();
        basepane.setPadding(new Insets(20, 20, 20, 20));

        TextField field = new TextField("username");
        PasswordField password = new PasswordField();
        password.setText("password");
        Button b = new Button("Log In");
        Button back = new Button("back");


        field.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        password.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");

        password.setSkin(new TextFieldCaretControlSkin(password, Color.TRANSPARENT));

        b.setPrefWidth(120);
        b.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        field.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        password.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        back.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        basepane.add(b, 1, 2);
        basepane.add(field, 1, 0);
        basepane.add(password, 1, 1);
        basepane.add(back, 0, 3);
        b.setOnMouseClicked(event -> {
            toggle(true, b, field, password);
            LoginTask t = new LoginTask(this);
            t.execute(field.getText(), password.getText());
        });
        back.setOnMouseClicked(event -> {
            initializeBaseLook();
        });
    }

    public void initializeRegisterLook() {

        basepane.getChildren().clear();

        TextField name = new TextField("NAME Firstname");
        TextField username = new TextField("username");
        TextField scc = new TextField("SCC#");
        TextField email = new TextField("email");
        PasswordField password = new PasswordField();
        password.setText("password");
        TextField code = new TextField("code");
        Button submit = new Button("submit");
        Button back = new Button("back");

        name.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        username.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        scc.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        email.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        password.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        code.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");

        password.setSkin(new TextFieldCaretControlSkin(password, Color.TRANSPARENT));


        Border b = new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));

        name.setBorder(b);
        username.setBorder(b);
        scc.setBorder(b);
        email.setBorder(b);
        password.setBorder(b);
        code.setBorder(b);
        submit.setBorder(b);
        back.setBorder(b);


        ChoiceBox vessel = new ChoiceBox();
        for (Vessel allVessel : allVessels) {
            vessel.getItems().add(allVessel.getName().replace("_", " "));
        }
        vessel.setValue(vessel.getItems().get(0));
        vessel.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        vessel.setBorder(b);

        basepane.add(name, 0, 0);
        basepane.add(username, 0, 1);
        basepane.add(scc, 0, 2);
        basepane.add(email, 2, 2);
        basepane.add(password, 2, 0);
        basepane.add(code, 2, 1);
        basepane.add(vessel, 4, 0);
        basepane.add(submit, 4, 3);
        basepane.add(back, 0, 3);

        submit.setPrefWidth(120);

        GridPane.setHalignment(submit, HPos.RIGHT);

        back.setOnMouseClicked(event -> {
            initializeBaseLook();
        });


        submit.setOnMouseClicked(event -> {
            toggle(true, name, username, scc, email, password, code, submit);
            if (!code.getText().equals("9b664WYNrL6WC4hq2u9CF3c6jVf6kHcK2Wn9X3tn")) {
                sendPopUp("Register", "Register Process Error", "The verification code is wrong, please retry", Alert.AlertType.WARNING);
                toggle(false, name, username, scc, email, password, code, submit);
                return;
            }
            if (name.getText().isEmpty() || name.getText().length() < 3) {
                sendPopUp("Register", "Register Process Error", "The name length is lower than 3 character", Alert.AlertType.WARNING);
                toggle(false, name, username, scc, email, password, code, submit);

                return;
            }
            if (username.getText().isEmpty()) {
                sendPopUp("Register", "Register Process Error", "The username field is empty", Alert.AlertType.WARNING);
                toggle(false, name, username, scc, email, password, code, submit);

                return;
            }
            if (scc.getText().isEmpty() || scc.getText().length() < 5) {
                sendPopUp("Register", "Register Process Error", "The SCC length is lower than 5 character", Alert.AlertType.WARNING);
                toggle(false, name, username, scc, email, password, code, submit);

                return;
            }
            if (email.getText().isEmpty()) {
                sendPopUp("Register", "Register Process Error", "The email field is empty", Alert.AlertType.WARNING);
                toggle(false, name, username, scc, email, password, code, submit);

                return;
            }

            RegisterTask r = new RegisterTask(this);
            r.execute(name.getText(), username.getText(), password.getText(), nameToId(vessel.getValue().toString()), email.getText(), scc.getText());
        });


    }

    public void toggle(boolean b, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(b);
        }
    }


    public String nameToId(String vesselname) {
        return vesselname.toLowerCase().replace(" ", "");
    }

    private SuperUser getLogged() {
        if (WebAPI.isBrowser()) {
            return webPreferences.getLoggedUser();
        } else {
            return userLocalStoreDesktop.getLoggedUser();
        }
    }

    public boolean getBoolean() {
        if (WebAPI.isBrowser()) {
            return webEditMode.get("instance" + jProApplication.getWebAPI().getInstanceID());
        } else {
            return userLocalStoreDesktop.getPreferences().getBoolean("editmode", false);
        }
    }

    public void toggleEditMode() {
        boolean actual = getBoolean();
        System.out.println(actual);
        if (WebAPI.isBrowser()) {
            if (actual) {
                webEditMode.remove("instance" + jProApplication.getWebAPI().getInstanceID());
                webEditMode.put("instance" + jProApplication.getWebAPI().getInstanceID(), false);
            } else {
                webEditMode.remove("instance" + jProApplication.getWebAPI().getInstanceID());
                webEditMode.put("instance" + jProApplication.getWebAPI().getInstanceID(), true);
            }
        } else {
            userLocalStoreDesktop.getPreferences().remove("editmode");
            userLocalStoreDesktop.getPreferences().putBoolean("editmode", !actual);
        }
    }

    public void initializeBaseLook() {
        if (webPreferences == null)
            webPreferences = new UserLocalStoreWeb(jProApplication.getWebAPI());

        basepane.getChildren().clear();
        basepane.setPadding(new Insets(20, 20, 20, 20));
        // basepane.setAlignment(Pos.CENTER);
        TextArea area = new TextArea();
        Button login = new Button("Log In");
        Button register = new Button("Register");
        Button profile = new Button("Profile");
        Button edmod = new Button("Edition Mode");
        Button send = new Button("Send");
        Button erase = new Button("Erase");
        Button about = new Button("About");
        Button cT = new Button("Edit Template");
        Button cD = new Button("Edit Default");

        Button[] b = new Button[]{login, register, profile, edmod, send, erase, about, cT, cD};

        boolean logged = false;
        if (!WebAPI.isBrowser()) {
            logged = userLocalStoreDesktop.isLoggedIn();
            userLocalStoreDesktop.getPreferences().putBoolean("editmode", false);
        } else {
            logged = webPreferences.isLoggedIn();
            webEditMode.remove("instance" + jProApplication.getWebAPI().getInstanceID());
            webEditMode.put("instance" + jProApplication.getWebAPI().getInstanceID(), false);
        }


        edmod.setOnMouseClicked(event -> {
            toggleEditMode();
            boolean edm = getBoolean();
            System.out.println(edm);
            if (edm) {
                area.setText(getLogged().getReport());
            } else {
                area.setText("");
            }
        });


        if (!logged) {
            toggle(true, profile, edmod, send, erase, cT, cD);
        }

        if (logged) {
            login.setText("Log Out");
            toggle(true, register, cT, cD);
            toggle(false, profile, edmod, send, erase);
            try {
                User u = ARSdk.DEFAULT_INSTANCE.syncronizeUser(getLogged().getSdkUser());
                if (WebAPI.isBrowser()) {
                    String username = webPreferences.getPreferences().getString("username", "");
                    String messengerid = webPreferences.getPreferences().getString("messengerid", "");
                    boolean editmode = webPreferences.getPreferences().getBoolean("editmode");
                    webPreferences.clearUser();
                    webPreferences.storeUser(new SuperUser(u, messengerid, username));
                    webPreferences.setUserLoggedIn(true);
                    webPreferences.getPreferences().putBoolean("editmode", editmode);
                    if (editmode)
                        area.setText(webPreferences.getLoggedUser().getReport().replace("\\n", "\n"));
                } else {
                    String username = userLocalStoreDesktop.getPreferences().get("username", "");
                    String messengerid = userLocalStoreDesktop.getPreferences().get("messengerid", "");
                    userLocalStoreDesktop.clearUser();
                    userLocalStoreDesktop.storeUser(new SuperUser(u, messengerid, username));
                    userLocalStoreDesktop.setUserLoggedIn(true);
                }
            } catch (IOException | UserNotFoundException e) {
                e.printStackTrace();
            }
            ARSdk sdk = ARSdk.DEFAULT_INSTANCE;
            try {
                if (sdk.isCo(getLogged().getMessengerid(), getLogged().getVesselid())) {
                    cT.setDisable(false);
                    cD.setDisable(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        login.setOnMouseClicked(event -> {
            if (!WebAPI.isBrowser()) {
                if (userLocalStoreDesktop.isLoggedIn()) {
                    userLocalStoreDesktop.clearUser();
                    initializeBaseLook();
                    sendPopUp("Log Out", "Log Out", "You Log Out", Alert.AlertType.INFORMATION);
                    return;
                }
            } else {
                if (webPreferences.isLoggedIn()) {
                    webPreferences.clearUser();
                    //jProApplication.getWebAPI().executeScript("console.error('name');");
                    //jProApplication.getWebAPI().executeScript("createCookie('namesd','sdf',31);");
                    // webPreferences.setUserLoggedIn(false);
                    //    initializeBaseLook();
                    jProApplication.getWebAPI().executeScript("document.location.reload();");
                    sendPopUp("Log Out", "Log Out", "You Log Out", Alert.AlertType.INFORMATION);
                    return;
                }
            }
            initializeLoginLook();
        });

        register.setOnMouseClicked(event -> {
            initializeRegisterLook();
        });

        profile.setOnMouseClicked(event -> {
            initializeProfileLook();
        });

        erase.setOnMouseClicked(event -> {
            area.setText("");
        });

        send.setOnMouseClicked(event -> {
            for (Button button : b) {
                button.setDisable(true);
            }
            boolean editmode = getBoolean();
            ARSdk sdk = ARSdk.DEFAULT_INSTANCE;
            String report = getLogged().getReport();
            if (!editmode) {
                report = report + "\n" + area.getText();
            } else {
                report = area.getText();
            }
            User u = getLogged().getSdkUser();
            u.setReport(report);
            if (WebAPI.isBrowser()) {
                webPreferences.updateReport(report);
            } else {
                userLocalStoreDesktop.updateReport(report);
            }
            try {
                sdk.postUserReport(u);
            } catch (IOException e) {
                e.printStackTrace();
            }
            area.setText("");
            sendPopUp("Report", "Report Sended", "Your report was sended to the server", Alert.AlertType.INFORMATION);
            if (WebAPI.isBrowser()) {
                jProApplication.getWebAPI().executeScript("document.location.reload();");
            } else {
                initializeBaseLook();
            }
        });

        cT.setOnMouseClicked(event -> {
            initializeTemplateLook();
        });

        cD.setOnMouseClicked(event -> {
            initializeDefaultLook();
        });

        about.setOnMouseClicked(event -> {
            sendPopUp("About", "ARS", "The Automatic Report System is brought you by the team of the Science Section of the USS Versailles R9\nDevelopped with love by LT. Colin THOMAS\nHosted by USS Versailles\nPrivacy policy : https://reports.nwa2coco.fr/enpp.html (EN), https://reports.nwa2coco.fr/frpp.html (FR)", Alert.AlertType.INFORMATION);
        });

        //WEB :: LocalStorage
        //APP ::

        login.setPrefWidth(120);
        register.setPrefWidth(120);
        profile.setPrefWidth(120);
        edmod.setPrefWidth(120);
        send.setPrefWidth(120);
        erase.setPrefWidth(120);
        about.setPrefWidth(120);
        cT.setPrefWidth(120);
        cD.setPrefWidth(120);

        login.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        register.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        profile.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        edmod.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        send.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        about.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        erase.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        cT.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        cD.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        area.setBorder(new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));

        area.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C");


        GridPane.setHalignment(login, HPos.LEFT);
        GridPane.setHalignment(register, HPos.LEFT);
        GridPane.setHalignment(profile, HPos.LEFT);
        GridPane.setHalignment(cT, HPos.LEFT);

        GridPane.setMargin(area, new Insets(0, 0, 10, 0));

        GridPane.setHalignment(area, HPos.CENTER);
        GridPane.setHalignment(edmod, HPos.RIGHT);
        GridPane.setHalignment(send, HPos.RIGHT);
        GridPane.setHalignment(erase, HPos.RIGHT);
        GridPane.setHalignment(cD, HPos.RIGHT);


        GridPane.setValignment(cT, VPos.BOTTOM);
        GridPane.setValignment(about, VPos.BOTTOM);
        GridPane.setValignment(cD, VPos.BOTTOM);

        //143F48
        basepane.setStyle("-fx-background-color: #143F48");
        basepane.add(login, 0, 0);
        basepane.add(register, 0, 1);
        basepane.add(profile, 0, 2);
        basepane.add(cT, 0, 3);

        //   basepane.add(area, 1,0, 2, 2);
        basepane.add(area, 1, 0, 3, 3);
        basepane.add(about, 2, 3);


        basepane.add(edmod, 4, 0);
        basepane.add(send, 4, 1);
        basepane.add(erase, 4, 2);
        basepane.add(cD, 4, 3);
     /*   basepane.add(login, 0, 0);
        basepane.add(register, 1, 0);
        basepane.add(profile, 2, 0);
        basepane.add(cT, 3, 0); */
    }

    public void initializeProfileLook() {
        basepane.getChildren().clear();

        SuperUser su = getLogged();

        TextField name = new TextField(su.getName());
        TextField username = new TextField(su.getUsername());
        TextField scc = new TextField(su.getScc());
        TextArea report = new TextArea();
        report.setDisable(true);
        Button submit = new Button("submit");
        Button back = new Button("back");
        Button lock = new Button("Unlock/Lock");
        Button destroy = new Button("Destroy Account");

        Label uuid = new Label("Your UUID : " + su.getUuid());
        Label linked = new Label("Linked : " + !(su.getMessengerid().equalsIgnoreCase("undefined")));
        uuid.setWrapText(true);

        name.setDisable(true);
        username.setDisable(true);
        scc.setDisable(true);


        uuid.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        uuid.setVisible(false);
                        TextField textarea = new TextField(uuid.getText());
                        textarea.setPrefHeight(uuid.getHeight() + 10);
                        textarea.setStyle("-fx-text-fill: #5B1414; -fx-background-color: transparent;");
                        basepane.add(textarea, 1, 3);

                        textarea.setOnKeyPressed(event -> {
                            System.out.println(event.getCode());
                            if (event.getCode().toString().equals("ENTER")) {
                                basepane.getChildren().remove(textarea);
                                uuid.setVisible(true);
                            }
                        });
                    }
                }
            }
        });


        report.setText(getLogged().getReport());
        name.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        username.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        scc.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        uuid.setStyle("-fx-text-fill: #5B1414");
        linked.setStyle("-fx-text-fill: #5B1414");

        destroy.setStyle("-fx-text-fill: #AD722C; -fx-background-color: #5B1414");

        destroy.setBorder(new Border(new BorderStroke(Color.web("#AD722C"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));


        destroy.setOnMouseEntered(event -> {
            if (!destroy.isDisabled()) {
                player.play();
            }
            //PLAY DANGEROUS MUSIC
        });

        destroy.setOnMouseExited(event -> {
            player.stop();
        });

        destroy.setOnMouseClicked(event -> {


            User user = getLogged().getSdkUser();
            Request r = new Request.Builder().url("https://ars.nwa2coco.fr/destroy_user").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user))).build();
            Request rs = new Request.Builder().url("https://auth.nwa2coco.fr/destroy_user").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(user))).build();
            try {
                ARSdk.HTTP_CLIENT.newCall(r).execute().body().string();
                String s = ARSdk.HTTP_CLIENT.newCall(rs).execute().body().string();
                System.out.println(s);
                sendPopUp("Account Destroy", "Destroy Process", "Your account is successfully destroyed", Alert.AlertType.INFORMATION);
                if (WebAPI.isBrowser()) {
                    webPreferences.clearUser();
                    initializeBaseLook();
                    jProApplication.getWebAPI().executeScript("document.location.reload();");
                } else {
                    userLocalStoreDesktop.clearUser();
                    initializeBaseLook();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Border b = new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));

        name.setBorder(b);
        username.setBorder(b);
        scc.setBorder(b);
        submit.setBorder(b);
        back.setBorder(b);
        lock.setBorder(b);

        destroy.setDisable(true);
        lock.setOnMouseClicked(event -> {
            destroy.setDisable(false);
            lock.setDisable(true);
            new ToggleDestroyTask().execute(lock, destroy, player);
        });


        ChoiceBox vessel = new ChoiceBox();
        for (Vessel allVessel : allVessels) {
            vessel.getItems().add(allVessel.getName().replace("_", " "));
        }
        vessel.setValue(getVesselsById(su.getVesselid()).getName().replace("_", " "));
        vessel.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        vessel.setBorder(b);


        basepane.add(name, 0, 0);
        basepane.add(username, 0, 1);
        basepane.add(scc, 2, 0);
        basepane.add(vessel, 4, 0);
        basepane.add(submit, 4, 3);
        basepane.add(back, 0, 3);
        basepane.add(uuid, 1, 3);
        basepane.add(linked, 2, 3);

        basepane.add(lock, 0, 2);
        basepane.add(destroy, 1, 2);
        submit.setPrefWidth(120);

        GridPane.setHalignment(submit, HPos.RIGHT);

        back.setOnMouseClicked(event -> {
            initializeBaseLook();
        });

        submit.setOnMouseClicked(event -> {
            submit.setDisable(true);
            vessel.setDisable(true);
            try {
                ARSdk.DEFAULT_INSTANCE.switchVessel(su, nameToId(vessel.getValue().toString()));
                if (WebAPI.isBrowser()) {
                    webPreferences.updateVessel(nameToId(vessel.getValue().toString()));
                    sendPopUp("Profile", "Switch Vessel", "You switch to the " + vessel.getValue().toString(), Alert.AlertType.INFORMATION);
                    initializeBaseLook();
                    jProApplication.getWebAPI().executeScript("document.location.reload();");
                } else {
                    userLocalStoreDesktop.updateVessel(nameToId(vessel.getValue().toString()));
                    sendPopUp("Profile", "Switch Vessel", "You switch to the " + vessel.getValue().toString(), Alert.AlertType.INFORMATION);
                    initializeBaseLook();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    public void initializeTemplateLook() {
        basepane.getChildren().clear();

        TextArea templateArea = new TextArea();
        Button defaultTemplate = new Button("Default Template");
        Button send = new Button("Send");
        Button miniTempalte = new Button("Mini Template");
        Border b = new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
        Button back = new Button("back");
        defaultTemplate.setBorder(b);
        send.setBorder(b);
        miniTempalte.setBorder(b);
        templateArea.setBorder(b);
        back.setBorder(b);

        defaultTemplate.setOnMouseClicked(event -> {
            templateArea.setText("Name : %name%\nDate : %date%\nSCC : %scc%");
        });

        miniTempalte.setOnMouseClicked(event -> {
            templateArea.setText("Name : %name%\nSCC : %scc%");
        });

        templateArea.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        GridPane.setMargin(templateArea, new Insets(0, 0, 10, 0));
        GridPane.setHalignment(defaultTemplate, HPos.LEFT);
        GridPane.setHalignment(send, HPos.LEFT);
        GridPane.setHalignment(miniTempalte, HPos.RIGHT);
        GridPane.setHalignment(templateArea, HPos.CENTER);

        basepane.add(defaultTemplate, 0, 0);
        basepane.add(templateArea, 1, 1, 3, 2);
        basepane.add(send, 2, 3);
        basepane.add(miniTempalte, 4, 0);
        basepane.add(back, 0, 3);
        send.setOnMouseClicked(event -> {
            CheckVessel vesselName = new CheckVessel(getLogged().getVesselid(), getLogged().getMessengerid(), templateArea.getText().replace("\n", "\\n"));
            try {
                ARSdk.HTTP_CLIENT.newCall(new Request.Builder().url("https://ars.nwa2coco.fr/update_template").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(vesselName))).build()).execute();
                sendPopUp("Default Template", "Default Template Altered", "You changed the default template of your vessel", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initializeBaseLook();
        });
        back.setOnMouseClicked(event -> {
            initializeBaseLook();
        });

    }

    public void initializeDefaultLook() {
        basepane.getChildren().clear();

        TextArea templateArea = new TextArea();
        Button defaultTemplate = new Button("Default Report");
        Button send = new Button("Send");
        Border b = new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
        Button back = new Button("back");
        templateArea.setBorder(b);
        defaultTemplate.setBorder(b);
        send.setBorder(b);
        back.setBorder(b);
        templateArea.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        GridPane.setMargin(templateArea, new Insets(0, 0, 10, 0));
        GridPane.setHalignment(defaultTemplate, HPos.LEFT);
        GridPane.setHalignment(back, HPos.LEFT);
        GridPane.setHalignment(send, HPos.LEFT);
        GridPane.setHalignment(templateArea, HPos.CENTER);

        send.setPrefWidth(defaultTemplate.getPrefWidth());
        back.setPrefWidth(defaultTemplate.getPrefWidth());

        basepane.add(defaultTemplate, 1, 0);
        basepane.add(templateArea, 1, 1, 3, 2);
        basepane.add(send, 2, 0);
        basepane.add(back, 3, 0);

        defaultTemplate.setOnMouseClicked(event -> {
                    templateArea.setText("Nothing to report");
                }
        );


        send.setOnMouseClicked(event -> {
            CheckVesselName vesselName = new CheckVesselName(getLogged().getVesselid(), getLogged().getMessengerid(), templateArea.getText().replace("\n", "\\n"));
            try {
                ARSdk.HTTP_CLIENT.newCall(new Request.Builder().url("https://ars.nwa2coco.fr/update_name").post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(vesselName))).build()).execute();
                sendPopUp("Default Report", "Default Report Altered", "You changed the default report of your vessel", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initializeBaseLook();
        });
        back.setOnMouseClicked(event -> {
            initializeBaseLook();
        });

    }


    public void init(JProApplication jProApplication) {
        this.jProApplication = jProApplication;
    }

    public void sendPopUp(String title, String header, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Label label = new Label(message);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);

        if (WebAPI.isBrowser()) {
            jProApplication.getWebAPI().executeScript("alert('" + message.replace("\n", "\\n") + "') ");
        } else {
            alert.showAndWait();
        }
    }

    public static ArrayList<Vessel> getAllVessels() {
        return allVessels;
    }

    public static Vessel getVesselsById(String id) {
        return vesselsById.getOrDefault(id, null);
    }
}
