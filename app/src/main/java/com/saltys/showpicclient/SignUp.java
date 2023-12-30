package com.saltys.showpicclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    TextInputEditText nameET, emailET, passwordET;
    Button signupButton;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String token = "65904dc3ffcfcf518b2813d0";
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        client = new OkHttpClient();

        nameET = findViewById(R.id.singUpName);
        emailET = findViewById(R.id.signupEmail);
        passwordET = findViewById(R.id.signupPassword);
        signupButton = findViewById(R.id.signupButton);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Objects.requireNonNull(nameET.getText()).toString().trim();
                String email = Objects.requireNonNull(emailET.getText()).toString().trim();
                String password = Objects.requireNonNull(passwordET.getText()).toString().trim();

                if (email.isEmpty() || !email.matches(EMAIL_REGEX)) {
                    emailET.setError("Please input a valid email!");
                    return;
                }

                if (name.isEmpty()) {
                    nameET.setError("Please input a valid name!");
                    return;
                }

                if (password.isEmpty() || password.length() < 4) {
                    passwordET.setError("Password must have minimum 4 characters!");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build();

                                    assert user != null;
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        List<String> emptyStringList = new ArrayList<>();
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("Name", name);
                                                        data.put("Email", email);
                                                        data.put("Token", token);
                                                        data.put("Images", emptyStringList);
                                                        data.put("Privacy", true);

                                                        db.collection("Users").document(user.getUid())
                                                                .set(data)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(SignUp.this, "SignUp successful.",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                                                                        startActivity( intent );
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(SignUp.this, "Error: "+e.getMessage(),
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }


    public void PostRequest1(String name, String email){
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .add("email",email)
                .build();

        String postBody = "{ \"name\": "+name+",\"email\":"+email+"}";
        Request request = new Request.Builder()
                .url("https://showpic-api.vercel.app/api/v1/user/auth")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("TAG", "onResponse: "+response);
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d("TAG", "run: "+response.body().toString());
                                }catch (Exception e){
                                    Log.d("TAG", "run: "+e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}