package com.example.photoblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field, reg_pass_field, reg_confirm_pass_field;
    private Button reg_btn, reg_login_btn;
    private ProgressDialog pd;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);

        reg_email_field = findViewById(R.id.etREmail);
        reg_pass_field = findViewById(R.id.etRPassword);
        reg_confirm_pass_field = findViewById(R.id.etRConfirmPassword);
        reg_btn = findViewById(R.id.btnRCreate);
        reg_login_btn = findViewById(R.id.btnRLogin);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)) {
                    if (pass.equals(confirm_pass)) {
                        pd.setMessage("Registering..");
                        pd.show();
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    pd.dismiss();
                                    String errormessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error: " + errormessage, Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "Password and Confirm password does not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
