package com.example.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.databinding.ActivityMainMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

public class MainMenu extends AppCompatActivity {

    TextView makeFlashcards;
    ArrayList<SetModel> sets;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);
        recyclerView = findViewById(R.id.sets_recyclerview);
        makeFlashcards = findViewById(R.id.makeFlashcards);
        mAuth = FirebaseAuth.getInstance();
        sets = new ArrayList<>();



        makeFlashcards.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, MakeFlashcards.class);
                i.putExtra("id","");
                startActivity(i);
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getEmail().split("@")[0]);
        ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            sets.clear();
                                            for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                                                if (item_snapshot.child("name").getValue()==null || item_snapshot.child("id").getValue()==null) {
                                                    return;
                                                }
                                                //System.out.println(item_snapshot.child("name").getValue().toString());
                                                String currId = item_snapshot.child("id").getValue().toString();
                                                sets.add(new SetModel(item_snapshot.child("name").getValue().toString(),currId));
                                            }
                                            setAdapter();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
    }

    private void setAdapter() {
        YourSetsAdapter adapter = new YourSetsAdapter(sets);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setAdapter(adapter);
    }
}

