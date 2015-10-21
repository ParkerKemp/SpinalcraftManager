package com.spinalcraft.manager.client;

import java.io.Serializable;

public class Application implements Comparable, Serializable {
    public String uuid;
    public String username;
    public String country;
    public int year;
    public String heard;
    public String email;
    public String comment;
    public int status;
    public String staffActor;
    public int actionTimestamp;
    public int timestamp;

    @Override
    public int compareTo(Object another) {
        Application other = (Application)another;
        return this.timestamp - other.timestamp;
    }
}