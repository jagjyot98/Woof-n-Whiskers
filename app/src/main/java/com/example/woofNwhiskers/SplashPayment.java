package com.example.woofNwhiskers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeseasy.com.firebaseauth.R;
import com.example.woofNwhiskers.model.UserClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashPayment extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    UserClass ProvUser;
    String NoOfServices = "0";
    DatabaseReference databaseReference;
    String name, email, uid, dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_payment);

        TextView showDetailsView = findViewById(R.id.showDetailsView);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            //User is signed in stay in my profile
            //set email of logged in user
            //name=User.child("Name").getValue();

            email = user.getEmail();
            uid = user.getUid();
            Log.i("currentUser", email + uid);
        } else {
            //user not signed in, go to login/register
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Card Payment");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();


        String serviceID = getIntent().getStringExtra("Serv_Id");
        String seekerID = getIntent().getStringExtra("Seeker_Id");
        String userProvID = getIntent().getStringExtra("User_Id");

        databaseReference = firebaseDatabase.getReference("Users");
        Query query = databaseReference.orderByChild("uid").equalTo(userProvID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProvUser = dataSnapshot.getValue(UserClass.class);

                    assert ProvUser != null;
                    NoOfServices = ProvUser.getNoOfServices();
                }
//            Log.i("ProvUser",ProvUser.getuserid());
//                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        HashMap<String, Object> UserResult = new HashMap<>();
        UserResult.put("NoOfServices", String.valueOf(Integer.parseInt(NoOfServices)+1));

        Log.i("NoOfServices",String.valueOf(NoOfServices+1));

        databaseReference.child(userProvID).updateChildren(UserResult)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        showDetailsView.setText(getIntent().getStringExtra("ServiceDetails"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        HashMap<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("seekerID", seekerID);

        databaseReference = firebaseDatabase.getReference("Services");
        databaseReference.child(serviceID).updateChildren(serviceResult)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showDetailsView.setText(getIntent().getStringExtra("ServiceDetails"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}