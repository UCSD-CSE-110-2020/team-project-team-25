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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunList;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavBar();

//        createFakeRuns();

        SharedPreferences sharedPreferences = getSharedPreferences("userHeight", MODE_PRIVATE);
        if (sharedPreferences.getString("height","-1").equals("-1")) {
            promptHeight(MainActivity.this);
        }
        this.addViewModelListener();
    }

    private void createFakeRuns(){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        RunList runs = new RunList();
        runs.add(new Run().setName("Point Loma"));
        runs.add(new Run().setName("Mission Trails"));
        sharedPreferences.edit().putString("runs", runs.toJSON()).apply();

        preferencesName = this.getResources().getString(R.string.current_run);
        sharedPreferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
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
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SharedPreferences sharedPreferences = getSharedPreferences("userHeight", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        taskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String task = taskEditText.getText().toString();
                try { Integer.parseInt(task); }
                catch (Exception e){ taskEditText.setError("Bad value"); }
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter your height in inches")
                .setMessage("Your height is used to calculate the number of miles you've traveled")
                .setView(taskEditText)
                .setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = taskEditText.getText().toString();
                        try {
                            Integer.parseInt(task);
                            editor.putString("height", task);
                            editor.apply();
                        } catch (NumberFormatException exception) {
                            promptHeight(c);
                        }
                    }
                })
                // Dont need cancel button atm
                // .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), findViewById(R.id.drawer_layout));
    }

    private void addViewModelListener(){
        RunViewModel runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        runViewModel.sharedRun.observe(this, this::addRun);
    }

    private void addRun(Run run){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        String runJSON = sharedPreferences.getString(preferencesName, "");
        RunList runs = new RunList(runJSON);
        runs.add(run);
        sharedPreferences.edit().putString(preferencesName, runs.toJSON()).commit();
    }
}
