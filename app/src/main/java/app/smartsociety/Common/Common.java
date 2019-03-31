package app.smartsociety.Common;

import android.content.Context;
import android.widget.Toast;

public class Common {

    public static Boolean admin = false;


    public static final String Update="Update";
    public static final String Delete="Delete";
    public static final String AddOut ="Set OutTime";


    public void createToast(String message, Context context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
