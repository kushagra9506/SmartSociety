package app.smartsociety.Dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import app.smartsociety.Common.Common;
import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.Model.Annoucements;
import app.smartsociety.Model.Maintainance;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.AnnouceViewHolder;
import app.smartsociety.ViewHolder.AnnoucementViewHolder;
import app.smartsociety.ViewHolder.PaymentViewHolder;

public class PaymentActivity extends AppCompatActivity {

    final String mid = "kylTTT40701625824158";
    FirebaseAuth auth;
    DatabaseReference db;
    String Url = "https://intermetallic-lamp.000webhostapp.com/Paytm/generateChecksum.php";
    String callbackurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    Common common = new Common();
    DatabaseReference fdb;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Maintainance, PaymentViewHolder> adapter;
    private FirebaseRecyclerOptions<Maintainance> maintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Payment");
        recyclerView = findViewById(R.id.paymentrecycler);
        fdb = FirebaseDatabase.getInstance().getReference("Maintainance");

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        loadpayment();



    }

    private void loadpayment() {
        maintain = new FirebaseRecyclerOptions.Builder<Maintainance>().setQuery(
                fdb.orderByChild("roomno").equalTo(Common.commonregister.getRoomno())
                ,Maintainance.class).build();
        adapter = new FirebaseRecyclerAdapter<Maintainance , PaymentViewHolder>(maintain){

            @NonNull
            @Override
            public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment,viewGroup,false);
                return new PaymentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PaymentViewHolder holder, final int position, @NonNull final Maintainance model) {
                holder.maintainancetitle.setText(model.getTitle());
                holder.date.setText(model.getDate());
                holder.cost.setText(String.valueOf(model.getAmount()));

                if (model.getPaid()){
                    holder.btn.setText("Paid");
                    holder.btn.setEnabled(false);
                }
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (model.getPaid()){
                            showupdate(model);
                        }
                        else {
                            Toast.makeText(PaymentActivity.this,"You have not paid the maintainance",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startpayment(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void showupdate(final Maintainance type) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Details");
        LayoutInflater inflater = this.getLayoutInflater();
        final View updatevisitor = inflater.inflate(R.layout.paymenthistory,null);
        final TextView payername,transaction,date,roomno,paid ;
        payername = updatevisitor.findViewById(R.id.payername);
        transaction = updatevisitor.findViewById(R.id.transactionid);
        date = updatevisitor.findViewById(R.id.hisPaymentdate);
        roomno = updatevisitor.findViewById(R.id.hisroomno);
        paid = updatevisitor.findViewById(R.id.paidornot);









        alertDialog.setView(updatevisitor);
        alertDialog.setIcon(R.drawable.payment);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //New Cat will be added

                payername.setText(type.getPaidby());
                transaction.setText(type.getTransactionid());
                date.setText(type.getDate());
                roomno.setText(type.getRoomno());
                if (type.getPaid()){
                    paid.setText("Paid");
                }
                else paid.setText("Not paid");


            }
        });

        alertDialog.show();
    }
    private void startpayment(final String key, final Maintainance item) {
        getpermission();

        final String orderId = UUID.randomUUID().toString().substring(0,28);
        final String cust_id = FirebaseAuth.getInstance().getUid();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try
                {
                    JSONObject object = new JSONObject(response);
                    if (object.has("CHECKSUMHASH")){
                        String CHECKSUMASH= object.getString("CHECKSUMHASH");
                        PaytmPGService paytmPGService = PaytmPGService.getStagingService();
                        HashMap<String, String> paramMap = new HashMap<String,String>();
                        paramMap.put( "MID" , mid);
                        paramMap.put( "ORDER_ID" , orderId);
                        paramMap.put( "CUST_ID" , cust_id);
                        paramMap.put( "CHANNEL_ID" , "WAP");
                        paramMap.put( "TXN_AMOUNT" , String.valueOf(item.getAmount()));
                        paramMap.put( "WEBSITE" , "WEBSTAGING");
                        paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                        paramMap.put( "CALLBACK_URL", callbackurl);
                        paramMap.put( "CHECKSUMHASH" , CHECKSUMASH);
                        PaytmOrder order = new PaytmOrder(paramMap);

                        paytmPGService.initialize(order,null);
                        paytmPGService.startPaymentTransaction(PaymentActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
//                                common.createToast(""+inResponse.toString(),PaymentActivity.this);
                                if (inResponse.getString("STATUS").equals("TXT_SUCCESS")){
                                    Maintainance nmain = new Maintainance(item.getKey(),item.getRoomno(),inResponse.getString("TXNID"),Common.commonregister.getName()
                                            ,inResponse.getString("TXNDATE"),inResponse.getString("ORDERID"),item.getDate(),item.getTitle(),auth.getUid(),true,item.getAmount());
                                    fdb.child(key).setValue(nmain);
                                }
                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction cancelled" , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction Cancelled" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                common.createToast("Something Went wrong!!",PaymentActivity.this);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramMap = new HashMap<String,String>();
                paramMap.put( "MID" , mid);
                paramMap.put( "ORDER_ID" , orderId);
                paramMap.put( "CUST_ID" , cust_id);
                paramMap.put( "CHANNEL_ID" , "WAP");
                paramMap.put( "TXN_AMOUNT" , String.valueOf(item.getAmount()));
                paramMap.put( "WEBSITE" , "WEBSTAGING");
                paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
                paramMap.put( "CALLBACK_URL", callbackurl);
                return paramMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void showdialog(String key) {

    }

    public void getpermission(){
        if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
    }
}
