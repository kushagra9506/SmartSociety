package app.smartsociety.AccountActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.Model.Register;
import app.smartsociety.R;
import fr.ganfra.materialspinner.MaterialSpinner;

public class RegisterActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
DatabaseReference fdb,roomdet;

FirebaseUser user;
FirebaseAuth auth;
EditText name,roomno,password,conpassword,email;
ImageView imageView;
    private Uri mImageUri;
    private StorageReference storageReference;
    private String profileimage;
    String sname,sroomno,spassword,sconpassword,semail;
    Boolean head = false;
    ProgressBar progressBar;
    RadioButton parent,child;
    Button bt;
    ArrayList<String> Roomno = new ArrayList<>();

    Common common = new Common();
    MaterialSpinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        parent = findViewById(R.id.Parent);
        bt = findViewById(R.id.regBtn);


        fdb = FirebaseDatabase.getInstance().getReference("Register");


        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("images");
        imageView = findViewById(R.id.regUserPhoto);
        progressBar = findViewById(R.id.regProgressBar);
        name = findViewById(R.id.regName);
        spinner = findViewById(R.id.regRoomno);
        password = findViewById(R.id.regPassword);
        conpassword = findViewById(R.id.regPassword2);
        email = findViewById(R.id.regMail);



        Roomno = common.roomno();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item,Roomno);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    sroomno = parent.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        parent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (parent.isChecked()){
                    head = true;
                }
                else{
                    head = false;
                }
            }
        });



    }



    public void Register(View view) {

        progressBar.setVisibility(View.VISIBLE);
        bt.setVisibility(View.INVISIBLE);
       uploadimage();
    }

    public void goback(View view) {
        finish();
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

    }


    private void createtoast(String s) {
        Toast.makeText(this,""+s,Toast.LENGTH_LONG).show();
    }
//    private void selectimage(View view) {
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);

        }
    }

    private void nowreg(){
        if (!profileimage.isEmpty()) {
            sname = name.getText().toString();
            spassword = password.getText().toString();
            sconpassword = conpassword.getText().toString();
            semail = email.getText().toString();

            if (!sname.isEmpty() && !sroomno.isEmpty() && !sconpassword.isEmpty() && !semail.isEmpty() && !spassword.isEmpty())
            {
                if (spassword.equals(sconpassword)){
                    auth.createUserWithEmailAndPassword(semail, spassword)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            if (!task.isSuccessful())
                                            {
                                                try
                                                {
                                                    throw Objects.requireNonNull(task.getException());
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword)
                                                {bt.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    createtoast("Weak Password");


                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                                {   bt.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                createtoast("Malformed Email");


                                                }
                                                catch (FirebaseAuthUserCollisionException existEmail)
                                                {   bt.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    createtoast("Email already exists");


                                                }
                                                catch (Exception e)
                                                {   bt.setVisibility(View.VISIBLE);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    createtoast("registeration failed");
                                                }
                                            }
                                            else if (task.isSuccessful()){

                                                user = auth.getCurrentUser();
                                                assert user != null;
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Register register = new Register(sname,sroomno,semail,spassword,profileimage,head,false);
                                                            fdb.child(Objects.requireNonNull(auth.getUid())).setValue(register);
                                                            bt.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            createtoast("Registration is done successfully, Please verify  your email id");
                                                            finish();
                                                            auth.signOut();
                                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                                                        }
                                                        else {
                                                            bt.setVisibility(View.VISIBLE);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            createtoast(task.getException().getMessage());
                                                        }

                                                    }
                                                });

                                            }
                                        }
                                    }
                            );
                }else{
                    bt.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    createtoast("Password did not matched");
                }

            }
            else{
                bt.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                createtoast("Please enter the complete details");
            }

        }
        else {
            bt.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            createtoast("Please select the image");
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadimage() {
        if (mImageUri != null) {

            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDialog.dismiss();
                            profileimage = uri.toString();
                            nowreg();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    mDialog.dismiss();
                    createtoast(e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setProgress((int) progress);
                }
            });
        }

    }

    public void selectimage(View view) {
        Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
}
