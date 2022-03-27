package com.example.mqtttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MonviewHolder>{

    private List<Character> listCharacters;

    public AdapterList(List<Character> list) { listCharacters = list; };

    @NonNull
    @Override
    public AdapterList.MonviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.characters_layout, parent, false);
        return new MonviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterList.MonviewHolder holder, int position) {
        Character c = listCharacters.get(position);

        holder.tvName.setText("Name : " + c.name);
        holder.tvLevel.setText("Level : " + c.level);
        holder.tvHP.setText("HP : " + c.hp);
        holder.tvMP.setText("MP : " + c.mp);
        holder.tvMap.setText("Map : " + c.map);
        holder.tvCellule.setText("Cellule : " + c.cellule);
    }

    @Override
    public int getItemCount() {
        return listCharacters.size();
    }

    public class MonviewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvLevel, tvHP, tvMP, tvMap, tvCellule;

        public MonviewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvHolderName);
            tvLevel = view.findViewById(R.id.tvHolderLevel);
            tvHP = view.findViewById(R.id.tvHolderHP);
            tvMP = view.findViewById(R.id.tvHolderMP);
            tvMap = view.findViewById(R.id.tvHolderMap);
            tvCellule = view.findViewById(R.id.tvHolderCellule);
        }
    }
}
