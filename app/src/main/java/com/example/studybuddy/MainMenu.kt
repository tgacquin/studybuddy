package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studybuddy.databinding.ActivityMainMenuBinding.inflate


class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflate(layoutInflater)
        setContentView(binding.root)


        binding.makeFlashcards.setOnClickListener() {
            val intent = Intent(this,MakeFlashcards::class.java)
            startActivity(intent)
        }
        binding.yourFlashcards.setOnClickListener() {
            val intent = Intent(this,YourFlashcardsList::class.java)
            startActivity(intent)
        }
    }
}