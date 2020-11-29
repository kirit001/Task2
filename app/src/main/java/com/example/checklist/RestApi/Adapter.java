package com.example.checklist.RestApi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.checklist.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Model.Data> listItems;
    private Context context;

    public Adapter(Context context, ArrayList<Model.Data> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model.Data listitems = listItems.get(position);

        holder.id.setText(""+listitems.getId());
        holder.email.setText(listitems.getEmail());
        holder.first_name.setText(listitems.getFirst_name());
        holder.last_name.setText(listitems.getLast_name());

        Glide.with(context).load(listitems.getAvatar()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                for (Throwable throwable : e.getRootCauses()){
                    Log.d("supreme",throwable.getLocalizedMessage());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(holder.avatar);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView id;
        public TextView email;
        public TextView first_name;
        public TextView last_name;
        public ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.apiid);
            email = itemView.findViewById(R.id.apiemail);
            first_name = itemView.findViewById(R.id.apifirst_name);
            last_name = itemView.findViewById(R.id.apilast_name);
            avatar = itemView.findViewById(R.id.apiavatar);
        }
    }
}
