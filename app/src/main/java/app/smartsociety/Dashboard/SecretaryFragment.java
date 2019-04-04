package app.smartsociety.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.smartsociety.Model.Maintainance;
import app.smartsociety.Model.Register;
import app.smartsociety.R;

public class SecretaryFragment extends Fragment {
    View v;

    EditText main;
    int amount;
    Button gener;
    ScrollView genscrol;

    DatabaseReference  gen,user;

    ArrayList<String>  userroomno = new ArrayList<>();
    Boolean aBoolean = false;
    DateFormat df = new SimpleDateFormat("dd MMM yyyy");


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

        gener = v.findViewById(R.id.addmaintain);
        genscrol = v.findViewById(R.id.generaterelat);

        user = FirebaseDatabase.getInstance().getReference("Register");
        gen = FirebaseDatabase.getInstance().getReference("Maintainance");


        gener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addroomno();
            }
        });



    }

    public void generatemain(final ArrayList<String> userroomno){
        gener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.valueOf(main.getText().toString());
                String date = df.format(Calendar.getInstance().getTime());
                for (int i=0;i<userroomno.size();i++){
                    String Key =  gen.push().getKey();
                    Maintainance maintainance = new Maintainance(Key,userroomno.get(i),null,null,null,null,date,false,amount);
                    assert Key != null;
                    gen.child(Key).setValue(maintainance);
                }



            }
        });
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
