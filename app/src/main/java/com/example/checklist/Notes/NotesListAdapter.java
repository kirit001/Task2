package com.example.checklist.Notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.Database.NotesDatabase.NotesEntity;
import com.example.checklist.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteslistViewholder> {

    OnDataClickListener onDataClickListener;
    private final Context context;
    private final List<NotesEntity> data;

    public NotesListAdapter(Context context, List<NotesEntity> data) {
        this.context = context;
        this.data = data;
        this.onDataClickListener = (OnDataClickListener) context;
    }

    @NonNull
    @Override
    public NoteslistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_noteslist, parent, false);
        return new NoteslistViewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteslistViewholder holder, int position) {
        holder.nameview.setText(data.get(position).getTitle());
        holder.idview.setText(data.get(position).getId().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface OnDataClickListener {
        void onClick(NotesEntity notesEntity);
    }

    public class NoteslistViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameview;
        TextView idview;

        public NoteslistViewholder(@NonNull View itemView) {
            super(itemView);
            nameview = itemView.findViewById(R.id.titleView);
            idview = itemView.findViewById(R.id.idView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDataClickListener.onClick(data.get(getAdapterPosition()));
        }
    }
}