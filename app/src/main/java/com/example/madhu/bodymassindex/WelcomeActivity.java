package com.example.madhu.bodymassindex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    ImageView ivNormal,ivUnderWeight,ivObese,ivPreobese,ivOverWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ivNormal=(ImageView)findViewById(R.id.ivNormal);
        ivUnderWeight=(ImageView)findViewById(R.id.ivUnderweight);
        ivObese=(ImageView)findViewById(R.id.ivObese);
        ivPreobese=(ImageView)findViewById(R.id.ivPreobese);
        ivOverWeight=(ImageView)findViewById(R.id.ivOverweight);

        ivNormal.startAnimation(AnimationUtils.loadAnimation(this,R.anim.a1));
        ivUnderWeight.startAnimation(AnimationUtils.loadAnimation(this,R.anim.a2));
        ivObese.startAnimation(AnimationUtils.loadAnimation(this,R.anim.a3));
        ivPreobese.startAnimation(AnimationUtils.loadAnimation(this,R.anim.a4));
        ivOverWeight.startAnimation(AnimationUtils.loadAnimation(this,R.anim.a5));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
