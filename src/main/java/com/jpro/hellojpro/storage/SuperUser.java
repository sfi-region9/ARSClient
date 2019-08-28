package com.jpro.hellojpro.storage;

import fr.colin.arssdk.objects.User;

public class SuperUser extends User {

    private String messengerid;
    private String username;

    public SuperUser(String name, String scc, String vesselid, String report, String uuid, String messengerid, String username) {
        super(name, scc, vesselid, report, uuid);
        this.messengerid = messengerid;
        this.username = username;
    }

    public SuperUser(User user, String messengerid, String username) {
        super(user.getName(), user.getScc(), user.getVesselid(), user.getReport(), user.getUuid());
        this.messengerid = messengerid;
        this.username = username;
    }

    public User getSdkUser() {
        return new User(getName(), getScc(), getVesselid(), getReport(), getUuid());
    }

    public String getUsername() {
        return username;
    }

    public String getMessengerid() {
        return messengerid;
    }
}
