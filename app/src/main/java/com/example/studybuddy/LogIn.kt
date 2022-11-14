package com.example.studybuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studybuddy.databinding.ActivityMainMenuBinding
import com.example.studybuddy.databinding.LoginBinding.inflate

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflate(layoutInflater)
        setContentView(binding.root)



        binding.signInBtn.setOnClickListener() {
            val intent = Intent(this,MainMenu::class.java)
            startActivity(intent)
        }
    }
}