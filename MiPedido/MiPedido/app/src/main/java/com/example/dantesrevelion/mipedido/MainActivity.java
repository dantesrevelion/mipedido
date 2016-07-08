package com.example.dantesrevelion.mipedido;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.dantesrevelion.mipedido.utils.MyAnimationUtils;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1800;
    private Handler mHandler = new Handler();

    Runnable ruu = null;
    LinearLayout layoutLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutLogo=(LinearLayout) findViewById(R.id.layoutLogo);
        MyAnimationUtils.translateAnimation(layoutLogo,1000L,2.0f,0,0,300,0);
        mHandler.postDelayed(ruu = new Runnable() {
            public void run() {
                doStuff();

            }
        },SPLASH_DISPLAY_LENGTH);
        /*
        layoutLogo=(LinearLayout) findViewById(R.id.layoutLogo);


        for(int i=0;i<300;i++){
      //      TranslateAnimation slide = new TranslateAnimation(0, 0, 300,300-i );
            Animation an=new TranslateAnimation(0,300,0,0);
            an.setDuration(100);
            LinearInterpolator interpolator=new LinearInterpolator();
            an.setInterpolator(interpolator);
            // slide.setFillAfter(true);

            layoutLogo.startAnimation(an);
        }
    */
    }
    private void doStuff() {
        mHandler.removeCallbacksAndMessages(ruu);
        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
        finish();
    }

}
