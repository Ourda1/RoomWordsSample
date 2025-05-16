package com.example.roomwordsample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditWordActivity extends AppCompatActivity {
    public static final String EXTRA_WORD_ID = "WORD_ID";
    public static final String EXTRA_WORD_TEXT = "WORD_TEXT";
    public static final String EXTRA_REPLY_ID = "REPLY_ID";
    public static final String EXTRA_REPLY_TEXT = "REPLY_TEXT";

    private EditText mEditWordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        mEditWordView = findViewById(R.id.edit_word);

        // Récupérer les données du mot à modifier
        final int wordId = getIntent().getIntExtra(EXTRA_WORD_ID, -1);
        String wordText = getIntent().getStringExtra(EXTRA_WORD_TEXT);

        // Remplir le champ avec le texte actuel
        mEditWordView.setText(wordText);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = mEditWordView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY_ID, wordId);
                replyIntent.putExtra(EXTRA_REPLY_TEXT, word);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}
