package com.example.firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText usernameEdit;
    EditText passwordEdit;
    Button loginBtn;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        usernameEdit = (EditText) findViewById(R.id.userNameEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //intent to signup activity
                Intent intent = new Intent(LoginActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            Intent intent = new Intent(LoginActivity.this, shopActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(){

        String email, password;
        email = usernameEdit.getText().toString();
        password = passwordEdit.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show(); //toasts
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(LoginActivity.this, shopActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
