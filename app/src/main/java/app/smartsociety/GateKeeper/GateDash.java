package app.smartsociety.GateKeeper;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    RelativeLayout addvisitor,visitordet;
    EditText name,contact,roomno;
    ImageView imageView;
    final int CAMERA_PICK_CODE_REQUEST = 71;
    StorageReference storageReference;
    String image;
    DatabaseReference db;
    Date currentTime = Calendar.getInstance().getTime();

    private EditText mSearchField;

    Common common = new Common();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Visitor, VisitorViewHolder> adapter;
    private FirebaseRecyclerOptions<Visitor> visitor;

    FirebaseRecyclerAdapter<Visitor, VisitorViewHolder> searchadapter;
    private FirebaseRecyclerOptions<Visitor> searchvisitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_dash);

        addvisitor = findViewById(R.id.visitorlayout);
        visitordet = findViewById(R.id.visitingdetlayout);
        imageView =  findViewById(R.id.addvisitorimage);
        name = findViewById(R.id.addvisitorname);
        contact = findViewById(R.id.addvisitorcontact);
        roomno = findViewById(R.id.addvisitingroom);
        mSearchField = findViewById(R.id.search_field);

        db = FirebaseDatabase.getInstance().getReference("Visitor");

        recyclerView = findViewById(R.id.announcementrecyler);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);




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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucement,parent,false);
                return new VisitorViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VisitorViewHolder holder, int position, @NonNull Visitor model) {

                holder.name.setText(model.getName());
                holder.contact.setText(model.getContact());
                holder.roomno.setText(model.getRoomno());
              holder.intime.setText(""+model.getIntime());
              if (model.getOutime() != null){

              }
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.personimage);

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
    }

    public void Submit(View view) {
        Visitor visitor = new Visitor(name.getText().toString(),image,contact.getText().toString(),roomno.getText().toString(),currentTime,null);
        db.push().setValue(visitor);
    }

    public void selectimage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_PICK_CODE_REQUEST);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.AddOut)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey());
        }
        else{
            showupdate(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }

    private void showupdate(String key, Visitor item) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CAMERA_PICK_CODE_REQUEST && resultCode == RESULT_OK){
            assert data != null;
            Uri uri = data.getData();
            uploadimage(uri);
        }
    }

     public void uploadimage(Uri uri){
         storageReference = FirebaseStorage.getInstance().getReference("Visitorimages").child(Objects.requireNonNull(uri.getLastPathSegment()));
         storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               image = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl().toString();
                 Toast.makeText(GateDash.this,"Uploading finished",Toast.LENGTH_LONG).show();

             }
         });
    }

    private void showUpdateDialog(String key) {
       db.child(key).child("outtime").setValue(currentTime);
       common.createToast("Updated",this);
    }


    public void Search(View view) {
        String searchText = mSearchField.getText().toString();

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucement,parent,false);
                return new VisitorViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VisitorViewHolder holder, int position, @NonNull Visitor model) {

                holder.name.setText(model.getName());
                holder.contact.setText(model.getContact());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.personimage);
                String in = "" + model.getIntime();
                holder.intime.setText(in);
                if (model.getOutime() != null){
                    holder.outime.setText(""+model.getOutime());
                }
                else holder.outime.setVisibility(View.GONE);
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.personimage);

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
