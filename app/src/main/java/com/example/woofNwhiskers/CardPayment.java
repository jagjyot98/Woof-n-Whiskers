package com.example.woofNwhiskers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.woofNwhiskers.databinding.ActivityCardPaymentBinding;

public class CardPayment extends AppCompatActivity {

    ActivityCardPaymentBinding cardPayment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardPayment = ActivityCardPaymentBinding.inflate(getLayoutInflater());
        setContentView(cardPayment.getRoot());

        //click listener for button
        cardPayment.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String cardName = cardPayment.cardName.getText().toString();
                    String cardNumber = cardPayment.cardNumber.getText().toString();
                    String expiryDate = cardPayment.expiryDate.getText().toString();
                    String cvv = cardPayment.cvv.getText().toString();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Card Payment");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }



    private boolean validateInput() {
        String cardName = cardPayment.cardName.getText().toString().trim();
        String cardNumber = cardPayment.cardNumber.getText().toString().trim();
        String expiryDate = cardPayment.expiryDate.getText().toString();
        String cvv = cardPayment.cvv.getText().toString();
        boolean termsChecked = cardPayment.checkbox.isChecked();
        boolean paymentSuccessful = processPayment(cardName, cardNumber, expiryDate, cvv);

        if (cardName.isEmpty()) {
            cardPayment.cardName.setError("Enter the name on card");
            return false;
        }
        if (cardNumber.isEmpty() || cardNumber.length() < 16) {
            cardPayment.cardNumber.setError("Enter a valid Card Number");
            return false;
        }
        if (expiryDate.isEmpty() || !expiryDate.matches("^(0[1-9]|1[0-2])\\/([0-9]{2})$")) {
            cardPayment.expiryDate.setError("Enter a valid expiry date (MM/YY)");
            return false;
        }
        if (cvv.isEmpty() || cvv.length() < 3 || cvv.length() >= 4) {
            cardPayment.cvv.setError("Valid CVV is required");
            return false;
        }
        if (!termsChecked) {
            Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Handle the payment result
        if (paymentSuccessful) {
            // Payment successful, handle success case

            // Placeholder for handling success case
            // Add logic for successful payment

            // Toast.makeText(CardPayment.this, "Payment successful!", Toast.LENGTH_SHORT).show();
            clearFields();
            Intent intent = new Intent(getApplicationContext(), SplashPayment.class);
            startActivity(intent);
            finish();
        } else {
            // Payment failed, handle failure case

            // Placeholder for handling failure case
            // Add logic for failed payment
            Toast.makeText(CardPayment.this, "Payment failed. Please check your card details.", Toast.LENGTH_SHORT).show();
        }
        // If all validations pass, return true
        return true;
    }

    private void clearFields() {
        cardPayment.cardName.setText("");
        cardPayment.cardNumber.setText("");
        cardPayment.expiryDate.setText("");
        cardPayment.cvv.setText("");
        cardPayment.checkbox.setChecked(false);
    }

    private boolean processPayment(String cardName, String cardNumber, String expiryDate, String cvv) {
        // Placeholder for payment processing logic
        // Implement your payment processing mechanism here
        // For demonstration purposes, consider a simple validation

        if (cardNumber.length() == 16 && expiryDate.matches("^(0[1-9]|1[0-2])\\/([0-9]{2})$") && cvv.length() == 3 && !cardName.isEmpty()) {
            // Simulating successful payment processing
            return true;
        } else {
            // Payment processing failed
            return false;
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();//go previous activity
        return super.onSupportNavigateUp();
    }
}