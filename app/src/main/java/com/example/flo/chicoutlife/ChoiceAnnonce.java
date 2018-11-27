package com.example.flo.chicoutlife;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoiceAnnonce extends Activity {

    RecyclerView recyclerView;
    VerticalAdapter verticalAdapter;
    private List<ModelAnnonce> modelsAnnonces;

    FirebaseDatabase database;
    DatabaseReference annonces;
    FirebaseStorage storage ;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choix_annonces);
        database = FirebaseDatabase.getInstance();
        storage =  FirebaseStorage.getInstance();

        //TODO faire en recevant des parametres cocher selon type si non general
        recyclerView = (RecyclerView) findViewById(R.id.linearMiniaturesAnnonces);

        modelsAnnonces = fillWithAnnonce();



    }

    public  List<ModelAnnonce> fillWithAnnonce(){

        annonces = database.getReference("Annonces");
        annonces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot annonce : dataSnapshot.getChildren()){

                    modelsAnnonces = new ArrayList<>();
                    verticalAdapter = new VerticalAdapter(modelsAnnonces,getApplicationContext());

                    String cheminImg = (String)annonce.child("Image").getValue();
                    mStorageRef =  storage.getReference();
                    StorageReference imageRef = mStorageRef.child(cheminImg);


                    final long ONE_MEGABYTE = 4096 * 4096;
                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            /*Recuperation des donnes textes dans la base de donnee*/
                            String modelTitre = (String) annonce.child("Titre").getValue();
                            String modelDescription = (String) annonce.child("Description").getValue();

                            /*Recuperation du chemin de l'annonce pour la redirection lors du OnClick*/
                            String cheminAnnonceBdd = annonce.getKey();
                            Bitmap imageAnnonce =null;

                            /*Recuperation de l'image dans la base de donnee*/
                            imageAnnonce = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageAnnonce = Bitmap.createScaledBitmap(imageAnnonce,360,240,false);
                            ModelAnnonce model = new ModelAnnonce(modelTitre, modelDescription,imageAnnonce,cheminAnnonceBdd);
                            modelsAnnonces.add(model);

                            /*Sert pour le recyclerView , sinon la taille est initialiser a zero et le recycler ne se remplit pas*/
                            verticalAdapter.notifyDataSetChanged();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.e("passage","failure dans choiceAnnonce " + exception.getMessage());
                        }
                    });


                }
                Log.d("passage","taille din datachange" + modelsAnnonces.size());

                LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(verticalLayoutManager);
                recyclerView.setAdapter(verticalAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //TODO remplir avec bdd realtime et storage
        return modelsAnnonces;
    }

    public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder>{

        List<ModelAnnonce> verticalList ;
        Context context;

        public VerticalAdapter(List<ModelAnnonce> verticalList,Context context){
            this.verticalList = verticalList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return verticalList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView imageViewAnnonce;
            TextView titre;
            TextView description;

            public MyViewHolder(View view){
                super(view);
                 /*Lier avec les view du xml*/
                imageViewAnnonce =(ImageView) view.findViewById(R.id.imageMiniatureAnnonce);
                titre = (TextView) view.findViewById(R.id.titreMiniatureAnnonce);
                description = (TextView) view.findViewById(R.id.descriptionMiniatureAnnonce);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("passage","est onCreateViewHolder");
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_miniature_annonce,parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            /*Set des donnes sur le holder*/
            holder.imageViewAnnonce.setImageBitmap(verticalList.get(position).getImageAnnonce());
            holder.titre.setText(verticalList.get(position).getModelTitre());
            holder.description.setText(verticalList.get(position).getModelDescription());

            /*Ajout du OnClick pour rediriger vers l'annonce*/
            final ModelAnnonce min = verticalList.get(position);
            holder.imageViewAnnonce.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context ,Annonce.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//sinon erreur
                    intent.putExtra("CHEMIN_ANNONCE",verticalList.get(position).getCheminAnnonceBdd());
                   // intent.putExtra("NOM_PAGE", min.getCheminAnnonceBdd());
                    context.startActivity(intent);

                    }
            }
            );

        }
    }

    @Override
    //create the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_goHome:
                Intent intentAccueil = new Intent(ChoiceAnnonce.this, Home_screen.class);
                startActivity(intentAccueil);
                finish();
                return true;
            case R.id.action_goBack:
                Intent intentRetour = new Intent(ChoiceAnnonce.this, ToDoListActivity.class); // TODO
                startActivity(intentRetour);
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
