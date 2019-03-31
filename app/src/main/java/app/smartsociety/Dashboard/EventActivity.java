package app.smartsociety.Dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.Model.Event;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.EventViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventActivity extends Fragment {

    private FirebaseDatabase fdb;
    private DatabaseReference databaseReference,userdet;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;
    private FirebaseRecyclerOptions<Event> event;
    View v;
    FloatingActionButton fab;
    public EventActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;

        fdb = FirebaseDatabase.getInstance();
        databaseReference = fdb.getReference("Events");
        recyclerView = v.findViewById(R.id.eventrecycler);

        ((Dashboard) Objects.requireNonNull(getActivity()))
                .setActionBarTitle("Annoucement");

        fab = v.findViewById(R.id.eventfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent annouce = new Intent(getActivity(),Addevent.class);
                startActivity(annouce);
            }
        });

        layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        loaddetails();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.Update)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else if(item.getTitle().equals(Common.Delete)){
            deleteannoucement(adapter.getRef(item.getOrder()).getKey());
            Toast.makeText(getActivity(),"delete!!",Toast.LENGTH_LONG).show();
        }

        return super.onContextItemSelected(item);

    }

    private void loaddetails() {

        event = new FirebaseRecyclerOptions.Builder<Event>().setQuery(
                databaseReference.orderByKey()
                ,Event.class).build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(event) {


            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
                holder.name.setText(model.getName());
                holder.description.setText(model.getDescription());
                holder.date.setText(model.getDate());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i=new Intent(getActivity(),Eventdetails.class);
                        i.putExtra("EventId",adapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event,parent,false);
                return new EventViewHolder(view);
            }


        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void deleteannoucement(String key) {
        databaseReference.child(key).removeValue();
    }

    private void showUpdateDialog(String key, Event item) {
        Intent annouce = new Intent(getActivity(),Addevent.class);
        annouce.putExtra("Key",key);
        startActivity(annouce);

    }


}
