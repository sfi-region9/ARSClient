package com.jpro.hellojpro.sdk;

import java.sql.SQLException;

public class CheckVesselName {

    private String vesselid;
    private String coid;
    private String text;

    public CheckVesselName(String vesselid, String coid, String text) {
        this.vesselid = vesselid;
        this.coid = coid;
        this.text = text;
    }

}
