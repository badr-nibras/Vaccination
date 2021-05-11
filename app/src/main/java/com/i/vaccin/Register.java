package com.i.vaccin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnFocusChangeListener {

    EditText Email, Password, FullName, Birthday, ConfirmerPass;
    FirebaseAuth Auth;
    FirebaseFirestore FStore;
    String UserID;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    MaterialDatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        ConfirmerPass = findViewById(R.id.editTextTextPassword2);
        FullName = findViewById(R.id.PersonName);
        Birthday = findViewById(R.id.Birthday);
        FStore = FirebaseFirestore.getInstance();
        Auth = FirebaseAuth.getInstance();
        radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Birthday");
        datePicker = builder.build();

        Birthday.setOnFocusChangeListener(this);



       /* if (Auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }*/
    }


    public void date(View view){
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        //Birthday.setText(datePicker.getHeaderText());
    }
    public void register(View view){
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        final String fullName = FullName.getText().toString().trim();
        final String birthday = Birthday.getText().toString().trim();
        String confirmed = ConfirmerPass.getText().toString().trim();


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

        /*if (confirmed != password){
            ConfirmerPass.setError("Both password are not identical");
            return;
        }*/


        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);


        Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                    UserID = Auth.getCurrentUser().getUid();
                    DocumentReference documentReference = FStore.collection("users").document(UserID);
                    Map<String,Object> User = new HashMap<>();
                    User.put("Full Name", fullName);
                    User.put("Birthday", birthday);
                    User.put("Sex", radioSexButton.getText());
                    documentReference.set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "onSuccess: user Profile is created for "+ UserID);
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), Vaccin_List.class));
                }
                else {
                    Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(b) datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

    }

}

