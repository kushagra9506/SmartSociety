package app.smartsociety.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import app.smartsociety.R;

public class dashboard_fragment extends Fragment {

    View v;

    CardView annoucement,event,firealarm,intruderalarm,emergency,secretarypanel,payment,complaints;

    public dashboard_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;

        annoucement = v.findViewById(R.id.dashannoucement);
        event = v.findViewById(R.id.Dashevent);
        firealarm = v.findViewById(R.id.Firealarm);
        payment = v.findViewById(R.id.DashPayment);
        complaints = v.findViewById(R.id.Complaint);
        intruderalarm = v.findViewById(R.id.Intruder);
        emergency = v.findViewById(R.id.Emergency);
        secretarypanel = v.findViewById(R.id.DashAdminPanel);




        annoucement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Annoucement annoucement = new Annoucement();
                assert getFragmentManager() != null;
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashframe,new Annoucement());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
//

    }
}
