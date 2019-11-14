package com.droiddevsa.budgetplanner.MVP.UI.View;


import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.droiddevsa.budgetplanner.MVP.UI.View.HomeActivity.HomeActivity;
import com.droiddevsa.budgetplanner.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView title = findViewById(R.id.textView_SplashTitle);
        TextView version = findViewById(R.id.textView_SplashVersion);
        ProgressBar progressBar = findViewById(R.id.progressBar_Splash);

        Animation fadeAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_animation);
        title.startAnimation(fadeAnimation);
        version.startAnimation(fadeAnimation);
        progressBar.startAnimation(fadeAnimation);

        final Intent i = new Intent(this, HomeActivity.class);
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(1500);//?
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }

            }

        };
        timer.start();
    }
}
