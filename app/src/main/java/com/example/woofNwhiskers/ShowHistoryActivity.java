package com.example.woofNwhiskers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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

public class ShowHistoryActivity extends AppCompatActivity implements RecyclerViewInterface{
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    ServiceAdapter adapterServices;
    ArrayList<ServiceClass> serviceList;
    RecyclerView servicesRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        servicesRecyclerView = findViewById(R.id.recyclerviewServices);

        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        LinearLayoutManager layoutManagerServices = new LinearLayoutManager(this);
        layoutManagerServices.setStackFromEnd(true);
        layoutManagerServices.setReverseLayout(true);

        serviceList = new ArrayList<>();

        adapterServices = new ServiceAdapter(this, serviceList, this);
        servicesRecyclerView.setAdapter(adapterServices);
        servicesRecyclerView.setLayoutManager(layoutManagerServices);
        DatabaseReference refServices = FirebaseDatabase.getInstance().getReference("Services");

        Query query = refServices.orderByChild("seekerID").equalTo(user.getUid());
        //get all data from this ref
        query.addValueEventListener(new ValueEventListener() {          //For Services
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ServiceClass myServices = ds.getValue(ServiceClass.class);

                    //add to list
                    serviceList.add(myServices);
                }
                //adapter
                adapterServices.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        //Nothing to be done when clickeds
    }
}