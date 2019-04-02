package app.smartsociety.GateKeeper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.Dashboard.Addannoucement;
import app.smartsociety.Model.Annoucements;
import app.smartsociety.Model.Visitor;
import app.smartsociety.R;
import app.smartsociety.ViewHolder.VisitorViewHolder;

public class GateDash extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 71;
    RelativeLayout addvisitor,visitordet;
    EditText name,contact,roomno,updatevisitroom;
    ImageView imageView;
    final int CAMERA_PICK_CODE_REQUEST = 71;

    String image;
    Bitmap photo;
    DatabaseReference db;
    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");

    private EditText mSearchField;

    Common common = new Common();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Visitor, VisitorViewHolder> adapter;
    private FirebaseRecyclerOptions<Visitor> visitor;


    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference mstorage;


    Uri uri;

    FirebaseRecyclerAdapter<Visitor, VisitorViewHolder> searchadapter;
    private FirebaseRecyclerOptions<Visitor> searchvisitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_dash);

        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        addvisitor = findViewById(R.id.visitorlayout);
        visitordet = findViewById(R.id.visitingdetlayout);
        imageView =  findViewById(R.id.addvisitorimage);
        name = findViewById(R.id.addvisitorname);
        contact = findViewById(R.id.addvisitorcontact);
        roomno = findViewById(R.id.addvisitingroom);
        mSearchField = findViewById(R.id.search_field);

        db = FirebaseDatabase.getInstance().getReference("Visitor");
        mstorage = storage.getReference("Visitorimages/");

        recyclerView = findViewById(R.id.visitorrecycler);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_PICK_CODE_REQUEST);



                } else {
                    requestPermission();
                }
            }
        });




        loaddetail();




    }

    private void loaddetail() {
        visitor = new FirebaseRecyclerOptions.Builder<Visitor>().setQuery(
                db
                ,Visitor.class).build();

        adapter = new FirebaseRecyclerAdapter<Visitor, VisitorViewHolder>(visitor) {


            @NonNull
            @Override
            public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor,parent,false);
                return new VisitorViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VisitorViewHolder holder, int position, @NonNull Visitor model) {

                holder.name.setText(model.getName());
                holder.contact.setText(model.getContact());
                holder.roomno.setText(model.getRoomno());
              holder.intime.setText(model.getIntime());
              if (model.getOuttime() != null){

                  holder.outime.setText(model.getOuttime());
                  holder.outime.setVisibility(View.VISIBLE);
              }
                Picasso.get().load(model.getImage()).into(holder.personimage);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public void addvisitor(View view) {
        visitordet.setVisibility(View.GONE);
        addvisitor.setVisibility(View.VISIBLE);

    }

    public void showvisitor(View view) {
        visitordet.setVisibility(View.VISIBLE);
        addvisitor.setVisibility(View.GONE);

        loaddetail();
    }

    public void Submit(View view) {
       String date = df.format(Calendar.getInstance().getTime());
        Visitor visitor = new Visitor(name.getText().toString(),image,contact.getText().toString(),roomno.getText().toString(),date,null);
        db.push().setValue(visitor);

        common.createToast("Details, Submitted",GateDash.this);

        name.setText("");
        photo = null;
        imageView.setImageResource(R.drawable.addvisitor);
        contact.setText("");
        roomno.setText("");
        date = null;

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GateDash.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.AddOut)){
            if (adapter.getItem(item.getOrder()).getOuttime() == null){
                showUpdateDialog(adapter.getRef(item.getOrder()).getKey());
            }
            else common.createToast("Already  updated",this);

        }
        else{
            showupdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }

    private void showupdate(String key, final Visitor item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GateDash.this);
        alertDialog.setTitle("Add Visit");
        LayoutInflater inflater = this.getLayoutInflater();
        View updatevisitor = inflater.inflate(R.layout.updatevisitor,null);
        updatevisitroom = updatevisitor.findViewById(R.id.updatevisiterroomno);






        alertDialog.setView(updatevisitor);
        alertDialog.setIcon(R.drawable.add);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //New Cat will be added
                if (updatevisitroom.getText().toString().isEmpty()){
                    Toast.makeText(GateDash.this,"Please enter the Room no",Toast.LENGTH_SHORT).show();
                }else {
                   String date = df.format(Calendar.getInstance().getTime());
                    Visitor visitor = new Visitor(item.getName(),item.getImage(),item.getContact(),
                           updatevisitroom.getText().toString(),date,null);

                    db.push().setValue(visitor);
                    common.createToast("Added",GateDash.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==CAMERA_PICK_CODE_REQUEST && resultCode == RESULT_OK ){

            photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageView.setImageBitmap(photo);
            uploadimage();
//            File file = new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg");
//             uri = Uri.fromFile(file);
//
//            Picasso.with(getBaseContext()).load(uri).into(imageView);
//            uploadimage();


        }
    }

     public void uploadimage(){

        progressDialog.setMessage("Uploading image Please wait ...");
        progressDialog.show();

         ByteArrayOutputStream stream = new ByteArrayOutputStream();
         photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
         byte[] b = stream.toByteArray();
         final StorageReference im = mstorage.child(String.valueOf(System.currentTimeMillis()));
         im.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                 im.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         image = uri.toString();
                        common.createToast("Uploaded",GateDash.this);
                         progressDialog.dismiss();
                     }
                 });

             }
         });

//        common.createToast(String.valueOf(uri),GateDash.this);
//         final StorageReference storageReference = mstorage.child("visitorimage/").child(Objects.requireNonNull(uri.getLastPathSegment()));
//         storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//             @Override
//             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                 storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                     @Override
//                     public void onSuccess(Uri uri) {
//
//                         image = uri.toString();
//                     }
//                 });
//             }
//         });
    }


    private void showUpdateDialog(String key) {
        String date = df.format(Calendar.getInstance().getTime());
        db.child(key).child("outtime").setValue(date);
       common.createToast("Updated",this);
    }


    public void Search(View view) {
        String searchText = mSearchField.getText().toString();
        if (searchText.isEmpty()){
            loaddetail();
        }
        else
        firebaseUserSearch(searchText);

    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = db.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        visitor = new FirebaseRecyclerOptions.Builder<Visitor>().setQuery(
                firebaseSearchQuery
                ,Visitor.class).build();
        adapter = new FirebaseRecyclerAdapter<Visitor, VisitorViewHolder>(visitor) {


            @NonNull
            @Override
            public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor,parent,false);
                return new VisitorViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VisitorViewHolder holder, int position, @NonNull Visitor model) {

                holder.name.setText(model.getName());
                holder.contact.setText(model.getContact());
                Picasso.get().load(model.getImage()).into(holder.personimage);

                holder.roomno.setText(model.getRoomno());
                holder.intime.setText(model.getIntime());
                if (model.getOuttime() != null){
                    holder.outime.setText(model.getOuttime());
                }
                else
                    holder.outime.setVisibility(View.GONE);
                Picasso.get().load(model.getImage()).into(holder.personimage);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
