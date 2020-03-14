package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunsListener;
import edu.ucsd.cse110.walkstatic.runs.TeammateRunArrayAdapter;

public class TeammateRunsFragment extends Fragment implements RunsListener, AdapterView.OnItemClickListener {

    private TeammateRunArrayAdapter teammateRunAdapter;
    private List<Run> runList;
    private Walkstatic app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_runs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.app = new Walkstatic(this.getContext());
        this.populateTeammateRuns();
        this.displayRuns();
        this.app.getTeam().addTeamListener(this::displayRuns);
    }

    private void populateTeammateRuns() {
        ListView listView = this.getActivity().findViewById(R.id.my_runs_list);
        this.runList = this.app.getRuns().getTeammateRuns();
        teammateRunAdapter = new TeammateRunArrayAdapter(this.getActivity(), R.layout.teammate_request_textview, this.runList);
        listView.setAdapter(teammateRunAdapter);
        listView.setOnItemClickListener(this);
        teammateRunAdapter.notifyDataSetChanged();
        this.app.getRuns().addRunsListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Run run = (Run)parent.getItemAtPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Run", run);
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_teammateRunsFragment_to_viewRunFragment, bundle);
    }

    public void displayRuns() {
        ListView listView = this.getActivity().findViewById(R.id.my_runs_list);
        listView.setVisibility(this.app.getTeam().isUserOnTeam() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void myRunsChanged(List<Run> myRuns) {

    }

    @Override
    public void teammateRunsChanged(List<Run> teammateRuns) {
        this.runList.clear();
        this.runList.addAll(teammateRuns);
        this.teammateRunAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }
}
