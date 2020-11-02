package com.eliezer.noteapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.eliezer.noteapp.R;

public class NewNoteActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.android.kukuwashere.REPLY";

    private EditText mEditNoteView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note_acivity);
        mEditNoteView = findViewById(R.id.edit_note);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditNoteView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = mEditNoteView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }}