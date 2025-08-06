package com.example.projecthomework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecthomework.databinding.RecyclerRowBinding;
import com.example.projecthomework.log.Explore;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CommentHolder> {

    ArrayList<Comment> arrayList;

    public Adapter(ArrayList<Comment> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Adapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.view.setText(arrayList.get(position).comment);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(holder.itemView.getContext(), Explore.class);
                intent.putExtra("comment",arrayList.get(position));
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;

        public CommentHolder( RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding =  recyclerRowBinding;
        }
    }



}


