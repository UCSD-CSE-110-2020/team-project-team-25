package edu.ucsd.cse110.walkstatic;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Handler;
import android.text.InputType;
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

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavBar();

        createFakeRuns();

        SharedPreferences sharedPreferences = getSharedPreferences("userHeight", MODE_PRIVATE);


        if (sharedPreferences.getString("height","-1").equals("-1") || sharedPreferences.getString("height","-1").equals("")) {
            promptHeight(MainActivity.this);
        }
    }

    private void createFakeRuns(){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        RunList runs = new RunList();
        runs.add(new Run(runs.getNextUUID(),"Point Loma"));
        runs.add(new Run(runs.getNextUUID(),"Mission Trails"));
        sharedPreferences.edit().putString("runs", runs.toJSON()).apply();
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
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
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
                        if(task.equals("") || task.equals("-1")) promptHeight(c);
                        else {
                            editor.putString("height", task);
                            editor.apply();
                        }
                       // Toast.makeText(c,task + " Inches Saved",Toast.LENGTH_SHORT).show();
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
