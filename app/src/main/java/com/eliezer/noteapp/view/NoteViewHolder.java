package com.eliezer.noteapp.view;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eliezer.noteapp.R;

class NoteViewHolder extends RecyclerView.ViewHolder {
    private final TextView noteViewItem;

    private NoteViewHolder(View itemView) {
        super(itemView);
        noteViewItem = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        noteViewItem.setText(text);
    }

    static NoteViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new NoteViewHolder(view);
    }
}
