package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunArrayAdapter;
import edu.ucsd.cse110.walkstatic.runs.RunsListener;

public class TeammateRunsFragment extends Fragment implements RunsListener {

    private RunArrayAdapter teammateRunAdapter;
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
        this.populateTeammates();
    }

    private void populateTeammates() {
        ListView listView = this.getActivity().findViewById(R.id.my_teammates_list);
        this.runList = this.app.getRuns().getTeammateRuns();
//        teammateListAdapter = new TeammateArrayAdapter(this.getActivity(), R.layout.teammate_request_textview, requestsList);
//        listView.setAdapter(teammateListAdapter);
//        teammateListAdapter.notifyDataSetChanged();
//        this.app.getTeammateRequests().addRequestsListener(this);
    }

    @Override
    public void teammateRunsChanged(List<Run> teammateRuns) {

    }
}
