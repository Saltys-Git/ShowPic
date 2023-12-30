package com.saltys.showpicclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    TextView goSignUpText;
    Button loginButton;
    TextInputEditText emailET, passwordET;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.loginEmail);
        passwordET = findViewById(R.id.loginPassword);
        goSignUpText = findViewById(R.id.loginScreenSignupText);
        loginButton = findViewById(R.id.loginButton);

        goSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), SignUp.class );
                startActivity( intent );
            }
        });
        loginButton.setOnClickListener(view -> {
            String email = Objects.requireNonNull(emailET.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordET.getText()).toString().trim();

            if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                emailET.setError("Please input a valid email!");
                return;
            }

            if (password.isEmpty() || password.length() < 4) {
                passwordET.setError("Password must have minimum 4 characters!");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                                startActivity( intent );
                                finish();
                            } else {
                                Toast.makeText(LoginScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}