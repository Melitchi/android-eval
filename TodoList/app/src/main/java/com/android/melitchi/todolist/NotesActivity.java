package com.android.melitchi.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.melitchi.todolist.pojos.Note;

/**
 * Created by fonta on 10/11/2016.
 */

public class NotesActivity extends AppCompatActivity {
    private String token;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        token=PreferenceHelper.getToken(NotesActivity.this);
        if (token == "") {
            Toast.makeText(this, "No token. Can't display activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NotesActivity.this,SigninActivity.class));
            finish();
        }
    }
}
