package com.example.studybuddy

import android.content.Context
import com.example.studybuddy.SetModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import com.example.studybuddy.R
import android.view.ViewGroup
import android.view.LayoutInflater
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.example.studybuddy.MakeFlashcards
import com.example.studybuddy.YourFlashcardsView
import java.util.ArrayList

class YourSetsAdapter(private val sets: ArrayList<SetModel>) :
    RecyclerView.Adapter<YourSetsAdapter.MyViewHolder>() {
    private var context: Context? = null
    var mAuth = FirebaseAuth.getInstance()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val edit: ImageView
        val delete: ImageView

        init {
            name = view.findViewById(R.id.set_title)
            edit = view.findViewById(R.id.edit_btn)
            delete = view.findViewById(R.id.delete_btn)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.set_layout, parent, false)
        context = itemView.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = sets[position].name
        holder.name.text = name
        holder.delete.setOnClickListener {
            val user = mAuth.currentUser!!.email!!.split("@").toTypedArray()[0]
            val rootRef = FirebaseDatabase.getInstance().reference
            rootRef.child(user).child(sets[position].id).removeValue()
        }
        holder.edit.setOnClickListener {
            val intent = Intent(context, MakeFlashcards::class.java)
            intent.putExtra("id", sets[position].id)
            intent.putExtra("name", sets[position].name)
            context!!.startActivity(intent)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, YourFlashcardsView::class.java)
            // For passing values
            intent.putExtra("id", sets[position].id)
            intent.putExtra("name", sets[position].name)
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sets.size
    }
}