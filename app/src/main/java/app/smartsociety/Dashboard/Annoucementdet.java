package app.smartsociety.Dashboard;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.smartsociety.Model.Annoucements;
import app.smartsociety.R;

public class Annoucementdet extends AppCompatActivity {
    FirebaseDatabase fdb;
    DatabaseReference ann;


    TextView title,message,date;
    String AnnouncementId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annoucementdet);

        title = findViewById(R.id.annoucetitle);
        message = findViewById(R.id.annoucemessage);
        date = findViewById(R.id.annoucedate);

        fdb = FirebaseDatabase.getInstance();
        ann = fdb.getReference("Annoucement");
        if (getIntent()!= null){
            AnnouncementId = getIntent().getStringExtra("AnnoucementId");
        }
        ann.child(AnnouncementId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Annoucements annoucement = dataSnapshot.getValue(Annoucements.class);

                    assert annoucement != null;
                    title.setText(annoucement.getTitle());
                    message.setText(annoucement.getMessage());
                    date.setText(annoucement.getDate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
