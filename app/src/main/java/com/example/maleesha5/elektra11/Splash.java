package com.example.maleesha5.elektra11;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by kokil on 19/05/2017.
 */

public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        final ImageView circle = (ImageView) findViewById(R.id.imageView);
        final ImageView plug = (ImageView) findViewById(R.id.imageView2);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        circle.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                circle.startAnimation(an2);
                plug.startAnimation(an2);
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


}
