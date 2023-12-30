package com.saltys.showpicclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView displayName,userImageCounter;
    ImageView settingsButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView imagesRecyclerView;
    RecyclerImageViewAdapter recyclerImageViewAdapter;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayName = findViewById(R.id.userDisplayName);
        userImageCounter = findViewById(R.id.userImageCounter);
        settingsButton = findViewById(R.id.settingsButton);
        imagesRecyclerView = findViewById(R.id.imagesRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        circleImageView = findViewById(R.id.userAvatar);

        setUserData();


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingsFragment settings = new SettingsFragment();
                settings.show( getSupportFragmentManager(), "settings" );
            }
        });
    }

    private void setUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getPhotoUrl() != null){
            Glide.with(MainActivity.this)
                    .load(user.getPhotoUrl())
                    .into(circleImageView);
        }
        DocumentReference docRef = db.collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        displayName.setText(Objects.requireNonNull(document.getString("Name").toUpperCase()));
                        Object Images = document.get("Images");
                        if (Images != null && Images instanceof List) {
                            List<?> imageList = (List<?>) Images;
                            userImageCounter.setText("Collection: "+imageList.size());
                            ArrayList<String> imagesArrayList = new ArrayList<>((Collection<? extends String>) imageList);
                            Log.d("TAG", "onComplete: "+imagesArrayList);
                            recyclerImageViewAdapter = new RecyclerImageViewAdapter(MainActivity.this,imagesArrayList);
                            imagesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                            imagesRecyclerView.setAdapter(recyclerImageViewAdapter);
                        }
                    }
                }
            }
        });

    }

    public void exit(){
        finishAffinity(); // Close all activites
        System.exit(0);
    }

    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent( this, LoginScreen.class );
        startActivity( intent );
        finish();
    }

    public void showSecret() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.notice_dailogue_all, null);
        builder.setView(v);
        TextView notice = v.findViewById(R.id.tokenText);
        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        notice.setText(document.getString("Token"));
                        Log.d("TAG", "DocumentSnapshot data: " + document.getString("Token"));
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }
}
