package com.example.easypark.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easypark.R;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {
    private ArrayList<Ticket> tickets;

    public TicketAdapter (ArrayList<Ticket> tickets){
        this.tickets= tickets;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtv_currentTicket_Date_value;
        TextView txtv_currentTicket_Location_value;
        TextView txtv_start_time_value;
        TextView txtv_end_time_value;
        TextView txtv_durationTitle_value;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtv_currentTicket_Date_value = itemView.findViewById(R.id.txtv_currentTicket_Date_value);
            txtv_currentTicket_Location_value = itemView.findViewById(R.id.txtv_currentTicket_Location_value);
            txtv_start_time_value = itemView.findViewById(R.id.txtv_start_time_value);
            txtv_end_time_value = itemView.findViewById(R.id.txtv_end_time_value);
            txtv_durationTitle_value = itemView.findViewById(R.id.txtv_durationTitle_value);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_layout, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtv_currentTicket_Date_value.setText(tickets.get(position).getDate());
        holder.txtv_currentTicket_Location_value.setText("inconnu");
        holder.txtv_start_time_value.setText(tickets.get(position).getHeureDebut());
        holder.txtv_end_time_value.setText(tickets.get(position).getHeureFin());
        holder.txtv_durationTitle_value.setText(tickets.get(position).getDuree());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

}