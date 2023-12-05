package com.example.woofNwhiskers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.codeseasy.com.firebaseauth.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ServiceDetails extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String ProvName, ProvImage, email, id, phone;
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(ServiceDetails.this,ServiceSeeker.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        TextView servDate, servType, servPetType, servDesc, servProvName, servNoOfServices;
        ImageView displaypic;
        Button buttonProceedPayment;

        displaypic = findViewById(R.id.displayPic);
        servProvName = findViewById(R.id.servProvName);
        servNoOfServices = findViewById(R.id.servProvNoServices);
        servDate = findViewById(R.id.servDate);
        servType = findViewById(R.id.servType);
        servPetType = findViewById(R.id.servPetType);
        servDesc = findViewById(R.id.servDesc);
        buttonProceedPayment = findViewById(R.id.buttonProceedPayment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Service Details");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        String uid = getIntent().getStringExtra("User_Id");

        if (user !=null){
            //User is signed in stay in my profile
            //set email of logged in user
            //name=User.child("Name").getValue();

            email = user.getEmail();
            id=user.getUid();
            Log.i("currentUser",email+id);
        }
        else {
            //user not signed in, go to login/register
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        Query query = databaseReference.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data is recieved
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Log.i("Query",ds.child("Name").getValue().toString());

                    ProvName = ds.child("Name").getValue().toString();
//                    Log.i("ProvName",ProvName);

                    ProvImage = ds.child("image").getValue().toString();
                    phone = ds.child("phone").getValue().toString();

                    servNoOfServices.setText("No Of Services provided: "+ds.child("NoOfServices").getValue().toString());
                    servProvName.setText(ProvName);
                    servDate.setText("Service Date: "+getIntent().getStringExtra("Serv_Date"));
                    servType.setText("Service type: "+getIntent().getStringExtra("Serv_Type"));
                    servPetType.setText("For Pet type: "+getIntent().getStringExtra("Serv_PetType"));
                    servDesc.setText(getIntent().getStringExtra("Serv_Desc"));

                    try {
                        //if image received then set
                        Picasso.get().load(ProvImage).into(displaypic);
                    }
                    catch (Exception e){
                        //set default if there is exception while getting image.
                        Picasso.get().load(R.drawable.ic_displaypic).into(displaypic);
                    }
                }

//                Log.i("ProvName2",ProvName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardPayment.class);
                intent.putExtra("Serv_Id",getIntent().getStringExtra("Serv_Id"));
                intent.putExtra("User_Id",getIntent().getStringExtra("User_Id"));
                intent.putExtra("Seeker_Id",id);
                intent.putExtra("ServiceDetails",getIntent().getStringExtra("Serv_Type")+" by: "+ProvName+"\nContact: "+phone+"\nfor date "+getIntent().getStringExtra("Serv_Date")+"\nat Location: "+getIntent().getStringExtra("Serv_Location")+"\nfor your "+getIntent().getStringExtra("Serv_PetType")+".");
                startActivity(intent);
                finish();
            }
        });
//        Log.i("ProvName",ProvName);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}