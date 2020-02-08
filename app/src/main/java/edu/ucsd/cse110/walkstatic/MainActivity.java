package edu.ucsd.cse110.walkstatic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    private static String fitnessServiceKey = "GOOGLE_FIT";
    public static void setFitnessServiceKey(String newKey) {
        fitnessServiceKey = newKey;
    }


    private ActionBarDrawerToggle toggle;

    private static final String TAG = "StepCountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavBar();

    }

    private void setupNavBar(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawer,  R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        //this.populateNavList();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp(){
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

//    private void populateNavList(){
//        ListView listView = findViewById(R.id.list_drawer);
//        List<String> listItems = Arrays.asList(new String[]{"Current Run", "My Runs"});
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.hamburger_textview,
//                listItems);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
