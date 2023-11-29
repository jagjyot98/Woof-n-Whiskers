package com.example.woofNwhiskers;





import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.codeseasy.com.firebaseauth.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    //TextView mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        //mProfile = findViewById(R.id.Profile);


        //home fragment default on start
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
            HomeFragment fragment1 = new HomeFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.content,fragment1,"");
            ft1.commit();
        }

    }
    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            // handle item clicks
            int itemId= menuItem.getItemId();
            if(itemId == R.id.nav_home){
                actionBar.setTitle("Home");
                HomeFragment fragment1 = new HomeFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.content,fragment1,"");
                ft1.commit();
                return true;
            } else if (itemId == R.id.nav_profile) {
                actionBar.setTitle("MyProfile");
                ProfileFragment fragment2 = new ProfileFragment();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.content,fragment2,"");
                ft2.commit();
                return true;
            } else if (itemId == R.id.nav_services) {
                actionBar.setTitle("Services");
                ServicesFragment fragment3 = new ServicesFragment();
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.content,fragment3,"");
                ft3.commit();
                return true;
            }



            return false;
        }
    };
    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user !=null){
            //User is signed in stay in my profile
            //set email of logged in user
           // mProfile.setText(user.getEmail());
        }
        else {
            //user not signed in, go to login/register
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }
    @Override
    protected void onStart(){
        // check user status on app start
        checkUserStatus();
        super.onStart();
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }



}