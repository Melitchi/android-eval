package com.android.melitchi.todolist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.android.melitchi.todolist.model.*;


public class SigninActivity extends AppCompatActivity {
    Button createAccount;
    Button connect;
    EditText login;
    EditText pass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        login=(EditText)findViewById(R.id.login);
        pass=(EditText)findViewById(R.id.pass);
        createAccount=(Button)findViewById(R.id.btnCreateAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        connect=(Button)findViewById(R.id.btnConnexion);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty()|| pass.getText().toString().isEmpty()) {
                    //editable.setError("Merci de remplir le champs");
                    Toast.makeText(SigninActivity.this, "champs vide", Toast.LENGTH_SHORT).show();
                }else{
                    displayLoader(true);
                    new SigninActivity.ConnectionAsyncTask(v.getContext()).execute(login.getText().toString(),pass.getText().toString());
                }
            }
        });
    }
    private void displayLoader(boolean toDisplay){
        if(toDisplay){
            progressDialog = new ProgressDialog(SigninActivity.this);
            progressDialog.setTitle("Chargement");
            progressDialog.setMessage("Connexion au compte...");
            progressDialog.show();
        }else{
            if(progressDialog !=null && progressDialog.isShowing()){
                progressDialog.cancel();
            }else{
                Toast.makeText(this, "pg inexistante", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class ConnectionAsyncTask extends AsyncTask<String, Void,  HttpResult> {

        Context context;

        public ConnectionAsyncTask(final Context context){
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
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/signin", theMap, null);

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
                Toast.makeText(context, "Vous êtes connecté", Toast.LENGTH_SHORT).show();
                String token="";
                try {
                    token = new JSONObject(response.json).optString("token");
                    PreferenceHelper.setToken(SigninActivity.this,token);
                    Intent in = new Intent(SigninActivity.this, NotesActivity.class);
                    startActivity(in);
                } catch (JSONException e) {
                    Log.e("token","unable to get token",e);
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SigninActivity.this, "signin failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
