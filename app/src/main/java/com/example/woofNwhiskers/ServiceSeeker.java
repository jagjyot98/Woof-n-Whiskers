package com.example.woofNwhiskers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeseasy.com.firebaseauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ServiceSeeker extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    ServiceAdapter serviceAdapter;
    ArrayList<ServiceClass> services;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_seeker);

        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Services");

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        services = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this,services,this);
        recyclerView.setAdapter(serviceAdapter);

        Query query = databaseReference.orderByChild("seekerID").equalTo("");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ServiceClass service = dataSnapshot.getValue(ServiceClass.class);
                    if(!Objects.equals(service.getUserID(), user.getUid()))
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
        intent.putExtra("Serv_Location", services.get(position).getLocation());
        startActivity(intent);
    }
}