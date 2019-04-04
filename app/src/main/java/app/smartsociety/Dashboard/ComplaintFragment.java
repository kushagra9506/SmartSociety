package app.smartsociety.Dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.GateKeeper.GateDash;
import app.smartsociety.Model.Complaint;
import app.smartsociety.Model.Visitor;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.ComplaintViewHolder;

public class ComplaintFragment extends Fragment {
    View v;
    CardView unresolved,resolved,percompliant;

    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");

    DatabaseReference fdb;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Complaint, ComplaintViewHolder> adapter;
    FirebaseAuth auth;
    Common common = new Common();
    
    FloatingActionButton com;

     FirebaseRecyclerOptions<Complaint> complaint;
    private EditText updatevisitroom;

    public ComplaintFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complaint, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;

        unresolved = v.findViewById(R.id.Unresolved);
        resolved = v.findViewById(R.id.Resolved);
        percompliant = v.findViewById(R.id.personalcomplaint);
        auth = FirebaseAuth.getInstance();
        
        com = v.findViewById(R.id.comfab);

        fdb = FirebaseDatabase
                .getInstance().getReference("Complaints");

        recyclerView = v.findViewById(R.id.complaintrecycler);

        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showupdate(getContext());
            }
        });

        complaint = new FirebaseRecyclerOptions.Builder<Complaint>().setQuery(
                fdb.orderByChild("resolved").equalTo(false)
                ,Complaint.class).build();
        loadcomplaint(complaint);

        unresolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                complaint = new FirebaseRecyclerOptions.Builder<Complaint>().setQuery(
                        fdb.orderByChild("resolved").equalTo(false)
                        ,Complaint.class).build();
                
                loadcomplaint(complaint);

            }
        });

        resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complaint = new FirebaseRecyclerOptions.Builder<Complaint>().setQuery(
                        fdb.orderByChild("resolved").equalTo(true)
                        ,Complaint.class).build();

                loadcomplaint(complaint);
            }
        });

        percompliant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                complaint = new FirebaseRecyclerOptions.Builder<Complaint>().setQuery(
                        fdb.orderByChild("uid").equalTo(auth.getUid())
                        ,Complaint.class).build();

                loadcomplaint(complaint);
            }
        });
    }

    private void showupdate(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialog.setTitle("Add Complaint");
        LayoutInflater inflater = this.getLayoutInflater();
        View newcomplaint = inflater.inflate(R.layout.addcomplaint,null);
        updatevisitroom = newcomplaint.findViewById(R.id.addcomplaint);






        alertDialog.setView(newcomplaint);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //New Cat will be added
                if (updatevisitroom.getText().toString().isEmpty()){
                    Toast.makeText(context,"Please enter the Room no",Toast.LENGTH_SHORT).show();
                }else {
                    dialog.dismiss();
                    String date = df.format(Calendar.getInstance().getTime());
                    Complaint complaint = new Complaint(updatevisitroom.getText().toString(),date,auth.getUid(),Common.commonregister.getImage(),false);


                    fdb.push().setValue(complaint);
                    common.sendNotification("Complaint","There is new Complaint",context);
                    common.createToast("Added",context);
                }

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadcomplaint(FirebaseRecyclerOptions<Complaint> complaint) {

        adapter = new FirebaseRecyclerAdapter<Complaint, ComplaintViewHolder>(complaint) {


            @NonNull
            @Override
            public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint,parent,false);
                return new ComplaintViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ComplaintViewHolder holder, final int position, @NonNull Complaint model) {

                holder.name.setText(model.getComplaint());
                holder.date.setText(model.getDate());
                Picasso.get().load(model.getImage()).into(holder.imageViewl);
                
                if (model.getResolved()){
                    holder.resolved.setVisibility(View.GONE);
                }
                else{
                    if (model.getUid().equals(auth.getUid())) {
                        holder.resolved.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fdb.child(Objects.requireNonNull(adapter.getRef(position).getKey())).child("resolved").setValue(true);
                            }
                        });
                    }
                    else{
                    holder.resolved.setVisibility(View.INVISIBLE);
                    }


                }
               
                

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
