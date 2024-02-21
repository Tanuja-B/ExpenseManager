package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sec80ttaActivity extends AppCompatActivity {

    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec80tta);

        b1=findViewById(R.id.back3);
        b2=findViewById(R.id.next3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80ttaActivity.this, sec80dActivity.class));
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80ttaActivity.this, sec80cActivity.class));
            }
        });

    }
}