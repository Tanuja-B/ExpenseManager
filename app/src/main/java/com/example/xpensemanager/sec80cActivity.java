package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sec80cActivity extends AppCompatActivity {


    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec80c);

        b1=findViewById(R.id.back1);
        b2=findViewById(R.id.next1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80cActivity.this, sec80ttaActivity.class));
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80cActivity.this, sec80dActivity.class));
            }
        });

    }
}