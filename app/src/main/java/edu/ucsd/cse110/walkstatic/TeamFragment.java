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
import edu.ucsd.cse110.walkstatic.teammate.TeammateArrayAdapter;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequestsListener;

public class TeamFragment extends Fragment implements TeammateRequestsListener {

    private TeammateArrayAdapter teammateListAdapter;
    private List<TeammateRequest> requestsList;
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
    }

    private void populateTeammates() {
        ListView listView = this.getActivity().findViewById(R.id.my_teammates_list);
        this.requestsList = this.app.getTeammateRequests().getRequests();
        teammateListAdapter = new TeammateArrayAdapter(this.getActivity(), R.layout.teammate_request_textview, requestsList);
        listView.setAdapter(teammateListAdapter);
        teammateListAdapter.notifyDataSetChanged();
        this.app.getTeammateRequests().addRequestsListener(this);
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
        Navigation.findNavController(this.getActivity(), this.getId()).navigate(R.id.action_teamFragment_to_inviteFragment);
    }


    @Override
    public void teammateRequestsUpdated(List<TeammateRequest> requests) {
        this.requestsList.clear();
        this.requestsList.addAll(requests);
        this.teammateListAdapter.notifyDataSetChanged();
    }
}

