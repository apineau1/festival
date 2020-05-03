package com.example.festival;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private boolean connecte=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Request requestRepresentations = new Request.Builder().url("http://anthonypineau.alwaysdata.net/representations").build();
        Http.getInstance().newCall(requestRepresentations).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObj = new JSONObject(body);

                            JSONArray representations = jsonObj.getJSONArray("representations");
                            int longueur =  representations.length()+1;

                            final String [] col1 = new String[longueur];
                            col1[0]="Lieu";
                            final String [] col2 = new String[longueur];
                            col2[0]="Groupe";
                            final String [] col3 = new String[longueur];
                            col3[0]="Date";
                            final String [] col4 = new String[longueur];
                            col4[0]="Heure début";
                            final String [] col5 = new String[longueur];
                            col5[0]="Heure fin";

                            for (int i=0; i < representations.length(); i++) {
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

                                if(i==0){
                                    row.setClickable(false);
                                }else{
                                    row.setClickable(true);
                                    row.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View view) {
                                            TableRow tablerow = (TableRow) view;
                                            TextView sample = (TextView) tablerow.getChildAt(1);
                                            String result="";//=sample.getText().toString();

                                            int index = table.indexOfChild(tablerow)-1;
                                            String id="0";
                                            try{
                                                JSONArray representations = jsonObj.getJSONArray("representations");

                                                JSONObject r = representations.getJSONObject(index);
                                                id = r.getString("id");
                                            } catch (final JSONException e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                            Intent intent = new Intent(MainActivity.this, RepresentationActivity.class);
                                            intent.putExtra("representation", id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                table.addView(row);
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

        Button buttonMainConnexion = (Button) findViewById(R.id.buttonMainConnexion);
        buttonMainConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Manager.isConnecte() && !connecte){
            connecte=true;
            final Button buttonMainConnexion = (Button) findViewById(R.id.buttonMainConnexion);
            final LinearLayout linearLayout = findViewById(R.id.layout_main);
            linearLayout.removeView(buttonMainConnexion);

            final LinearLayout linearLayoutConnecte = new LinearLayout(this);
            linearLayoutConnecte.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutConnecte.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            linearLayoutConnecte.addView(Functions.textViewClientConnecte(this));

            Button buttonDeconnexion = new Button(this);
            buttonDeconnexion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            buttonDeconnexion.setText(R.string.buttonDeconnexion);
            linearLayoutConnecte.addView(buttonDeconnexion);

            buttonDeconnexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Manager.deconnexion();
                    linearLayout.removeView(linearLayoutConnecte);
                    linearLayout.addView(buttonMainConnexion, 0);
                    recreate();
                }
            });
            linearLayout.addView(linearLayoutConnecte, 0);
        }
    }
}