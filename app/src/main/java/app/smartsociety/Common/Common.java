package app.smartsociety.Common;

import android.content.Context;
import android.util.Log;
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

import app.smartsociety.Model.Register;

public class Common {

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    public static Boolean admin = false;


    public static final String Update="Update";
    public static final String Delete="Delete";
    public static final String AddOut ="Set OutTime";
    public static Register commonregister;


    public void createToast(String message, Context context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public void sendNotification(String type, String body,Context context) {
        mRequestQue = Volley.newRequestQueue(Objects.requireNonNull(context));
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
                    header.put("authorization","key=\n" + "AIzaSyCGd2gZbgcDbQVk1tm3yTq_w8JYcSHBSdk");
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
