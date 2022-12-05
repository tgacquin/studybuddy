package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.studybuddy.FlashcardModel
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.studybuddy.R
import android.content.Intent
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class YourFlashcardsView : AppCompatActivity() {
    lateinit var cardContent: TextView
    lateinit var flashcards: ArrayList<FlashcardModel>
    lateinit var rightArrow: ImageButton
    lateinit var leftArrow: ImageButton
    lateinit var setName: TextView
    lateinit var progress: TextView
    lateinit var mAuth: FirebaseAuth
    var showTerm = false
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_your_flashcards_view)
        i = 0
        showTerm = true
        cardContent = findViewById(R.id.cardcontent)
        leftArrow = findViewById(R.id.leftarrow)
        rightArrow = findViewById(R.id.rightarrow)
        setName = findViewById(R.id.viewSetName)
        flashcards = ArrayList()
        progress = findViewById(R.id.progress)
        mAuth = FirebaseAuth.getInstance()
        val intent = intent
        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")
        setName.setText(name)


        //Replace with data later
        val ref = FirebaseDatabase.getInstance().reference.child(
            mAuth!!.currentUser!!.email!!
                .split("@").toTypedArray()[0]
        ).child(id!!).child("flashcards")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item_snapshot in dataSnapshot.children) {
                    val term = item_snapshot.child("term").value.toString()
                    val definition = item_snapshot.child("definition").value.toString()
                    flashcards!!.add(FlashcardModel(term, definition))
                }
                cardContent.setText(flashcards!![0].term)
                progress.setText(1.toString() + "/" + flashcards!!.size)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        rightArrow.setOnClickListener(View.OnClickListener {
            i++
            i %= flashcards!!.size
            cardContent.setText(flashcards!![i].term)
            progress.setText(i.toString() + 1.toString() + "/" + flashcards!!.size)
        })
        leftArrow.setOnClickListener(View.OnClickListener {
            i--
            if (i == -1) {
                i = flashcards!!.size - 1
            }
            progress.setText(i.toString() + 1.toString() + "/" + flashcards!!.size)
            cardContent.setText(flashcards!![i].term)
        })
        cardContent.setOnClickListener(View.OnClickListener {
            showTerm = !showTerm
            if (showTerm == false) {
                cardContent.setText(flashcards!![i].definition)
            } else {
                cardContent.setText(flashcards!![i].term)
            }
        })
    }
}