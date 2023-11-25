package com.example.woofNwhiskers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.SAdapter> {

 private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<ServiceClass> services;

    public ServiceAdapter(Context context, ArrayList<ServiceClass> services, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.services = services;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public static class SAdapter  extends RecyclerView.ViewHolder {
        TextView serveID, serveDate, serveType, servePet, serveLocation, serveDesc;
        public SAdapter(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            serveID = itemView.findViewById(R.id.serveID);
            serveDate = itemView.findViewById(R.id.serveDate);
            serveType = itemView.findViewById(R.id.serveType);
            servePet = itemView.findViewById(R.id.servePet);
            serveLocation = itemView.findViewById(R.id.serveLocation);
            serveDesc = itemView.findViewById(R.id.serveDesc);

        }

        void bindView(final int position, RecyclerViewInterface recyclerViewInterface){
            itemView.setOnClickListener(new View.OnClickListener() {        //setting onclick listener on rows
                @Override
                public void onClick(View v) {                                   //attaching delete functionality with every row
                    if(recyclerViewInterface != null){
                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public SAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row,parent,false);
        return new SAdapter(view,recyclerViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull SAdapter holder, int position) {
        ServiceClass service = services.get(position);

        holder.serveID.setText(service.getServiceID());
        holder.serveDate.setText(service.getDate());
        holder.serveType.setText(service.getServiceType());
        holder.servePet.setText(service.getPetType());
        holder.serveLocation.setText(service.getLocation());
        holder.serveDesc.setText(service.getServiceDesc());

        ((SAdapter) holder).bindView(position, recyclerViewInterface);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }



}