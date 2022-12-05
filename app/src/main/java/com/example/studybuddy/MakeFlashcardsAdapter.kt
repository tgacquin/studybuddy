package com.example.studybuddy

import com.example.studybuddy.FlashcardModel
import com.example.studybuddy.MakeFlashcardsAdapter.TextChangeCallback
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.studybuddy.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import java.util.ArrayList

class MakeFlashcardsAdapter(
    private val flashcards: ArrayList<FlashcardModel>, private val callbackTerm: TextChangeCallback,
    private val callbackDefinition: TextChangeCallback
) : RecyclerView.Adapter<MakeFlashcardsAdapter.MyViewHolder>() {
    interface TextChangeCallback {
        fun textChangedAt(position: Int, text: String?)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val term: TextView
        val definition: TextView

        init {
            term = view.findViewById(R.id.term)
            definition = view.findViewById(R.id.definition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.flashcard_toadd, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val term = flashcards[position].term
        val definition = flashcards[position].definition
        holder.term.text = term
        holder.definition.text = definition
        holder.term.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s == "") {
                    return
                }
                flashcards[position].term = s.toString()
                callbackTerm.textChangedAt(position, s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        holder.definition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s == "") {
                    return
                }
                flashcards[position].definition = s.toString()
                callbackDefinition.textChangedAt(position, s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }
}