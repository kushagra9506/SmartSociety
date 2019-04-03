package app.smartsociety.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import app.smartsociety.AccountActivity.LoginActivity;
import app.smartsociety.Common.Common;
import app.smartsociety.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference fdb;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseMessaging.getInstance().unsubscribeFromTopic("Fire");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Intruder");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Emergency");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Annoucement");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Event");


        auth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView imageView = headerView.findViewById(R.id.navprofileimage);

        Picasso.get().load(Common.commonregister.getImage()).into(imageView);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navName);
        TextView navEmail = headerView.findViewById(R.id.navEmail);
        navUsername.setText(Common.commonregister.getName());
        navEmail.setText(Common.commonregister.getEmail());
        if (Common.admin){
            Menu navmenu = navigationView.getMenu();
            navmenu.findItem(R.id.navadmin).setVisible(true);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.dashframe,new dashboard_fragment());
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout) {
            auth.signOut();
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Fire");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Intruder");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Emergency");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Annoucement");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("Event");

            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navannoucement) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dashframe,new Annoucement());
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        } else if (id == R.id.navevent) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.dashframe,new EventActivity());
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        } else if (id == R.id.navpayment) {
            startActivity(new Intent(this,PaymentActivity.class));
        } else if (id == R.id.navsetting) {

        } else if (id == R.id.navmembers) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String your_title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(your_title);
    }

    public void distresscall(View view) {
    }
}
