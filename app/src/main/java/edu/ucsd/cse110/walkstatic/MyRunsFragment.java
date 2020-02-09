package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MyRunsFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_runs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.populateMyRuns();
    }

    private void populateMyRuns(){
        ListView listView = this.getActivity().findViewById(R.id.my_runs_list);
        List<Run> listItems = Arrays.asList(new Run[]{new Run("Point Loma"), new Run("Del Mar")});
        ArrayAdapter<Run> adapter = new ArrayAdapter<Run>(this.getActivity(),
                R.layout.run_list_textview,
                listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Run run = (Run)parent.getItemAtPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Run", run);
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_myRunsFragment_to_viewRunFragment, bundle);
    }
}
