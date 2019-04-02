package app.smartsociety;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.Dashboard.Dashboard;
import app.smartsociety.Model.Register;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.splash_tv);
        iv = (ImageView) findViewById(R.id.splash_iv);

        auth = FirebaseAuth.getInstance();
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
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if (auth.getCurrentUser()!= null){
                        checkadmin();
                    }
                    else{
                        startActivity(i);
                        finish();
                    }


                }
            }
        };

        timer.start();
    }

    private void checkadmin() {
        DatabaseReference fdb = FirebaseDatabase.getInstance().getReference("Register");

        fdb.child(Objects.requireNonNull(auth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Register register = dataSnapshot.getValue(Register.class);
                    assert register != null;
                    Common.admin = register.getAdmin();
                    Common.commonregister = register;
                    loadnext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadnext() {
        finish();
        startActivity(new Intent(this, Dashboard.class));
    }
}

