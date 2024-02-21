package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        EditText emailUser=findViewById(R.id.email_account);
        EditText dateofCreation=findViewById(R.id.dateofCreation);
        EditText timeOfCreation=findViewById(R.id.timeOfCreation);
        EditText signInAt=findViewById(R.id.lastSignInAt);

//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        emailUser.setText(user.getEmail());
//
//
//        Long timestampCreate=user.getMetadata().getCreationTimestamp();
//        Date date1 = new Date(timestampCreate);
//
//        SimpleDateFormat jdf = new SimpleDateFormat("dd MMM yyyy");
//        String java_date = jdf.format(date1);
//
//        SimpleDateFormat jdf1 = new SimpleDateFormat("HH:mm:ss z");
//        String TimeOfCreation = jdf1.format(date1);
//
//
//        dateofCreation.setText(java_date);
//        timeOfCreation.setText(TimeOfCreation);
//
//        Long lastSignInTS=user.getMetadata().getLastSignInTimestamp();
//        Date date2 = new Date(lastSignInTS);
//        SimpleDateFormat jdf2 = new SimpleDateFormat("dd MMM yyyy    HH:mm:ss z");
//        String SignInAt = jdf2.format(date2);
//
//        signInAt.setText(SignInAt);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            emailUser.setText(user.getEmail());

            // Get account creation timestamp
            long timestampCreate = user.getMetadata().getCreationTimestamp();
            Date date1 = new Date(timestampCreate);
            SimpleDateFormat jdf = new SimpleDateFormat("dd MMM yyyy");
            String java_date = jdf.format(date1);

            SimpleDateFormat jdf1 = new SimpleDateFormat("HH:mm:ss z");
            String TimeOfCreation = jdf1.format(date1);

            dateofCreation.setText(java_date);
            timeOfCreation.setText(TimeOfCreation);

            // Get last sign-in timestamp
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser refreshedUser = mAuth.getCurrentUser();
                        long lastSignInTS = refreshedUser.getMetadata().getLastSignInTimestamp();
                        Date date2 = new Date(lastSignInTS);
                        SimpleDateFormat jdf2 = new SimpleDateFormat("dd MMM yyyy    HH:mm:ss z");
                        String SignInAt = jdf2.format(date2);

                        signInAt.setText(SignInAt);
                    } else {
                        Toast.makeText(AccountActivity.this, "Failed to refresh user data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where the user is not logged in
        }
    }

}