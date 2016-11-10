package com.android.melitchi.todolist.model;

/**
 * Created by fonta on 07/11/2016.
 */
public class HttpResult {

    public int code;
    public String json;

    public HttpResult(int code, String s) {
        this.code = code;
        this.json = s;
    }
}