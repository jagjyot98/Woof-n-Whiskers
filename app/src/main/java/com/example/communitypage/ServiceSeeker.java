package com.example.communitypage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ServiceSeeker extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ServiceAdapter serviceAdapter;
    ArrayList<ServiceClass> services;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_seeker);

        recyclerView = findViewById(R.id.servicesList);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add New Post");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();      //////////////////

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        services = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this,services,this);
        recyclerView.setAdapter(serviceAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.child("Services").getChildren()){
                    ServiceClass service = dataSnapshot.getValue(ServiceClass.class);
                    
                    services.add(service);
                }
                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ServiceSeeker.this, ServiceDetails.class);
        intent.putExtra("Serv_Id", services.get(position).getServiceID());
        intent.putExtra("User_Id", services.get(position).getUserID());
        intent.putExtra("Serv_Date", services.get(position).getDate());
        intent.putExtra("Serv_Type", services.get(position).getServiceType());
        intent.putExtra("Serv_PetType", services.get(position).getPetType());
        intent.putExtra("Serv_Desc", services.get(position).getServiceDesc());
        startActivity(intent);
    }
}