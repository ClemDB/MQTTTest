package com.example.mqtttest;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapterlist extends RecyclerView.Adapter<Adapterlist.MonviewHolder>{
    @NonNull
    @Override
    public Adapterlist.MonviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapterlist.MonviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MonviewHolder extends RecyclerView.ViewHolder {
        public MonviewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
