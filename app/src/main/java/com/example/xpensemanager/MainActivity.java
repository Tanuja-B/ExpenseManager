package com.example.xpensemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgotpassword;
    private TextView mSignUphere;


    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth=FirebaseAuth.getInstance();

        // we use this below code to not login again and again with same eamil id and password.
        // If same user logins again,they can straight away login.No need to enter all the essentials.
//


        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        mDialog=new ProgressDialog(this);

        //up to the above code only...for that usage.
        loginDetails();
    }

    private void loginDetails(){

        mEmail=findViewById(R.id.email_login);
        mPass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        mForgotpassword=findViewById(R.id.forgot_password);
        mSignUphere=findViewById(R.id.signup_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String pass=mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Password is required.");
                    return;
                }

                mDialog.setMessage("Processing");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mDialog.dismiss();
                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }


        });

        mSignUphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

        mForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetActivity.class));
            }
        });
    }

}
