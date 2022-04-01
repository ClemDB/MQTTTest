package com.example.mqtttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterListLeader extends RecyclerView.Adapter<AdapterListLeader.MonviewHolder>{

    private List<Leaderboard> leaderboardList;



    public AdapterListLeader(List<Leaderboard> list) { leaderboardList = list; };

    @NonNull
    @Override
    public AdapterListLeader.MonviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.layout_leader, parent, false);
        return new AdapterListLeader.MonviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterListLeader.MonviewHolder holder, int position) {
        Leaderboard l1 = leaderboardList.get(0);
        Leaderboard l2 = leaderboardList.get(1);
        Leaderboard l3 = leaderboardList.get(2);

        holder.tvnom1.setText(l1.getNom());
        holder.tvnom2.setText(l2.getNom());
        holder.tvnom3.setText(l3.getNom());
        holder.tvcoup1.setText(String.valueOf(l1.getCoups()));
        holder.tvcoup2.setText(String.valueOf(l2.getCoups()));
        holder.tvcoup3.setText(String.valueOf(l3.getCoups()));
        holder.tvtemps1.setText(String.valueOf(l1.getTemps()));
        holder.tvtemps2.setText(String.valueOf(l2.getTemps()));
        holder.tvtemps3.setText(String.valueOf(l3.getTemps()));
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public class MonviewHolder extends RecyclerView.ViewHolder {

        TextView tvnom1, tvcoup1, tvtemps1, tvnom2, tvcoup2, tvtemps2, tvnom3, tvcoup3, tvtemps3;

        public MonviewHolder(View view) {
            super(view);

            tvnom1 = view.findViewById(R.id.tvChaLeader1);
            tvcoup1 = view.findViewById(R.id.tvCoupLeader1);
            tvtemps1 = view.findViewById(R.id.tvTempsLeader1);
            tvnom2 = view.findViewById(R.id.tvChaLeader2);
            tvcoup2 = view.findViewById(R.id.tvCoupLeader2);
            tvtemps2 = view.findViewById(R.id.tvTempsLeader2);
            tvnom3 = view.findViewById(R.id.tvChaLeader3);
            tvcoup3 = view.findViewById(R.id.tvCoupLeader3);
            tvtemps3 = view.findViewById(R.id.tvTempsLeader3);
        }
    }
}
