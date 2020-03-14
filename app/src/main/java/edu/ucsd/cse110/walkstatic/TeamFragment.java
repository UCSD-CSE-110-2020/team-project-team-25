package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.walkstatic.teammate.Team;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateArrayAdapter;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestsListener;

public class TeamFragment extends Fragment implements TeammateRequestsListener {
    private TeammateArrayAdapter teammateListAdapter;
    private List<Object> teammateList;
    private Walkstatic app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.app = new Walkstatic(this.getContext());
        this.populateTeammates();
        this.app.getTeam().addTeamListener(this::updateList);
    }

    private void populateTeammates() {
        ListView teammatesListView = this.requireActivity().findViewById(R.id.my_teammates_list);

        Team team = this.app.getTeam();
        this.teammateList = new ArrayList<>();
        this.teammateList.addAll(team.getTeammates());
        this.teammateList.addAll(this.app.getTeammateRequests().getRequests());

        teammateListAdapter = new TeammateArrayAdapter(this.getActivity(),
                R.layout.teammate_textview, this.teammateList);
        teammatesListView.setAdapter(teammateListAdapter);
        teammatesListView.setOnItemClickListener(this::onItemClick);
        teammateListAdapter.notifyDataSetChanged();

        this.app.getTeammateRequests().addRequestsListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        Object clicked = parent.getItemAtPosition(pos);

        if (clicked instanceof Teammate) { return; }

        TeammateRequest request = (TeammateRequest) clicked;
        Bundle bundle = new Bundle();
        bundle.putSerializable("request", request);
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_teamFragment_to_inviteAcceptedFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            this.inviteTeammate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inviteTeammate() {
        Navigation.findNavController(this.requireActivity(), this.getId()).navigate(R.id.action_teamFragment_to_inviteFragment);
    }

    private void updateList() {
        this.teammateList.clear();
        this.teammateList.addAll(this.app.getTeam().getTeammates());
        this.teammateList.addAll(this.app.getTeammateRequests().getRequests());
        this.teammateListAdapter.notifyDataSetChanged();
    }

    @Override
    public void teammateRequestsUpdated(List<TeammateRequest> requests) {
        this.updateList();
    }
}

