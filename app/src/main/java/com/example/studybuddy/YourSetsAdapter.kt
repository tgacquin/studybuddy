package com.example.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.FlashcardModel;
import com.example.studybuddy.MakeFlashcards;
import com.example.studybuddy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class YourSetsAdapter extends RecyclerView.Adapter<YourSetsAdapter.MyViewHolder> {
    private ArrayList<SetModel> sets;
    private Context context;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public YourSetsAdapter(ArrayList<SetModel> sets) {
        this.sets = sets;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView edit;
        private ImageView delete;

        public MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.set_title);
            edit = view.findViewById(R.id.edit_btn);
            delete = view.findViewById(R.id.delete_btn);
        }
    }


    @NonNull
    @Override
    public YourSetsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_layout, parent, false);
        context = itemView.getContext();
        return new YourSetsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YourSetsAdapter.MyViewHolder holder, int position) {
        String name = sets.get(position).name;
        holder.name.setText(name);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mAuth.getCurrentUser().getEmail().split("@")[0];
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child(user).child(sets.get(position).id).removeValue();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MakeFlashcards.class);
                intent.putExtra("id",sets.get(position).id);
                intent.putExtra("name",sets.get(position).name);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YourFlashcardsView.class);
                // For passing values
                intent.putExtra("id",sets.get(position).id);
                intent.putExtra("name",sets.get(position).name);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sets.size();
    }



}
