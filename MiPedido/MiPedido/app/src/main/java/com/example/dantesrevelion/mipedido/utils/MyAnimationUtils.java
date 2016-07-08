package com.example.dantesrevelion.mipedido.utils;

import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.dantesrevelion.mipedido.R;

/**
 * Created by Dantes Revelion on 07/07/2016.
 */
public class MyAnimationUtils {


    public static void translateAnimation(View v ,long duracion,float inter, float x1,float x2,float y1,float y2) {

        final TranslateAnimation translateanimation = new TranslateAnimation(x1,x2,y1,y2);
        translateanimation.setDuration(duracion);
        translateanimation.setInterpolator(new AnticipateOvershootInterpolator(
                inter));
        v.setAnimation(translateanimation);
        //  translateanimation.setAnimationListener(animationlistener);
    }
}
