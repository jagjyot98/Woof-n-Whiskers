package com.example.woofNwhiskers;

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

import com.example.codeseasy.com.firebaseauth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText mEmlEdt,mPsdEdt;
    Button mLoginBtn;
    TextView mNotHaveAccount;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();       //////////////
        if (actionBar != null) {
            actionBar.setTitle("Login");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mEmlEdt = findViewById(R.id.emlEdt);
        mPsdEdt = findViewById(R.id.psdEdt);
        mLoginBtn = findViewById(R.id.loginBtn);
        mNotHaveAccount = findViewById(R.id.notHave_account);
        mAuth = FirebaseAuth.getInstance();

        //login button click
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // input data
                String email = mEmlEdt.getText().toString();
                String pass= mPsdEdt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //Invaild email set error
                    mEmlEdt.setError("Invalid Email");
                    mPsdEdt.setFocusable(true);
                }
                else {
                    //valid login
                    loginUser(email,pass);
                }

            }


        });
        mNotHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        //init progress dialogue
        pd = new ProgressDialog(this);
        pd.setMessage("Logging In...");

    }
    private void loginUser(String email, String pass) {
        pd.setMessage("Logging in...");
        pd.show();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }
    @Override
    public boolean onSupportNavigateUp(){   //////////
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}