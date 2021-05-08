package com.i.vaccin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity{
    EditText Email, Password;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);

        Auth = FirebaseAuth.getInstance();
    }

    public void login(View view){
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Email.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(password)){
            Password.setError("Password is required");
            return;
        }

        if (password.length() < 4){
            Password.setError("Password must be above 4 characters");
            return;
        }

        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Vaccin_List.class));
                }
                else {
                    Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void register(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
    }
}
