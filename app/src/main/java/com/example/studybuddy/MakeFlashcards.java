package com.example.studybuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import kotlin.Metadata;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 7, 1},
        k = 1,
        d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0014R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\r"},
        d2 = {"Lcom/example/studybuddy/MakeFlashcards;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/studybuddy/MakeFlashcardsAdapter;", "getAdapter", "()Lcom/example/studybuddy/MakeFlashcardsAdapter;", "setAdapter", "(Lcom/example/studybuddy/MakeFlashcardsAdapter;)V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "StudyBuddy.app.main"}
)
public final class MakeFlashcards extends AppCompatActivity {
    @Nullable
    private MakeFlashcardsAdapter adapter;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_make_flashcards);
    }
}
