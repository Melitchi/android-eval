package com.android.melitchi.todolist.pojos;

/**
 * Created by fonta on 10/11/2016.
 */

public class Note {
    String id;
    String username;
    String note; //text
    long date;
    boolean done;

    public Note(String username, String note, long date, boolean done, String id) {
        this.username = username;
        this.note = note;
        this.date = date;
        this.done = done;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNote() {
        return note;
    }

    public long getDate() {
        return date;
    }

    public boolean getDone() {
        return done;
    }
}
