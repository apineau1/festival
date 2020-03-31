package com.example.festival;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private int nbRepresentations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final OkHttpClient okHttpClient = new OkHttpClient();

        Request myGetRequestnbRepresentations = new Request.Builder().url("http://192.168.1.66/api/nbRepresentations.php").build();

        okHttpClient.newCall(myGetRequestnbRepresentations).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String textNbRepresentations = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObjnbRepresentations = new JSONObject(textNbRepresentations);
                            nbRepresentations = jsonObjnbRepresentations.getInt("nbRepresentations");
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

        Request myGetRequest = new Request.Builder().url("http://192.168.1.66/api/api.php").build();

        okHttpClient.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String text = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            final JSONObject jsonObj = new JSONObject(text);

                            final String [] col1 = new String[nbRepresentations+1];
                            col1[0]="Lieu";
                            final String [] col2 = new String[nbRepresentations+1];
                            col2[0]="Groupe";
                            final String [] col3 = new String[nbRepresentations+1];
                            col3[0]="Date";
                            final String [] col4 = new String[nbRepresentations+1];
                            col4[0]="Heure début";
                            final String [] col5 = new String[nbRepresentations+1];
                            col5[0]="Heure fin";

                            JSONArray representations = jsonObj.getJSONArray("representations");

                            for (int i=1; i < representations.length(); i++) {
                                JSONObject r = representations.getJSONObject(i);
                                String date = r.getString("date");
                                String heureDebut = r.getString("heureDebut");
                                String heureFin = r.getString("heureFin");

                                col3[i] = date;
                                col4[i] = heureDebut;
                                col5[i] = heureFin;

                                JSONObject lieu = r.getJSONObject("lieu");
                                String nomLieu = lieu.getString("nom");
                                col1[i] = nomLieu;

                                JSONObject groupe = r.getJSONObject("groupe");
                                String nomGroupe = groupe.getString("nom");
                                col2[i] = nomGroupe;
                            }

                            final TableLayout table = findViewById(R.id.idTable);
                            TableRow row;
                            TextView tv1,tv2, tv3, tv4, tv5;

                            for(int i=0;i<col1.length;i++) {
                                row = new TableRow(MainActivity.this);

                                tv1 = new TextView(MainActivity.this);
                                tv1.setText(col1[i]);
                                tv1.setGravity(Gravity.CENTER);
                                tv1.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                                tv1.setPadding(0,0,0,20);

                                tv2 = new TextView(MainActivity.this);
                                tv2.setText(col2[i]);
                                tv2.setGravity(Gravity.CENTER);
                                tv2.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                                tv2.setPadding(0,0,0,20);

                                tv3 = new TextView(MainActivity.this);
                                tv3.setText(col3[i]);
                                tv3.setGravity(Gravity.CENTER);
                                tv3.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                                tv3.setPadding(0,0,0,30);

                                tv4 = new TextView(MainActivity.this);
                                tv4.setText(col4[i]);
                                tv4.setGravity(Gravity.CENTER);
                                tv4.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                                tv4.setPadding(0,0,0,20);

                                tv5 = new TextView(MainActivity.this);
                                tv5.setText(col5[i]);
                                tv5.setGravity(Gravity.CENTER);
                                tv5.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
                                tv5.setPadding(0,0,0,20);

                                row.addView(tv1);
                                row.addView(tv2);
                                row.addView(tv3);
                                row.addView(tv4);
                                row.addView(tv5);

                                row.setClickable(true);

                                table.addView(row);

                                row.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        TableRow tablerow = (TableRow) view;
                                        TextView sample = (TextView) tablerow.getChildAt(1);
                                        String result="";//=sample.getText().toString();

                                        int index = table.indexOfChild(tablerow)-1;

                                        String[] representation= new String[4];
                                        String[] lieu= new String[4];
                                        String[] groupe= new String[6];
                                        try{
                                            JSONArray representations = jsonObj.getJSONArray("representations");

                                            JSONObject r = representations.getJSONObject(index);
                                            String id = r.getString("id");
                                            String date = r.getString("date");
                                            String heureDebut = r.getString("heureDebut");
                                            String heureFin = r.getString("heureFin");

                                            representation[0]=id;
                                            representation[1]=date;
                                            representation[2]=heureDebut;
                                            representation[3]=heureFin;

                                            JSONObject lieuJson = r.getJSONObject("lieu");
                                            String idLieu = lieuJson.getString("id");
                                            String nomLieu = lieuJson.getString("nom");
                                            String adresseLieu = lieuJson.getString("adresseLieu");
                                            String capAccueilLieu = lieuJson.getString("capAccueil");

                                            lieu[0]=idLieu;
                                            lieu[1]=nomLieu;
                                            lieu[2]=adresseLieu;
                                            lieu[3]=capAccueilLieu;

                                            JSONObject groupeJson = r.getJSONObject("groupe");
                                            String idGroupe = groupeJson.getString("nom");
                                            String nomGroupe = groupeJson.getString("nom");
                                            String identiteResponsableGroupe = groupeJson.getString("nom");
                                            String adresseGroupe = groupeJson.getString("nom");
                                            String nombrePersonnesGroupe = groupeJson.getString("nom");
                                            String nomPaysGroupe = groupeJson.getString("nom");

                                            groupe[0]=idGroupe;
                                            groupe[1]=nomGroupe;
                                            groupe[2]=identiteResponsableGroupe;
                                            groupe[3]=adresseGroupe;
                                            groupe[4]=nombrePersonnesGroupe;
                                            groupe[5]=nomPaysGroupe;
                                    } catch (final JSONException e) {
                                        Log.e("message d'erreur", "Json parsing error: " + e.getMessage());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    Intent intent = new Intent(MainActivity.this, RepresentationActivity.class);
                                    intent.putExtra("representation", representation);
                                    intent.putExtra("lieu", lieu);
                                    intent.putExtra("groupe", groupe);
                                    startActivity(intent);
                                    }
                                });
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
}