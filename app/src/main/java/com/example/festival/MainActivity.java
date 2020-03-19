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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> listeRepresentations = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        int longueur=0;

        InputStream inputStream2 = getResources().openRawResource(R.raw.api);
        try {
            if (inputStream2 != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream2));

                String str;
                StringBuilder buf = new StringBuilder();

                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\r\n");
                }
                reader.close();
                inputStream2.close();

                try {
                    JSONObject jsonObj = new JSONObject(buf.toString());
                    JSONArray representations = jsonObj.getJSONArray("representations");

                    longueur = representations.length();
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        } catch (java.io.FileNotFoundException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG);
        } catch (IOException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG);
        }

        final String [] col1 = new String[longueur+1];
        col1[0]="Lieu";
        final String [] col2 = new String[longueur+1];
        col2[0]="Groupe";
        final String [] col3 = new String[longueur+1];
        col3[0]="Date";
        final String [] col4 = new String[longueur+1];
        col4[0]="Heure début";
        final String [] col5 = new String[longueur+1];
        col5[0]="Heure fin";

        InputStream inputStream = getResources().openRawResource(R.raw.api);
        try {
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String str;
                StringBuilder buf = new StringBuilder();

                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\r\n");
                }
                reader.close();
                inputStream.close();

                try {
                    JSONObject jsonObj = new JSONObject(buf.toString());
                    JSONArray representations = jsonObj.getJSONArray("representations");

                    for (int i = 0; i < representations.length(); i++) {
                        JSONObject r = representations.getJSONObject(i);
                        String date = r.getString("date");
                        String heureDebut = r.getString("heureDebut");
                        String heureFin = r.getString("heureFin");

                        col3[i+1] = date;
                        col4[i+1] = heureDebut;
                        col5[i+1] = heureFin;

                        JSONObject lieu = r.getJSONObject("lieu");
                        String nomLieu = lieu.getString("nom");
                        col1[i+1] = nomLieu;

                        JSONObject groupe = r.getJSONObject("groupe");
                        String nomGroupe = groupe.getString("nom");
                        col2[i+1] = nomGroupe;
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
        } catch (java.io.FileNotFoundException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG);
        } catch (IOException e) {
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_LONG);
        }

        final TableLayout table = findViewById(R.id.idTable);
        TableRow row;
        TextView tv1,tv2, tv3, tv4, tv5;

        for(int i=0;i<col1.length;i++) {
            row = new TableRow(this);

            tv1 = new TextView(this);
            tv1.setText(col1[i]);
            tv1.setGravity(Gravity.CENTER);
            tv1.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
            tv1.setPadding(0,0,0,20);

            tv2 = new TextView(this);
            tv2.setText(col2[i]);
            tv2.setGravity(Gravity.CENTER);
            tv2.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
            tv2.setPadding(0,0,0,20);

            tv3 = new TextView(this);
            tv3.setText(col3[i]);
            tv3.setGravity(Gravity.CENTER);
            tv3.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
            tv3.setPadding(0,0,0,30);

            tv4 = new TextView(this);
            tv4.setText(col4[i]);
            tv4.setGravity(Gravity.CENTER);
            tv4.setLayoutParams( new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ) );
            tv4.setPadding(0,0,0,20);

            tv5 = new TextView(this);
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

                    InputStream inputStream = getResources().openRawResource(R.raw.api);
                    try {
                        if (inputStream != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                            String str;
                            StringBuilder buf = new StringBuilder();

                            while ((str = reader.readLine()) != null) {
                                buf.append(str + "\r\n");
                            }
                            reader.close();
                            inputStream.close();

                            try {
                                JSONObject jsonObj = new JSONObject(buf.toString());
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

                                result += date+" "+heureDebut+" "+heureFin+" "+nomLieu+" "+nomGroupe;
                            } catch (final JSONException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    } catch (java.io.FileNotFoundException e) {
                        Toast.makeText(MainActivity.this, "FileNotFoundException", Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this, "FileNotFoundException", Toast.LENGTH_LONG);
                    }

                    //Toast toast = Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG);
                    //toast.show();

                    Intent intent = new Intent(MainActivity.this, RepresentationActivity.class);
                    intent.putExtra("representation", representation);
                    intent.putExtra("lieu", lieu);
                    intent.putExtra("groupe", groupe);
                    startActivity(intent);
                }
            });
        }
        */

        OkHttpClient okHttpClient = new OkHttpClient();
        Request myGetRequest = new Request.Builder()
                .url("https://api.github.com/users/florent37")
                .build();//https://api.github.com/users/florent37 https://91.160.18.96:8443/apineau1/api/api.php https://api.androidhive.info/contacts/ http://localhost/api/api.php

        okHttpClient.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String text = response.body().string();
                final int statusCode = response.code();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView5 = (TextView) findViewById(R.id.textView5);
                        String message = null;

                        
                        textView5.setText(text); //message
                    }
                });
            }
        });

    }
}