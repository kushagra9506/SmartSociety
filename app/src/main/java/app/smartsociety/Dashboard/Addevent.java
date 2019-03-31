package app.smartsociety.Dashboard;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import app.smartsociety.Model.Event;
import app.smartsociety.Model.Register;
import app.smartsociety.R;

public class Addevent extends AppCompatActivity {
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private EditText name,description,time,eventdate,contact,place,organisedby;
    DatabaseReference ann,user;

    private String Key;
    private boolean update;
    FirebaseAuth auth;
    private String sname,roomno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.addname);
        description = findViewById(R.id.adddescription);
        time = findViewById(R.id.addtime);
        eventdate = findViewById(R.id.adddate);
        contact = findViewById(R.id.addcontact);
        place = findViewById(R.id.addplace);
        user = FirebaseDatabase.getInstance().getReference("Register");

user.child(Objects.requireNonNull(auth.getUid())).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()){
            Register register = dataSnapshot.getValue(Register.class);
            assert register != null;
            sname = register.getName();
            roomno = register.getRoomno();

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

        ann = FirebaseDatabase.getInstance().getReference("Events");

        if (getIntent() != null){
            Key = getIntent().getStringExtra("Key");
            if (Key != null){
                update = true;
                ann.child(Key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Event event = dataSnapshot.getValue(Event.class);
                            assert event != null;
                            name.setText(event.getName());
                            description.setText(event.getDescription());
                            time.setText(event.getTime());
                            place.setText(event.getPlace());
                            time.setText(event.getTime());
                            eventdate.setText(event.getDate());
                            contact.setText(event.getContact());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }


    public void add(View view) {

        Event event = new Event(name.getText().toString(),eventdate.getText().toString()
                ,description.getText().toString(),place.getText().toString()
                ,sname,time.getText().toString(),contact.getText().toString(),roomno);

        if (update){
            ann.child(Key).setValue(event);
            Fragment newFragment = new EventActivity();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.commit();

        }
        else{
            ann.push().setValue(event);
            Fragment newFragment = new EventActivity();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.commit();
        }
    }
}
