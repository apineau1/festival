package com.example.festival;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RepresentationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representation);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String [] representation = getIntent().getStringArrayExtra("representation");
        String [] lieu = getIntent().getStringArrayExtra("lieu");
        String [] groupe = getIntent().getStringArrayExtra("groupe");

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

        reserver();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reserver();
    }

    private void reserver(){
        try{
            Manager m = Manager.getInstance();
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
                    //Intent intent = new  Intent(RepresentationActivity.this, ConnexionActivity.class);
                    //startActivity(intent);
                    EditText editTextNbPlacesVoulues = findViewById(R.id.editTextNbPlacesVoulues);
                    Toast.makeText(getApplicationContext(), editTextNbPlacesVoulues.getText(),Toast.LENGTH_LONG).show();
                }
            });
        }catch(Exception e){

        }
    }
}
