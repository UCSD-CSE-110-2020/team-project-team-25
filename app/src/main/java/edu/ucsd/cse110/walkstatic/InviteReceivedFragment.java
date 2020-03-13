package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.ucsd.cse110.walkstatic.teammate.Teammate;

public class InviteReceivedFragment extends Fragment {

    private Teammate teammate;
    private Walkstatic app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.teammate = (Teammate) getArguments().get("request");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_received_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = new Walkstatic(getContext());

        TextView requesterName = getActivity().findViewById(R.id.requester_name);
        TextView requesterEmail = getActivity().findViewById(R.id.requester_email);

        requesterName.setText(teammate.getName());
        requesterEmail.setText(teammate.getEmail());

        Button acceptButton = view.findViewById(R.id.acceptButton);
        Button rejectButton = view.findViewById(R.id.rejectButton);

        acceptButton.setOnClickListener(click -> {
            app.getTeam().merge(teammate.getTeam());
            Navigation.findNavController(this.getActivity(), this.getId()).navigateUp();
        });

        rejectButton.setOnClickListener(click -> {
            Navigation.findNavController(this.getActivity(), this.getId()).navigateUp();
        });
    }
}
