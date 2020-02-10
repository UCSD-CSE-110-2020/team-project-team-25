package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;

public class EditRunFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.done_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            this.saveRun();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRun(){
        EditText runName = this.getActivity().findViewById(R.id.run_name_text);
        EditText startingPoint = this.getActivity().findViewById(R.id.starting_point_text);
        String runText = runName.getText().toString() + startingPoint.getText();

        RunViewModel runViewModel = new ViewModelProvider(this.getActivity()).get(RunViewModel.class);
        runViewModel.setRun(new Run(this.safeGetUUID(), runName.getText().toString()));
        runName.clearFocus();
        startingPoint.clearFocus();
        Navigation.findNavController(this.getView()).navigateUp();
    }

    private int safeGetUUID(){
        if(getArguments() == null){
            return 0;
        }
        return getArguments().getInt("UUID", 0);
    }
}
