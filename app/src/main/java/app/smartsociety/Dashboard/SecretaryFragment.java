package app.smartsociety.Dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.smartsociety.Common.Common;
import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.Model.Maintainance;
import app.smartsociety.Model.Register;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.PaymentViewHolder;
import app.smartsociety.ViewHolder.Paymenthistoryholder;

public class SecretaryFragment extends Fragment {
    View v;

    EditText main,det;
    Long amount;
    Button gener;
    ScrollView genscrol;


    CardView generatepay,paymenthis;
    DatabaseReference  gen,user;
    Common common = new Common();
    ArrayList<String>  userroomno = new ArrayList<>();
    Boolean aBoolean = false;
    DateFormat df = new SimpleDateFormat("dd MMM yyyy");
    RelativeLayout paymenthistory;



    DatabaseReference fdb;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Maintainance, Paymenthistoryholder> adapter;
    private FirebaseRecyclerOptions<Maintainance> maintain;
    public SecretaryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.secretary_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;
        main = v.findViewById(R.id.maintainaince);
        det = v.findViewById(R.id.maintainaincedet);

        generatepay = v.findViewById(R.id.generatepay);
        paymenthis = v.findViewById(R.id.expenses);
        gener = v.findViewById(R.id.addmaintain);
        genscrol = v.findViewById(R.id.generaterelat);
        paymenthistory = v.findViewById(R.id.paymenthislay);


        user = FirebaseDatabase.getInstance().getReference("Register");
        gen = FirebaseDatabase.getInstance().getReference("Maintainance");


        gener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addroomno();
            }
        });

        generatepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymenthistory.setVisibility(View.GONE);
                genscrol.setVisibility(View.VISIBLE);
            }
        });
        paymenthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymenthistory.setVisibility(View.VISIBLE);
                genscrol.setVisibility(View.GONE);
            }
        });

        recyclerView = v.findViewById(R.id.paymenthistoryrecycler);
        fdb = FirebaseDatabase.getInstance().getReference("Maintainance");

        layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        loadpayment();


    }

    private void loadpayment() {
        maintain = new FirebaseRecyclerOptions.Builder<Maintainance>().setQuery(
                fdb
                ,Maintainance.class).build();
        adapter = new FirebaseRecyclerAdapter<Maintainance , Paymenthistoryholder>(maintain){

            @NonNull
            @Override
            public Paymenthistoryholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.paymenthistory,viewGroup,false);
                return new Paymenthistoryholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull Paymenthistoryholder holder, final int position, @NonNull final Maintainance model) {

                if (model.getPaid()){
                    holder.paid.setText("Paid");
                    holder.transaction.setText(model.getTransactionid());
                    holder.paymentdate.setText(model.getPaymentdate());
                    holder.name.setText(model.getPaidby());
                }
                else{
                    holder.paid.setText("Not paid");
                    holder.transaction.setText("There is no  transaction id");
                    holder.name.setVisibility(View.GONE);
                    holder.paymentdate.setText(View.GONE);

                }

                holder.roomno.setText(model.getRoomno());


            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    public void generatemain(final ArrayList<String> userroomno){


                if (!det.getText().toString().isEmpty() && !main.getText().toString().isEmpty()){
                    final String title = det.getText().toString();
                    amount = Long.valueOf(main.getText().toString());
                    String date = df.format(Calendar.getInstance().getTime());
                    for (int i=0;i<userroomno.size();i++){
                        String Key =  gen.push().getKey();
                        Maintainance maintainance = new Maintainance(Key,userroomno.get(i),null,null,null,null,date,title,null,false,amount);
                        assert Key != null;
                        gen.child(Key).setValue(maintainance);

                    }

                    common.createToast("Generated",getActivity());
                }
                else{

                    common.createToast("Please enter the complete the details",getActivity());
                }



    }
    private void addroomno() {

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Register register = dataSnapshot1.getValue(Register.class);
                    assert register != null;
                    if (register.getHead()){
                        userroomno.add(register.getRoomno());
                    }
                }
                generatemain(userroomno);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
