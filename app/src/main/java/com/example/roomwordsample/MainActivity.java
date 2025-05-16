package com.example.roomwordsample;

import android.content.Intent;
import android.os.Bundle;

import com.example.roomwordsample.adapter.WordListAdapter;
import com.example.roomwordsample.model.Word;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;


import com.example.roomwordsample.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements WordListAdapter.WordItemClickListener {

    private ActivityMainBinding binding;
    private WordViewModel mWordViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WordListAdapter mAdapter = new WordListAdapter(this);
        binding.contentMain.recyclerview.setAdapter(mAdapter);
        binding.contentMain.recyclerview.setHasFixedSize(true);

        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, words -> {
            mAdapter.setWords(words);
        });

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.insert(word);
        } else if (requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditWordActivity.EXTRA_REPLY_ID, -1);
            if (id != -1) {
                String wordText = data.getStringExtra(EditWordActivity.EXTRA_REPLY_TEXT);
                Word word = new Word(wordText);
                word.setId(id);
                mWordViewModel.update(word);
                Toast.makeText(
                        getApplicationContext(),
                        R.string.word_updated,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleteClick(Word word) {
        mWordViewModel.delete(word);
        Toast.makeText(
                getApplicationContext(),
                R.string.word_deleted,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEditClick(Word word) {
        Intent intent = new Intent(MainActivity.this, EditWordActivity.class);
        intent.putExtra(EditWordActivity.EXTRA_WORD_ID, word.getId());
        intent.putExtra(EditWordActivity.EXTRA_WORD_TEXT, word.getWord());
        startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
    }
}