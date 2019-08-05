package app.smartsociety.Dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.smartsociety.Common.Common;
import app.smartsociety.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {
    View v;
    TextView name,email,pin,phone,roomno,respass;
    CircleImageView image;
    Button redit;
    ImageButton imageButton;
    Boolean b=false;

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        name = v.findViewById(R.id.setprofname);
        email = v.findViewById(R.id.setprofemail);
        pin = v.findViewById(R.id.setprofmpin);
        phone = v.findViewById(R.id.setprofphone);
        roomno = v.findViewById(R.id.setprofroomno);
        image = v.findViewById(R.id.setprofileimage);
        redit = v.findViewById(R.id.seteditbtn);
        respass = v.findViewById(R.id.setresetpass);
        imageButton = v.findViewById(R.id.seteye);


        name.setText(Common.commonregister.getName());
        email.setText(Common.commonregister.getEmail());
        roomno.setText(Common.commonregister.getRoomno());
        phone.setText(Common.commonregister.getPhone());
        Picasso.get().load(Common.commonregister.getImage()).into(image);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b){
                    showupdate();
                }
                else{
                    pin.setText("*******");
                    b = true;
                }

            }
        });




        redit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        respass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void showupdate() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Verify the password");
        LayoutInflater inflater = this.getLayoutInflater();
        final View updatevisitor = inflater.inflate(R.layout.showmpin,null);
        final EditText updatevisitroom;
        updatevisitroom = updatevisitor.findViewById(R.id.mpasswordverify);









        alertDialog.setView(updatevisitor);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //New Cat will be added
                if (updatevisitroom.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Please enter the Room no",Toast.LENGTH_SHORT).show();
                }else if (updatevisitroom.getText().toString().equals(Common.commonregister.getPassword())){
                   dialog.dismiss();
                   pin.setText(Common.commonregister.getMpin());
                   b = false;
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
}
