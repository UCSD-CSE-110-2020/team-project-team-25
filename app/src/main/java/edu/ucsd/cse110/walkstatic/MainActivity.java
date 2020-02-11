package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    }

    private void createFakeRuns(){
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        RunList runs = new RunList();
        runs.add(new Run("Point Loma"));
        runs.add(new Run("Mission Trails"));
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

    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), findViewById(R.id.drawer_layout));
    }


}
