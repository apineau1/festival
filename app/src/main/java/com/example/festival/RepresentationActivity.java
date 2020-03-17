package com.example.festival;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


        TextView textView = (TextView) findViewById(R.id.textViewAfficheGroupe);
        textView.setText(groupe[1]);
        TextView textViewLieu = (TextView) findViewById(R.id.textViewAfficheLieu);
        textViewLieu.setText(lieu[1]);
        TextView textViewDate = (TextView)findViewById(R.id.textViewAfficheDate);
        textViewDate.setText(representation[1]);
        TextView textViewHeureDeb = (TextView)findViewById(R.id.textViewAfficheHeureDeb);
        textViewHeureDeb.setText(representation[2]);
        TextView textViewHeureFin = (TextView)findViewById(R.id.textViewAfficheHeureFin);
        textViewHeureFin.setText(representation[3]);

    }
}
