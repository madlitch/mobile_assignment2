package com.massimo.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Note> notes;

    Adapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteCardTitle, noteCardContent;
        ConstraintLayout noteCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCard = itemView.findViewById(R.id.noteCard);
            noteCardTitle = itemView.findViewById(R.id.noteCardTitle);
            noteCardContent = itemView.findViewById(R.id.noteCardContent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.note_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title = notes.get(i).getTitle();
        String details = notes.get(i).getContent();

        viewHolder.noteCardTitle.setText(title);
        viewHolder.noteCardContent.setText(details);

        int backgroundColor = notes.get(i).getColor();
        viewHolder.noteCard.setBackgroundColor(backgroundColor);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setData(List<Note> newData) {
        notes.clear();
        notes.addAll(newData);
        notifyDataSetChanged();
    }
}
