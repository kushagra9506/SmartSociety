package app.smartsociety.AccountActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import app.smartsociety.Common.Common;
import app.smartsociety.Dashboard.Dashboard;
import app.smartsociety.GateKeeper.GateDash;
import app.smartsociety.Model.Register;
import app.smartsociety.R;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    String semail,spassword;
    Button btnlogin;
    FirebaseAuth auth;
    ProgressBar progressBar;
    DatabaseReference fdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.login_mail);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress);
        btnlogin = findViewById(R.id.loginBtn);
        fdb = FirebaseDatabase.getInstance().getReference("Register");
        Paper.init(this);


        auth = FirebaseAuth.getInstance();

//



    }



    public void Register(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void Login(View view) {
        progressBar.setVisibility(View.VISIBLE);
        btnlogin.setVisibility(View.INVISIBLE);
        semail = email.getText().toString();
        spassword = password.getText().toString();
        if (!semail.isEmpty() && !spassword.isEmpty()){

            if (semail.contains("gatekeeper")){
                auth.signInWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                         progressBar.setVisibility(View.INVISIBLE);
                         btnlogin.setVisibility(View.VISIBLE);
                         startActivity(new Intent(LoginActivity.this, GateDash.class));
                         Paper.book().write(Common.user,semail);
                        }
                        else{
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidCredentialsException e)
                            {

                                progressBar.setVisibility(View.INVISIBLE);
                                btnlogin.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }else{
                try{
                    auth.signInWithEmailAndPassword(semail,spassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = auth.getCurrentUser();
                                        assert user != null;
                                        if (user.isEmailVerified())
                                        {
                                            Paper.book().write(Common.user,semail);

                                            checkadmin();

                                        }
                                        else{
                                            Paper.book().write(Common.user,semail);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnlogin.setVisibility(View.VISIBLE);
                                            createtoast("Please verify your email to access the Application");
                                        }


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        try{

                                            throw task.getException();

                                        }
                                        catch (FirebaseAuthInvalidCredentialsException e)
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnlogin.setVisibility(View.VISIBLE);
                                            Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();


                                        }
                                        catch (Exception e)
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnlogin.setVisibility(View.VISIBLE);
                                            Toast.makeText(LoginActivity.this,"",Toast.LENGTH_LONG).show();

                                        }
                                    }

                                    // ...
                                }
                            });
                }
                catch (Exception e) {


                }

            }
            }else{
            createtoast("Please enter the complete details");
        }


    }

    private void checkadmin() {
        fdb.child(Objects.requireNonNull(auth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Register register = dataSnapshot.getValue(Register.class);
                    assert register != null;
                    Common.admin = register.getAdmin();
                    Common.commonregister = register;
                    loadnext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadnext() {

        FirebaseMessaging.getInstance().subscribeToTopic("Fire");
        FirebaseMessaging.getInstance().subscribeToTopic("Intruder");
        FirebaseMessaging.getInstance().subscribeToTopic("Emergency");
        FirebaseMessaging.getInstance().subscribeToTopic("Annoucement");
        FirebaseMessaging.getInstance().subscribeToTopic("Event");
        progressBar.setVisibility(View.INVISIBLE);
        btnlogin.setVisibility(View.VISIBLE);
        startActivity(new Intent(LoginActivity.this, Dashboard.class));
        finish();
        email.setText(null);
        password.setText(null);
    }

    private void createtoast(String s) {
        Toast.makeText(this,""+s,Toast.LENGTH_LONG).show();
    }



}
