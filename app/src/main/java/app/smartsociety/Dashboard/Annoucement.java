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
import app.smartsociety.Model.Annoucements;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.AnnouceViewHolder;
import app.smartsociety.ViewHolder.AnnoucementViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Annoucement extends Fragment {
    View v;

    DatabaseReference fdb;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Annoucements, AnnoucementViewHolder> adapter;
    FirebaseRecyclerAdapter<Annoucements, AnnouceViewHolder> normaladapter;
    private FirebaseRecyclerOptions<Annoucements> announce;
    FloatingActionButton fab;



    public Annoucement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_annoucement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        ((Dashboard) Objects.requireNonNull(getActivity()))
                .setActionBarTitle("Annoucement");

        fdb = FirebaseDatabase.getInstance().getReference("Annoucement");
        fab = v.findViewById(R.id.annfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent annouce = new Intent(getActivity(),Addannoucement.class);
                startActivity(annouce);
            }
        });

        if (!Common.admin){
            fab.hide();
        }



        recyclerView = v.findViewById(R.id.announcementrecyler);

        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        
        if (Common.admin){
            loadannouce();
        }
        else{
            loadnormalannouce();
        }

    }


    private void loadannouce() {
        announce = new FirebaseRecyclerOptions.Builder<Annoucements>().setQuery(
                fdb
                ,Annoucements.class).build();

        adapter = new FirebaseRecyclerAdapter<Annoucements, AnnoucementViewHolder>(announce) {


            @NonNull
            @Override
            public AnnoucementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucement,parent,false);
                return new AnnoucementViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AnnoucementViewHolder holder, int position, @NonNull Annoucements model) {

                holder.Title.setText(model.getTitle());
                holder.date.setText(model.getDate());
                holder.message.setText(model.getMessage());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i=new Intent(getActivity(),Annoucementdet.class);
                        i.putExtra("AnnoucementId",adapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void loadnormalannouce() {
        announce = new FirebaseRecyclerOptions.Builder<Annoucements>().setQuery(
                fdb
                ,Annoucements.class).build();

        normaladapter = new FirebaseRecyclerAdapter<Annoucements, AnnouceViewHolder>(announce) {


            @NonNull
            @Override
            public AnnouceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucement,parent,false);
                return new AnnouceViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AnnouceViewHolder holder, int position, @NonNull Annoucements model) {

                holder.Title.setText(model.getTitle());
                holder.date.setText(model.getDate());
                holder.message.setText(model.getMessage());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i=new Intent(getActivity(),Annoucementdet.class);
                        i.putExtra("AnnoucementId",normaladapter.getRef(position).getKey());
                        startActivity(i);
                    }
                });

            }
        };
        normaladapter.startListening();
        recyclerView.setAdapter(normaladapter);
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

    private void deleteannoucement(String key) {
        fdb.child(key).removeValue();
    }

    private void showUpdateDialog(String key, Annoucements item) {
        Intent annouce = new Intent(getActivity(),Addannoucement.class);
        annouce.putExtra("Key",key);
        startActivity(annouce);

    }


}
