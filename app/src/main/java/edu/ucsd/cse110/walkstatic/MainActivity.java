package edu.ucsd.cse110.walkstatic;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavBar();

        createFakeRuns();

        SharedPreferences sharedPreferences = getSharedPreferences("userHeight", MODE_PRIVATE);

        // If first run
        if (sharedPreferences.getBoolean("firstUse", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstUse", false);
            editor.commit();

            // Set default value for height if user closes app w/o entering height
            editor.putString("height","65");
            editor.apply();
            Toast.makeText(MainActivity.this, sharedPreferences.getString("height","65") + " Inches Saved",Toast.LENGTH_SHORT).show();

            // Prompt user for height
            promptHeight(MainActivity.this);
        }
        else{
            String uHeight = sharedPreferences.getString("height","65");
            Toast.makeText(MainActivity.this, uHeight + " loaded",Toast.LENGTH_SHORT).show();
        }
    }

    private void createFakeRuns(){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<Run> runs = new ArrayList<Run>();
        runs.add(new Run("Point Loma"));
        runs.add(new Run("Mission Trails"));
        sharedPreferences.edit().putString("runs", gson.toJson(runs)).apply();
    }

    private void setupNavBar(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_graph).build();
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    protected void promptHeight(Context c) {
        final EditText taskEditText = new EditText(c);
        SharedPreferences sharedPreferences = getSharedPreferences("userHeight", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter your height in inches")
                .setMessage("Your height is used to calculate the number of miles you've traveled")
                .setView(taskEditText)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString();
                        editor.putString("height",task);
                        editor.apply();
                        Toast.makeText(c,task + " Inches Saved",Toast.LENGTH_SHORT).show();
                    }
                })
                // Dont need cancel button atm
                // .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), findViewById(R.id.drawer_layout));
    }


}
