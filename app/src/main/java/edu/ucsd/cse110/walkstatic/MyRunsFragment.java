package edu.ucsd.cse110.walkstatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunList;

public class MyRunsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private RunList runs;
    private ArrayAdapter<Run> runListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initRunViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_runs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.populateMyRuns();
        this.initAddButton();
    }

    private void populateMyRuns(){
        ListView listView = this.getActivity().findViewById(R.id.my_runs_list);
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);

        String json = sharedPreferences.getString(preferencesName, "[]");
        runs = new RunList(json);
        runListAdapter = new ArrayAdapter<Run>(this.getActivity(),
                R.layout.run_list_textview,
                runs);
        listView.setAdapter(runListAdapter);
        listView.setOnItemClickListener(this);
        runListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Run run = (Run)parent.getItemAtPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Run", run);
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_myRunsFragment_to_viewRunFragment, bundle);
    }

    private void initAddButton(){
        FloatingActionButton addButton = this.getActivity().findViewById(R.id.add_new_route);
        addButton.setOnClickListener((view) -> {
            Bundle bundle = new Bundle();
            int nextUUID = this.runs.getNextUUID();
            bundle.putInt("UUID", nextUUID);
            Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_myRunsFragment_to_editRunFragment, bundle);
        });
    }

    private void initRunViewModel(){
        RunViewModel runViewModel = new ViewModelProvider(this.getActivity()).get(RunViewModel.class);
        runViewModel.sharedRun.observe(this, run -> {
            this.addRun(run);
        });
    }

    private void addRun(Run run){
        this.runs.add(run);
        this.runListAdapter.notifyDataSetChanged();
        String preferencesName = this.getResources().getString(R.string.run_save_name);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(
                preferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(preferencesName, this.runs.toJSON()).apply();
    }
}
