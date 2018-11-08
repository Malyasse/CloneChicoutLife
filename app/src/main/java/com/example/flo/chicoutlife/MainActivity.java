package com.example.flo.chicoutlife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonConnect = findViewById(R.id.connecter);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentAccueil = new Intent(MainActivity.this, ToDoListActivity.class);
                startActivity(intentAccueil);
                finish();

            }
        });

        final Button buttonInscription = findViewById(R.id.inscrire);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentAccueil = new Intent(MainActivity.this, RenseignementActivity.class);
                startActivity(intentAccueil);
                finish();
            }
        });
    }


    //create the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_goBack:
                Intent intentAccueil = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intentAccueil);
                finish();
                return true;

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