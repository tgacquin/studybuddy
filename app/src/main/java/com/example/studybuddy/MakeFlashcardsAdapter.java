package com.example.studybuddy;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.FlashcardModel;
import com.example.studybuddy.MakeFlashcards;
import com.example.studybuddy.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MakeFlashcardsAdapter extends RecyclerView.Adapter<MakeFlashcardsAdapter.MyViewHolder> {

    private ArrayList<FlashcardModel> flashcards;
    private TextChangeCallback callbackTerm;
    private TextChangeCallback callbackDefinition;

    public interface TextChangeCallback {
        void textChangedAt(int position, String text);
    }

    public MakeFlashcardsAdapter(ArrayList<FlashcardModel> flashcards,TextChangeCallback callbackTerm,
                                 TextChangeCallback callbackDefinition) {
        this.flashcards = flashcards;
        this.callbackTerm = callbackTerm;
        this.callbackDefinition = callbackDefinition;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView term;
        private TextView definition;

        public MyViewHolder(final View view) {
            super(view);
            term = view.findViewById(R.id.term);
            definition = view.findViewById(R.id.definition);
        }
    }

    @NonNull
    @Override
    public MakeFlashcardsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_toadd, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MakeFlashcardsAdapter.MyViewHolder holder, int position) {
        String term = flashcards.get(position).term;
        String definition = flashcards.get(position).definition;
        holder.term.setText(term);
        holder.definition.setText(definition);
        holder.term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")){
                    return;
                }
                flashcards.get(position).term = String.valueOf(s);
                callbackTerm.textChangedAt(position, String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.definition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")){
                    return;
                }
                flashcards.get(position).definition = String.valueOf(s);
                callbackDefinition.textChangedAt(position, String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}