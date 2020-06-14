package com.example.firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class signupActivity extends AppCompatActivity implements View.OnClickListener { //TODO SIGN ACTIVITY
    private FirebaseAuth mAuth;
    EditText emailText;
    EditText passwordText;
    EditText passwordConfirmationText;
    TextView errorText;
    Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        emailText = (EditText) findViewById(R.id.signupEmail);
        passwordText = (EditText) findViewById(R.id.signupPassword);
        passwordConfirmationText = (EditText) findViewById(R.id.signupPasswordConfirmation);


        errorText = (TextView) findViewById(R.id.errorTextView);
        submitBtn = (Button) findViewById(R.id.signupbtn);
        errorText.setVisibility(View.GONE);
        submitBtn.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
    }

    private void signup(){

        String email, password,passwordConfrim;
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        passwordConfrim=passwordConfirmationText.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show(); //toasts
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(passwordConfrim)) {
            Toast.makeText(getApplicationContext(), "Please confirm password!", Toast.LENGTH_LONG).show();
            return;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();

// if network is connected, download feed

        if (networkInfo != null && networkInfo.isConnected()) {

            if(password.equals(passwordConfrim)) {


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_LONG).show();
//
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    errorText.setText("Email is in use!");
                                    errorText.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
            else
            {
                errorText.setText("Passwords don't match!");
                errorText.setVisibility(View.VISIBLE);
            }
        }
        else
            Toast.makeText(getApplicationContext(), "You need an internet connection for this action", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onClick(View v) {
    signup();
    }
}
