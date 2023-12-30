package com.saltys.showpicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {
    Animation aniSlide;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView logo = findViewById(R.id.splashScreenLogo);
        TextView tagline = findViewById(R.id.splashScreenTagline);


        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(900);

        AnimationSet animation = new AnimationSet(true); //change to false
        animation.addAnimation(fadeIn);
        logo.setAnimation(animation);
        tagline.setAnimation(animation);

        aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        aniSlide.setDuration(1000);

//        logo.startAnimation(aniSlide);
        new Handler( ).postDelayed(new Runnable() {
            @Override
            public void run() {
                tagline.startAnimation(fadeOut);
                logo.startAnimation(aniSlide);
                logo.setVisibility(View.GONE);
                tagline.setVisibility(View.GONE);
                Toast.makeText(Splash.this, "Show", Toast.LENGTH_SHORT).show();
                new Handler( ).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent;
                        if(user == null){
                            intent = new Intent( Splash.this, LoginScreen.class );
                        }else{
                            intent = new Intent( Splash.this, MainActivity.class );
                        }
                        startActivity( intent );
                        finish();
                    }
                },900 );
            }
        },1000 );
    }
}