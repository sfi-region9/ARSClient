package com.jpro.hellojpro.sdk;


import java.sql.SQLException;

public class CheckVessel {

    private String vesselid;
    private String coid;
    private String template;

    public CheckVessel(String vesselid, String coid, String template) {
        this.vesselid = vesselid;
        this.coid = coid;
        this.template = template;
    }


}
