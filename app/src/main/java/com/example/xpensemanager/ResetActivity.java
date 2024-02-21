package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;

public class ResetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        mAuth = FirebaseAuth.getInstance();

        EditText mailText = findViewById(R.id.forgot_password_email);
        Button sendEmail_btn = findViewById(R.id.btn_reset_password);
        sendEmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mailText.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Please Check your mail for Password reset Instructions", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! Reset Link is not sent!. " + e.getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        });

    }
}
