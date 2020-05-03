package com.example.festival;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.festival.utilitaires.Functions;
import com.example.festival.utilitaires.Http;
import com.example.festival.utilitaires.Manager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnexionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        Button buttonConnexion = (Button)findViewById(R.id.buttonConnexion);
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextLogin = (EditText)findViewById(R.id.editTextLogin);
                String login = editTextLogin.getText().toString();
                EditText editTextMdp = (EditText)findViewById(R.id.editTextMdp);
                String mdp = editTextMdp.getText().toString();
                mdp = Functions.md5(mdp);

                RequestBody formBody = new FormBody.Builder().add("login", login).add("mdp", mdp).build();
                Request myGetRequestnbRepresentations = new Request.Builder().url("http://anthonypineau.alwaysdata.net/connexion").post(formBody).build();

                Http.getInstance().newCall(myGetRequestnbRepresentations).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) { }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String textConnexion = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObjnbRepresentations = new JSONObject(textConnexion);
                                    String text = jsonObjnbRepresentations.getString("connexion");
                                    if(text.equals("true")){
                                        int id = jsonObjnbRepresentations.getInt("id");
                                        String nom = jsonObjnbRepresentations.getString("nom");
                                        String prenom = jsonObjnbRepresentations.getString("prenom");
                                        new Manager(id, nom, prenom);
                                        Toast.makeText(getApplicationContext(),"Bienvenue "+prenom, Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Login incorrect", Toast.LENGTH_LONG).show();
                                    }
                                } catch (final JSONException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        Button btnRetour = (Button)findViewById(R.id.buttonRetour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
