package com.example.festival;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RepresentationActivity extends AppCompatActivity {
    private String idRepresentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representation);

        String [] representation = getIntent().getStringArrayExtra("representation");
        String [] lieu = getIntent().getStringArrayExtra("lieu");
        String [] groupe = getIntent().getStringArrayExtra("groupe");

        idRepresentation=representation[0];

        TextView textViewGroupe = (TextView) findViewById(R.id.textViewAfficheGroupe);
        textViewGroupe.setText(" "+groupe[1]);
        TextView textViewLieu = (TextView) findViewById(R.id.textViewAfficheLieu);
        textViewLieu.setText(" "+lieu[1]);
        TextView textViewDate = (TextView)findViewById(R.id.textViewAfficheDate);
        textViewDate.setText(" "+representation[1]);
        TextView textViewHeureDeb = (TextView)findViewById(R.id.textViewAfficheHeureDeb);
        textViewHeureDeb.setText(" "+representation[2]);
        TextView textViewHeureFin = (TextView)findViewById(R.id.textViewAfficheHeureFin);
        textViewHeureFin.setText(" "+representation[3]);
        TextView textViewNbPlacesTotales = (TextView)findViewById(R.id.textViewAffichageNbPlacesTotales);
        textViewNbPlacesTotales.setText(" "+lieu[3]);
        TextView textViewNbPlacesDisponibles = (TextView)findViewById(R.id.textViewAffichageNbPlacesDisponibles);
        textViewNbPlacesDisponibles.setText(" "+representation[4]);

        Button buttonAuthentification = (Button) findViewById(R.id.buttonAuthentification);

        buttonAuthentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(RepresentationActivity.this, ConnexionActivity.class);
                startActivity(intent);

            }
        });

        Button buttonBackHome = (Button) findViewById(R.id.buttonBackHome);
        buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reserver();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reserver();
    }

    private void reserver(){
        try{
            final Manager m = Manager.getInstance();
            TextView textViewClient = (TextView)findViewById(R.id.textViewClient);
            textViewClient.setText(m.getNom()+" "+m.getPrenom());
            textViewClient.setVisibility(View.VISIBLE);

            Button buttonAuthentification = (Button) findViewById(R.id.buttonAuthentification);

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.placesVoulues);
            linearLayout.setVisibility(View.VISIBLE);

            buttonAuthentification.setText("Réserver");

            buttonAuthentification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextNbPlacesVoulues = findViewById(R.id.editTextNbPlacesVoulues);

                    Request myGetRequestnbRepresentations = new Request.Builder().url("http://192.168.1.66/api/insertReservation.php?idRepresentation="+idRepresentation+"&idClient="+m.getId()+"&nbPlaces="+editTextNbPlacesVoulues.getText()).build();

                    Http.getInstance().newCall(myGetRequestnbRepresentations).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) { }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String body = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(body);
                                        String reserv = jsonObject.getString("return");
                                        if(reserv=="true"){
                                            Toast.makeText(getApplicationContext(), "Réservation effectuée",Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "La réservation n'a pas pu être effectuée",Toast.LENGTH_LONG).show();
                                        }
                                    } catch (final JSONException e) {
                                        Log.e("message d'erreur", "Json parsing error: " + e.getMessage());
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
        }catch(Exception e){

        }
    }
}
