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

import app.smartsociety.Model.Event;
import app.smartsociety.R;

public class Eventdetails extends AppCompatActivity {
    FirebaseDatabase fdb;
    DatabaseReference ann;


    TextView name,description,time,eventdate,contact,place,organisedby,updatedby;
    String EventId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetails);


        name = findViewById(R.id.eventnamedet);
        description = findViewById(R.id.eventdescriptiondet);
        time = findViewById(R.id.eventtimedet);
        eventdate = findViewById(R.id.eventdatedet);
        contact = findViewById(R.id.eventcontactdet);
        place = findViewById(R.id.eventplacedet);
        organisedby = findViewById(R.id.eventorganizerdet);



        if (getIntent()!= null){
            EventId = getIntent().getStringExtra("EventId");
        }


        fdb = FirebaseDatabase.getInstance();
        ann = fdb.getReference("Events");

        ann.child(EventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);

                    assert event != null;
                    name.setText(event.getName());
                    description.setText(event.getDescription());
                    eventdate.setText(event.getDate());
                    place.setText(event.getPlace());
                    organisedby.setText(event.getUpdatedby());
                    time.setText(event.getTime());
                    contact.setText(event.getContact());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
