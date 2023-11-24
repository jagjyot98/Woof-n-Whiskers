package com.example.communitypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText mFnameEdt,mLnameEdt,mEmailEdt,mPasswordEdt;
    Button mRegisterBtn;
    TextView mHaveAccount;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Registration");
        }

        mFnameEdt = findViewById(R.id.fNameEdt);
        mLnameEdt = findViewById(R.id.lNameEdt);
        mEmailEdt = findViewById(R.id.emailEdt);
        mPasswordEdt = findViewById(R.id.passwordEdt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccount = findViewById(R.id.have_account);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = mFnameEdt.getText().toString().trim();
                String lName = mLnameEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();
                String email = mEmailEdt.getText().toString().trim();

                //email validate
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        mEmailEdt.setError("Invalid Email");
                        mEmailEdt.setFocusable(true);
                    } else if (password.length()<8) {
                        mPasswordEdt.setError("Password Length must be 8 characters");
                        mPasswordEdt.setFocusable(true);
                    }
                    else {
                        registerUser(fName,lName,email,password);
                    }
            }


        });
        // handle already have account onclick
        mHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });


    }
    private void registerUser(String fName, String lName, String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            // get user email and uid from auth
                            if (user != null) {
                                String email = user.getEmail();
                                String uid = user.getUid();

                                //when user is registered store user info in firebase realtime  database
                                // using hashmap
                                HashMap<Object, String> hashMap = new HashMap<>();
                                // add info in hashmap
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("Name", fName);// adding these details in profile edit
                                hashMap.put("phone", "");
                                hashMap.put("image", "");
                                hashMap.put("cover", "");
                                hashMap.put("petType", lName);


                                //firebase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                //path to store use data  name "USers"
                                DatabaseReference reference = database.getReference("Users");
                                //put data within hashmap in database
                                reference.child(uid).setValue(hashMap);
                            }
                            Toast.makeText(RegisterActivity.this, "Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go to previous activity
        return super.onSupportNavigateUp();
    }
}