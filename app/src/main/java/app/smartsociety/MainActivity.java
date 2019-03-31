package app.smartsociety;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.splash_tv);
        iv = (ImageView) findViewById(R.id.splash_iv);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        tv.startAnimation(anim);
        iv.startAnimation(anim);

        final Intent i = new Intent(this, Welcome_slider_1.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
    }

