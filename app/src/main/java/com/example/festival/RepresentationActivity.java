package com.example.festival;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.festival.utilitaires.Functions;
import com.example.festival.utilitaires.Http;
import com.example.festival.utilitaires.Manager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RepresentationActivity extends AppCompatActivity {
    String idRepresentation;
    String nbPlacesDisponibles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representation);
        String representation = getIntent().getStringExtra("representation");

        Request requestUneRepresentation = new Request.Builder().url("http://anthonypineau.alwaysdata.net/representation/"+representation).build();
        Http.getInstance().newCall(requestUneRepresentation).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject r = new JSONObject(body);
                            idRepresentation = r.getString("id");
                            String date = r.getString("date");
                            String heureDebut = r.getString("heureDebut");
                            String heureFin = r.getString("heureFin");
                            nbPlacesDisponibles = r.getString("nbPlacesDisponibles");

                            JSONObject lieuJson = r.getJSONObject("lieu");
                            String nomLieu = lieuJson.getString("nom");
                            String capAccueilLieu = lieuJson.getString("capAccueil");

                            JSONObject groupeJson = r.getJSONObject("groupe");
                            String nomGroupe = groupeJson.getString("nom");

                            TextView textViewGroupe = (TextView) findViewById(R.id.textViewAfficheGroupe);
                            textViewGroupe.setText(" "+nomGroupe);
                            TextView textViewLieu = (TextView) findViewById(R.id.textViewAfficheLieu);
                            textViewLieu.setText(" "+nomLieu);
                            TextView textViewDate = (TextView)findViewById(R.id.textViewAfficheDate);
                            textViewDate.setText(" "+date);
                            TextView textViewHeureDeb = (TextView)findViewById(R.id.textViewAfficheHeureDeb);
                            textViewHeureDeb.setText(" "+heureDebut);
                            TextView textViewHeureFin = (TextView)findViewById(R.id.textViewAfficheHeureFin);
                            textViewHeureFin.setText(" "+heureFin);
                            TextView textViewNbPlacesTotales = (TextView)findViewById(R.id.textViewAffichageNbPlacesTotales);
                            textViewNbPlacesTotales.setText(" "+capAccueilLieu);
                            TextView textViewNbPlacesDisponibles = (TextView)findViewById(R.id.textViewAffichageNbPlacesDisponibles);
                            textViewNbPlacesDisponibles.setText(" "+nbPlacesDisponibles);
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

        Button buttonBackHome = (Button) findViewById(R.id.buttonBackHome);
        buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Manager.isConnecte()){
            LinearLayout layout_representation_connecte = findViewById(R.id.layout_representation_connecte);
            layout_representation_connecte.addView(Functions.textViewClientConnecte(this), 0);


            LinearLayout linearLayoutPlacesVoulues = new LinearLayout(this);
            linearLayoutPlacesVoulues.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutPlacesVoulues.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textViewPlacesVoulues = new TextView(this);
            textViewPlacesVoulues.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViewPlacesVoulues.setText(R.string.textViewNbPlacesVoulues);

            linearLayoutPlacesVoulues.addView(textViewPlacesVoulues);

            final EditText editTextPlacesVoulues = new EditText(this);
            editTextPlacesVoulues.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            editTextPlacesVoulues.setInputType(InputType.TYPE_CLASS_NUMBER);
            linearLayoutPlacesVoulues.addView(editTextPlacesVoulues);

            LinearLayout layout_representation = findViewById(R.id.layout_representation);
            layout_representation.addView(linearLayoutPlacesVoulues, 8);

            Button buttonReservation = new Button(this);
            buttonReservation.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            buttonReservation.setText(R.string.buttonReservation);
            layout_representation.addView(buttonReservation, 9);

            buttonReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(editTextPlacesVoulues.getText().toString()) <= Integer.parseInt(nbPlacesDisponibles)) {
                        RequestBody formBody = new FormBody.Builder().add("idRepresentation", idRepresentation).add("idClient", String.valueOf(Manager.getId())).add("nbPlaces", editTextPlacesVoulues.getText().toString()).build();
                        Request myGetRequestnbRepresentations = new Request.Builder().url("http://anthonypineau.alwaysdata.net/insertReservation").post(formBody).build();
                        Http.getInstance().newCall(myGetRequestnbRepresentations).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                final String body = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(body);
                                            String reserv = jsonObject.getString("return");
                                            if (reserv.equals("true")) {
                                                Toast.makeText(getApplicationContext(), "Réservation effectuée", Toast.LENGTH_LONG).show();
                                                recreate();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "La réservation n'a pas pu être effectuée", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (final JSONException e) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "La réservation n'a pas pu être effectuée, le nombre de places demandées doit être inférieur au nombre de places disponibles", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
