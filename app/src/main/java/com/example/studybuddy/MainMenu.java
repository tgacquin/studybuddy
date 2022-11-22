package com.example.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.studybuddy.databinding.ActivityMainMenuBinding;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

public class MainMenu extends AppCompatActivity {

    Button makeFlashcards;
    Button yourFlashcards;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);

        makeFlashcards = findViewById(R.id.makeFlashcards);
        yourFlashcards = findViewById(R.id.yourFlashcards);

        makeFlashcards.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, MakeFlashcards.class);
                startActivity(i);
            }
        });

        yourFlashcards.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, YourFlashcardsList.class);
                startActivity(i);
            }
        });


    }
}

