package com.example.studybuddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.MakeFlashcardsAdapter.TextChangeCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MakeFlashcards : AppCompatActivity() {
    lateinit var adapter: MakeFlashcardsAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var addCard: TextView
    lateinit var saveSet: TextView
    lateinit var setName: TextView
    lateinit var flashcards: ArrayList<FlashcardModel>
    lateinit var mAuth: FirebaseAuth
    var firstTime = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_make_flashcards)
        mAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.makeRecyclerView)
        addCard = findViewById(R.id.makeAddCard)
        saveSet = findViewById(R.id.makeSave)
        setName = findViewById(R.id.makeSetName)
        val setNameString = setName.getText().toString()
        val id: String
        val i = intent
        flashcards = ArrayList()
        if (i.getStringExtra("id") == "") {
            val r = Random()
            id = r.nextInt(999999).toString()
            flashcards!!.add(FlashcardModel("", ""))
        } else {
            id = i.getStringExtra("id").toString()
            setName.setText(i.getStringExtra("name"))
            val ref = FirebaseDatabase.getInstance().reference.child(
                mAuth!!.currentUser!!.email!!
                    .split("@").toTypedArray()[0]
            ).child(id).child("flashcards")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (firstTime == true) {
                        for (item_snapshot in dataSnapshot.children) {
                            val term = item_snapshot.child("term").value.toString()
                            val definition = item_snapshot.child("definition").value.toString()
                            flashcards!!.add(FlashcardModel(term, definition))
                        }
                        setAdapter()
                        firstTime = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }


        //Get data for the arraylist later
        setAdapter()
        addCard.setOnClickListener(View.OnClickListener {
            flashcards!!.add(FlashcardModel("", ""))
            setAdapter()
        })
        saveSet.setOnClickListener(View.OnClickListener { //Send data to Firebase
            println(setName.text.toString())
            println(flashcards.contains(FlashcardModel("","")))

            if (setName.text.equals("")) {
                Toast.makeText(baseContext, "Give set a name", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (flashcards.contains(FlashcardModel("",""))) {
                Toast.makeText(baseContext, "Give empty cards info", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            firstTime = false
            val rootRef = FirebaseDatabase.getInstance().reference
            val user = mAuth!!.currentUser!!.email!!.split("@").toTypedArray()[0]
            rootRef.child(user).child(id).child("flashcards").removeValue()
            rootRef.child(user).child(id).child("id").setValue(id)
            rootRef.child(user).child(id).child("name").setValue(setName.getText().toString())
            for (flashcard in flashcards!!) {
                val f = FlashcardModel(flashcard.term, flashcard.definition)
                rootRef.child(user).child(id).child("flashcards").push().setValue(f)
            }
            Toast.makeText(baseContext, "Set saved!", Toast.LENGTH_SHORT).show()
            val i = Intent(this@MakeFlashcards, MainMenu::class.java)
            startActivity(i)
        })
    }

    private fun setAdapter() {
        val termAdapter = object : TextChangeCallback {
            override fun textChangedAt(position: Int, text: String?) {
                flashcards[position].term = text!!
            }
        }

        val definitionAdapter = object : TextChangeCallback {
            override fun textChangedAt(position: Int, text: String?) {
                flashcards[position].definition = text!!
            }
        }

        val adapter = MakeFlashcardsAdapter(
            flashcards,termAdapter, definitionAdapter,
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
    }
}