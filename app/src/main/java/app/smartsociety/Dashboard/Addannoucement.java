package app.smartsociety.Dashboard;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.smartsociety.Model.Annoucements;
import app.smartsociety.R;

public class Addannoucement extends AppCompatActivity {
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private EditText title,message;
    DatabaseReference ann,db;
    private String user,name;
    private String Key;
    private boolean update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addannoucement);

        title = findViewById(R.id.addtitle);
        message = findViewById(R.id.addmessage);

        ann = FirebaseDatabase.getInstance().getReference("Annoucement");

        if (getIntent() != null) {
            Key = getIntent().getStringExtra("Key");
            if (Key != null) {
                update = true;
                ann.child(Key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Annoucements annoucement = dataSnapshot.getValue(Annoucements.class);
                            assert annoucement != null;
                            title.setText(annoucement.getTitle());
                            message.setText(annoucement.getMessage());
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
        Annoucements annoucement = new Annoucements(title.getText().toString(),message.getText().toString(),date);

            if (update){
                ann.child(Key).setValue(annoucement);
                Fragment newFragment = new Annoucement();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.commit();
                finish();

            }
            else{
                ann.push().setValue(annoucement);
                Fragment newFragment = new Annoucement();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.commit();
                finish();
            }
        }
    }

