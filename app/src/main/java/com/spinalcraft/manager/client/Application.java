package com.spinalcraft.manager.client;

import java.sql.Timestamp;

public class Application implements Comparable{
    public String username;
    public String country;
    public int year;
    public String heard;
    public String email;
    public int timestamp;
    public String comment;

    @Override
    public int compareTo(Object another) {
        Application other = (Application)another;
        return this.timestamp - other.timestamp;
    }
}