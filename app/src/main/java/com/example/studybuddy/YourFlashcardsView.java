package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public final class YourFlashcardsView extends AppCompatActivity {
    TextView cardContent;
    ArrayList<FlashcardModel> flashcards;
    ImageButton rightArrow;
    ImageButton leftArrow;
    TextView setName;
    TextView progress;
    FirebaseAuth mAuth;
    boolean showTerm;
    int i;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_your_flashcards_view);
        i = 0;
        showTerm = true;
        cardContent = findViewById(R.id.cardcontent);
        leftArrow = findViewById(R.id.leftarrow);
        rightArrow = findViewById(R.id.rightarrow);
        setName = findViewById(R.id.viewSetName);
        flashcards = new ArrayList<FlashcardModel>();
        progress = findViewById(R.id.progress);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        setName.setText(name);


        //Replace with data later

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getEmail().split("@")[0]).child(id).child("flashcards");
        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                          for (DataSnapshot item_snapshot : dataSnapshot.getChildren()) {
                                              String term = item_snapshot.child("definition").getValue().toString();
                                              String definition = item_snapshot.child("term").getValue().toString();
                                              flashcards.add(new FlashcardModel(term, definition));
                                          }
                                          cardContent.setText(flashcards.get(0).term);
                                          progress.setText(1 + "/" + flashcards.size());
                                      }


                                      @Override
                                      public void onCancelled(@NonNull DatabaseError error) {
                                      }
                                  });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++; i%=flashcards.size();
                cardContent.setText(flashcards.get(i).term);
                progress.setText(i+1 + "/" + flashcards.size());
            }
        });


        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i--;
                if (i == -1) {
                    i = flashcards.size() - 1;
                }
                progress.setText(i+1 + "/" + flashcards.size());
                cardContent.setText(flashcards.get(i).term);

            }
        });

        cardContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerm = !showTerm;
                if (showTerm==false) {
                    cardContent.setText(flashcards.get(i).definition);
                } else {
                    cardContent.setText(flashcards.get(i).term);
                }
            }
        });
    }
}
