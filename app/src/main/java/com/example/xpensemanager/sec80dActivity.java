package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sec80dActivity extends AppCompatActivity {


    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec80d);

        b1=findViewById(R.id.back2);
        b2=findViewById(R.id.next2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80dActivity.this, sec80cActivity.class));
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(sec80dActivity.this, sec80ttaActivity.class));
            }
        });
    }
}