package com.android.melitchi.todolist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.melitchi.todolist.model.HttpResult;
import com.android.melitchi.todolist.model.NetworkHelper;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText login;
    EditText pass;
    EditText img;
    Button create;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        login = (EditText)findViewById(R.id.loginCreate);
        pass = (EditText)findViewById(R.id.passCreate);
        img= (EditText)findViewById(R.id.img);
        create=(Button)findViewById(R.id.btnCreate);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()) {
                    //editable.setError("Merci de remplir le champs");
                    Toast.makeText(SignupActivity.this, "champs vide", Toast.LENGTH_SHORT).show();
                }else{
                    displayLoader(true);
                    new HelloAsyncTask(v.getContext()).execute(login.getText().toString(),pass.getText().toString(),img.getText().toString());
                }
            }
        });
    }
    private void displayLoader(boolean toDisplay){
        if(toDisplay){
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setTitle("Chargement");
            progressDialog.setMessage("Création du compte...");
            progressDialog.show();
        }else{
            if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.cancel();
            }else{
                Toast.makeText(this, "pg inexistante", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class HelloAsyncTask extends AsyncTask<String, Void, HttpResult> {

        Context context;

        public HelloAsyncTask(final Context context){
            this.context = context;
        }

        @Override
        protected HttpResult doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                return new HttpResult(500, null);
            }
            try {
                Map<String, String> theMap = new HashMap<>();
                theMap.put("username", params[0]);
                theMap.put("pwd", params[1]);
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/signup", theMap, null);

                return result;
            }catch (Exception e){
                Log.e("netwotkHelper",e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(final HttpResult response){
            displayLoader(false);
            if(response.code == 200){
                SignupActivity.this.finish();
            } else {
                Toast.makeText(SignupActivity.this, "signup failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
