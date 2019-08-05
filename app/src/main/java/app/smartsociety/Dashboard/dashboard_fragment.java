package app.smartsociety.Dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.R;

public class dashboard_fragment extends Fragment {

    View v;

    CardView annoucement,event,firealarm,intruderalarm,emergency,secretarypanel,payment,complaints;
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    Common common = new Common();

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
        ((Dashboard) Objects.requireNonNull(getActivity()))
                .setActionBarTitle("Dashboard");
        annoucement = v.findViewById(R.id.dashannoucement);
        event = v.findViewById(R.id.Dashevent);
        firealarm = v.findViewById(R.id.Firealarm);
        payment = v.findViewById(R.id.DashPayment);
        complaints = v.findViewById(R.id.Complaint);
        intruderalarm = v.findViewById(R.id.Intruder);
        emergency = v.findViewById(R.id.Emergency);
        secretarypanel = v.findViewById(R.id.DashAdminPanel);


        if(!Common.admin){
            secretarypanel.setVisibility(View.GONE);
        }

        mRequestQue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        complaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashframe,new ComplaintFragment());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),PaymentActivity.class));
            }
        });


        secretarypanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashframe,new SecretaryFragment());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        annoucement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assert getFragmentManager() != null;
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashframe,new Annoucement());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Annoucement annoucement = new Annoucement();
                assert getFragmentManager() != null;
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.dashframe,new EventActivity());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        
        firealarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showupdate("Fire");

            }
        });
        intruderalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showupdate("Intruder");

            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showupdate("Emergency");

            }
        });
//

    }

    private void showupdate(final String type) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Verify the password");
        LayoutInflater inflater = this.getLayoutInflater();
        final View updatevisitor = inflater.inflate(R.layout.password,null);
        final EditText  updatevisitroom;
        updatevisitroom = updatevisitor.findViewById(R.id.mpinverify);









        alertDialog.setView(updatevisitor);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //New Cat will be added

                if (updatevisitroom.getText().toString().isEmpty()){
                        Toast.makeText(getContext(),"Please enter the Room no",Toast.LENGTH_SHORT).show();
                }else if (updatevisitroom.getText().toString().equals(Common.commonregister.getMpin())){
                    dialog.dismiss();
                    String body = "There is "+ type +" at " + Common.commonregister.getRoomno();
                    common.createToast("Notified",getContext());
                    sendNotification(type,body);
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
    private void sendNotification(final String type, String body) {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+type);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",type);
            notificationObj.put("body",body);
            json.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyB92EFgKJy33tubZrwnNli0o6jCMq-JOJ8");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}
