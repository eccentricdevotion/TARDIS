/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.storage;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMMessage {

    private int id;
    private UUID who;
    private String message;
    private String date;
    private boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getWho() {
        return who;
    }

    public void setWho(UUID who) {
        this.who = who;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
