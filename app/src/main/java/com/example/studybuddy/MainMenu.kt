package com.example.studybuddy

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.studybuddy.SetModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.studybuddy.R
import android.content.Intent
import android.view.View
import com.example.studybuddy.MakeFlashcards
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.studybuddy.YourSetsAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import java.util.ArrayList

class MainMenu : AppCompatActivity() {
    lateinit var makeFlashcards: TextView
    lateinit var sets: ArrayList<SetModel>
    var recyclerView: RecyclerView? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main_menu)
        recyclerView = findViewById(R.id.sets_recyclerview)
        makeFlashcards = findViewById(R.id.makeFlashcards)
        mAuth = FirebaseAuth.getInstance()
        sets = ArrayList()
        makeFlashcards.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MainMenu, MakeFlashcards::class.java)
            i.putExtra("id", "")
            startActivity(i)
        })
        val ref = FirebaseDatabase.getInstance().reference.child(
            mAuth!!.currentUser!!.email!!.split("@").toTypedArray()[0]
        )
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                sets!!.clear()
                for (item_snapshot in dataSnapshot.children) {
                    if (item_snapshot.child("name").value == null || item_snapshot.child("id").value == null) {
                        return
                    }
                    //System.out.println(item_snapshot.child("name").getValue().toString());
                    val currId = item_snapshot.child("id").value.toString()
                    sets!!.add(SetModel(item_snapshot.child("name").value.toString(), currId))
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setAdapter() {
        val adapter = YourSetsAdapter(sets)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter
    }
}