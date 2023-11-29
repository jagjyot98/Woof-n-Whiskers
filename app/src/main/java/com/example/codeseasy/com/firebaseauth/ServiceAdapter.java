package com.example.codeseasy.com.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codeseasy.com.firebaseauth.databinding.ServiceRowBinding;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.SAdapter> {


    Context context;
    ArrayList<ServiceClass> services;

    public ServiceAdapter(Context context, ArrayList<ServiceClass> services) {
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public SAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row,parent,false);
        return new SAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.SAdapter holder, int position) {
        ServiceClass service = services.get(position);

        holder.serveID.setText(service.getServiceID());
        holder.serveDate.setText(service.getDate());
        holder.serveType.setText(service.getServiceType());
        holder.servePet.setText(service.getPetType());
        holder.serveLocation.setText(service.getLocation());
        holder.serveDesc.setText(service.getServiceDesc());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class SAdapter  extends RecyclerView.ViewHolder{

        TextView serveID, serveDate, serveType, servePet, serveLocation, serveDesc;
        public SAdapter(@NonNull View itemView) {
            super(itemView);

            serveID = itemView.findViewById(R.id.serveID);
            serveDate = itemView.findViewById(R.id.serveDate);
            serveType = itemView.findViewById(R.id.serveType);
            servePet = itemView.findViewById(R.id.servePet);
            serveLocation = itemView.findViewById(R.id.serveLocation);
            serveDesc = itemView.findViewById(R.id.serveDesc);
        }
    }

}