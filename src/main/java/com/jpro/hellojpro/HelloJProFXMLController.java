package com.jpro.hellojpro;

import com.google.gson.Gson;
import com.jpro.hellojpro.async.LoginTask;
import com.jpro.hellojpro.async.RegisterTask;
import com.jpro.hellojpro.auth.Register;
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
import com.sun.org.apache.xpath.internal.operations.Bool;
import fr.colin.arssdk.ARSdk;
import fr.colin.arssdk.UserNotFoundException;
import fr.colin.arssdk.objects.User;
import fr.colin.arssdk.objects.Vessel;
import javafx.animation.ParallelTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by TB on 25.02.16.
 */
public class HelloJProFXMLController implements Initializable {

    @FXML
    private GridPane basepane;

    public static HashMap<String, Boolean> webEditMode = new HashMap<>();

    public UserLocalStoreDesktop userLocalStoreDesktop;
    UserLocalStoreWeb webPreferences;
    private JProApplication jProApplication;

    private static ArrayList<Vessel> allVessels;
    private static HashMap<String, Vessel> vesselsById = new HashMap<>();


    protected ParallelTransition pt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        TextField password = new TextField();
        password.setText("password");
        Button b = new Button("Log In");
        Button back = new Button("back");


        field.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        password.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");

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
            field.setDisable(true);
            password.setDisable(true);
            b.setDisable(true);
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
        TextField password = new TextField("password");
        TextField code = new TextField("code");
        Button submit = new Button("submit");
        Button back = new Button("back");

        name.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        username.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        scc.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        email.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        password.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        code.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");


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
            if (!code.getText().equals("9b664WYNrL6WC4hq2u9CF3c6jVf6kHcK2Wn9X3tn")) {
                return;
            }
            name.setDisable(true);
            username.setDisable(true);
            scc.setDisable(true);
            email.setDisable(true);
            password.setDisable(true);
            code.setDisable(true);
            submit.setDisable(true);
            RegisterTask r = new RegisterTask(this);
            r.execute(name.getText(), username.getText(), password.getText(), nameToId(vessel.getValue().toString()), email.getText(), scc.getText());
            initializeBaseLook();
        });


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
            return webPreferences.isEditMode();
        } else {
            return userLocalStoreDesktop.getPreferences().getBoolean("editmode", false);
        }
    }

    public void toggleEditMode() {
        boolean actual = getBoolean();
        System.out.println(actual);
        if (WebAPI.isBrowser()) {
            if (actual) {
                System.out.println("remove editmode");
                webPreferences.getPreferences().remove("editmode");
            } else {
                System.out.println("add editmode");
                webPreferences.getPreferences().putBoolean("editmode", true);
            }
            jProApplication.getWebAPI().executeScript("document.location.reload();");
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
            profile.setDisable(true);
            edmod.setDisable(true);
            send.setDisable(true);
            erase.setDisable(true);
            cT.setDisable(true);
            cD.setDisable(true);
            login.setDisable(false);
            register.setDisable(false);

        }

        if (logged) {
            login.setText("Log Out");
            register.setDisable(true);
            cT.setDisable(true);
            cD.setDisable(true);
            profile.setDisable(false);
            edmod.setDisable(false);
            send.setDisable(false);
            erase.setDisable(false);
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
                    initializeBaseLook();
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

        name.setDisable(true);
        username.setDisable(true);
        scc.setDisable(true);

        report.setText(getLogged().getReport());
        name.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        username.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");
        scc.setStyle("-fx-text-fill: #5B1414; -fx-fill: #AD722C; -fx-background-color: #AD722C");


        Border b = new Border(new BorderStroke(Color.web("5B1414"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));

        name.setBorder(b);
        username.setBorder(b);
        scc.setBorder(b);
        submit.setBorder(b);
        back.setBorder(b);


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
            jProApplication.getWebAPI().executeScript("alert('" + message + "') ");
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
