package com.example.roomwordsample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomwordsample.R;
import com.example.roomwordsample.model.Word;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private List<Word> mWords;
    private WordItemClickListener mClickListener;

    public interface WordItemClickListener {
        void onDeleteClick(Word word);
        void onEditClick(Word word);
    }

    public WordListAdapter(WordItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
            holder.itemView.setOnClickListener(v -> {
                showOptionsDialog(v.getContext(), current);
            });
        } else {
            // Couvre le cas où les données ne sont pas encore prêtes.
            holder.wordItemView.setText("No Word");
        }
    }

    // Afficher le dialogue avec options Modifier et Supprimer
    private void showOptionsDialog(Context context, Word word) {
        String[] options = {"Modifier", "Supprimer"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Modifier
                    if (mClickListener != null) {
                        mClickListener.onEditClick(word);
                    }
                    break;
                case 1: // Supprimer
                    if (mClickListener != null) {
                        mClickListener.onDeleteClick(word);
                    }
                    break;
            }
        });
        builder.show();
    }
    public void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() est appelé plusieurs fois, et lorsqu'il est appelé pour la première fois,
    // mWords n'a pas été mis à jour (cela signifie qu'initialement, il est nul, et nous ne pouvons pas retourner nul).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }
}