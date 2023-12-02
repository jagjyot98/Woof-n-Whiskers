package com.example.woofNwhiskers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceProviderNConfirm extends AppCompatActivity{ //implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    StorageReference storageReference;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    ProgressDialog pd;
    Button buttonConfirm;
    EditText location, serDesc, date;
    String UserID;

    ServiceClass service;

    Spinner serType, petType;
    ProgressBar progressBar;
    String servText="", petText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_nconfirm);

        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data is recieved
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    UserID = ds.child("uid").getValue().toString();
//                    String Name = ds.child("Name").getValue().toString();
//                    String email = ds.child("email").getValue().toString();
//                    String image = ds.child("image").getValue().toString();
//                    String petType = "" + ds.child("petType").getValue();
//                    String phone = "" + ds.child("phone").getValue().toString();
//                    String cover = "" + ds.child("cover").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Service Posting");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        buttonConfirm = findViewById(R.id.buttonConfirmSerProv);
//        buttonBack = findViewById(R.id.buttonBackSerProv);
        progressBar = findViewById(R.id.progressBar);

        date = findViewById(R.id.serviceDate);
        location = findViewById(R.id.serviceLocation);
        serDesc = findViewById(R.id.serviceDesc);
        serType = (Spinner) findViewById(R.id.spinnerSerType);
        petType = (Spinner) findViewById(R.id.spinnerPetType);


        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();      ///////////////////


//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        //create a list of items for the spinner.
        String[] services = new String[]{"Select Pet Service", "Pet Sitter", "Pet Walker", "Pet Buddy"};
        String[] pets = new String[]{"Select Pet Type", "Dog", "Cat"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pets);

        //set the spinners adapter to the previously created one.
        serType.setAdapter(serviceAdapter);
        petType.setAdapter(petAdapter);


        serType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if(position != 0)
                    servText = services[position];

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ServiceProviderNConfirm.this, "Not Selected SERVICE Type", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        petType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if(position != 0)
                    petText = pets[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ServiceProviderNConfirm.this, "Not Selected PET Type", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String servDate;
                String TextLocation, TextDesc;

                servDate = String.valueOf(date.getText());  //valueOf(date.getText());

                TextLocation = String.valueOf(location.getText());

                TextDesc = String.valueOf(serDesc.getText());

                //getting current date and time for service id
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                if (TextUtils.isEmpty(String.valueOf(date.getText()))) {
                    Toast.makeText(ServiceProviderNConfirm.this, "Enter date", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

//                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//                String textFromEditText = "23-08-2013";
//                LocalDate myDate = LocalDate.parse(textFromEditText, dateFormatter);
//                String myText = myDate.format(dateFormatter);
//                System.out.println("Formatted again: " + myText);

                if (TextUtils.isEmpty(TextLocation)) {
                    Toast.makeText(ServiceProviderNConfirm.this, "Enter location", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(servText)) {
                    Toast.makeText(ServiceProviderNConfirm.this, "Select service Type", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(petText)) {
                    Toast.makeText(ServiceProviderNConfirm.this, "Select pet Type", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //              if(TextUtils.isEmpty(TextDesc))     //userId,
                service = new ServiceClass(UserID, currentDateandTime, "", TextLocation, servDate, servText, petText, TextDesc);  //date.getText(),
                addDatatoFirebase(service);
            }

            private void addDatatoFirebase(ServiceClass service) {
                progressBar.setVisibility(View.GONE);
                // we are use add value event listener method
                // which is called with database reference.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // inside the method of on Data change we are setting
                        // our object class to our database reference.
                        // data base reference will sends data to firebase.
                        databaseReference.child("Services").child(service.getServiceID()).setValue(service);

                        // after adding this data we are showing toast message.
                        Toast.makeText(ServiceProviderNConfirm.this, "Service posted", Toast.LENGTH_SHORT).show();

                        date.setText("");
                        location.setText("");
                        serType.setSelection(0);
                        petType.setSelection(0);
                        serDesc.setText("");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // if the data is not added or it is cancelled then
                        // we are displaying a failure toast message.
                        Toast.makeText(ServiceProviderNConfirm.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}

