package com.android.melitchi.todolist.model;

/**
 * Created by fonta on 08/11/2016.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import com.android.melitchi.todolist.pojos.Note;


/**
 * Created by sca on 03/06/15.
 */
public class JsonParser {

    public static List<Note> getNotes(String json) throws JSONException {
        List <Note> notes = new LinkedList<>();
        JSONArray array = new JSONArray(json);
        JSONObject obj;
        Note note;
        for(int i=0; i < array.length(); i++){
            obj = array.getJSONObject(i);
            note = new Note(obj.optString("username"), obj.optString("note"), obj.optLong("date"), obj.optBoolean("done"), obj.optString("id"));
            notes.add(note);
        }

        return notes;
    }

    public static String getToken(String response) throws JSONException {
        return new JSONObject(response).optString("token");
    }
}