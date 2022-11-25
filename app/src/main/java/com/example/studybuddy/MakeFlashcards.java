package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public final class MakeFlashcards extends AppCompatActivity {

    private MakeFlashcardsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView addCard;
    private TextView saveSet;
    private TextView setName;
    ArrayList<FlashcardModel> flashcards;
    FirebaseAuth mAuth;
    boolean firstTime = true;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_make_flashcards);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.makeRecyclerView);
        addCard = findViewById(R.id.makeAddCard);
        saveSet = findViewById(R.id.makeSave);
        setName = findViewById(R.id.makeSetName);
        String setNameString = setName.getText().toString();
        String id;
        Intent i = getIntent();
        flashcards = new ArrayList<>();
        if (i.getStringExtra("id").equals("")) {
            Random r = new Random();
            id = String.valueOf(r.nextInt(999999));
            flashcards.add(new FlashcardModel("",""));
        } else {
            id = i.getStringExtra("id");
            setName.setText(i.getStringExtra("name"));
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getEmail().split("@")[0]).child(id).child("flashcards");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (firstTime==true) {
                        for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                            String term = item_snapshot.child("definition").getValue().toString();
                            String definition = item_snapshot.child("term").getValue().toString();
                            flashcards.add(new FlashcardModel(term, definition));

                        }
                        setAdapter();
                        firstTime = false;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }




        //Get data for the arraylist later
       setAdapter();



       addCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               flashcards.add(new FlashcardModel("",""));
               setAdapter();
           }
       });

       saveSet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Send data to Firebase
               firstTime = false;
               DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
               String user = mAuth.getCurrentUser().getEmail().split("@")[0];
               rootRef.child(user).child(id).child("flashcards").removeValue();
               rootRef.child(user).child(id).child("id").setValue(String.valueOf(id));
               rootRef.child(user).child(id).child("name").setValue(setName.getText().toString());
               for(FlashcardModel flashcard : flashcards) {
                   FlashcardModel f = new FlashcardModel(flashcard.term,flashcard.definition);
                   rootRef.child(user).child(id).child("flashcards").push().setValue(f);
               }
               Toast.makeText(getBaseContext(),"Set saved!",Toast.LENGTH_SHORT).show();
               Intent i = new Intent(MakeFlashcards.this,MainMenu.class);
               startActivity(i);
           }
       });
    }

    private void setAdapter() {
        MakeFlashcardsAdapter adapter = new MakeFlashcardsAdapter(flashcards,new MakeFlashcardsAdapter.TextChangeCallback() {
            @Override
            public void textChangedAt(int position, String text) {
                flashcards.get(position).term = text;
            }
        },new MakeFlashcardsAdapter.TextChangeCallback() {
            @Override
            public void textChangedAt(int position, String text) {
                flashcards.get(position).definition = text;
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
