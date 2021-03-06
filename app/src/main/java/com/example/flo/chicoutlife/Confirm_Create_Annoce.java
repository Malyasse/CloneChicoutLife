package com.example.flo.chicoutlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Confirm_Create_Annoce extends AppCompatActivity {
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_creat_annonce);


        // Bouton publier nouvelle annonce
        final Button buttonNewAnnonce = findViewById(R.id.publier_annonce);
        buttonNewAnnonce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentNewAnnonce = new Intent(Confirm_Create_Annoce.this, CreateAnnonce.class);
                startActivity(intentNewAnnonce);
                finish();
            }
        });

        // Bouton Acces annonces
        final Button buttonAnnonces = findViewById(R.id.all_annonces);
        buttonAnnonces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAnnonces = new Intent(Confirm_Create_Annoce.this,AnnoncesAccesLibre.class);
                startActivity(intentAnnonces);
                finish();
            }
        });


        // Button logout
        Button logout = (Button) findViewById(R.id.signout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

    }
    @Override
    //create the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_quit:
                finish();
                System.exit(0);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
