package com.example.easypark.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easypark.R;

import java.util.ArrayList;

public class AvisAdapter extends RecyclerView.Adapter<AvisAdapter.MyViewHolder> {
    private ArrayList<Avis> allAvis;

    public AvisAdapter (ArrayList<Avis> allAvis){
        this.allAvis= allAvis;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtv_avis_value;
        TextView txtv_date_value;
        TextView txtv_user_value;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtv_avis_value = itemView.findViewById(R.id.txtv_avis_value);
            txtv_date_value = itemView.findViewById(R.id.txtv_date_value);
            txtv_user_value = itemView.findViewById(R.id.txtv_user_value);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.avis_layout, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_avis_value.setText(allAvis.get(position).getContent());
        holder.txtv_date_value.setText(allAvis.get(position).getDate());
        holder.txtv_user_value.setText(allAvis.get(position).getUser());
    }

    @Override
    public int getItemCount() {
        return allAvis.size();
    }

}
