package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.teammate.Teammate;
import edu.ucsd.cse110.walkstatic.teammate.TeammateRequest;


public class InviteFragment extends Fragment {

    private Walkstatic app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.app = new Walkstatic(this.getContext());
        this.addChangeListeners();
    }

    private void addChangeListeners(){
        EditText name = this.requireActivity().findViewById(R.id.teammate_name_input);
        EditText email = this.requireActivity().findViewById(R.id.teammate_email_input);
        this.addChangeListenerToEditText(name);
        this.addChangeListenerToEditText(email);
    }

    private void addChangeListenerToEditText(EditText editText){
        editText.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(editable)){
                    editText.setError(requireContext().getResources().getString(R.string.name_empty_error));
                } else {
                    editText.setError(null);
                }
                requireActivity().invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.invite_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        Teammate teammate = this.getTeammate();
        MenuItem invite = menu.findItem(R.id.action_invite);
        invite.setEnabled(teammate.isValid());
        int tint = teammate.isValid() ? R.color.tintActive : R.color.tintDisabled;
        invite.setIconTintList(getContext().getResources().getColorStateList(tint, null));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_invite) {
            this.inviteTeammate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Teammate getTeammate(){
        EditText teammateNameEditText = this.getActivity().findViewById(R.id.teammate_name_input);
        EditText teammateEmailEditText = this.getActivity().findViewById(R.id.teammate_email_input);
        String name = teammateNameEditText.getText().toString();
        String email = teammateEmailEditText.getText().toString();
        Teammate other = new Teammate(email);
        other.setName(name);
        return other;
    }

    private void inviteTeammate() {
        Teammate other = this.getTeammate();
        if(!other.isValid()){
            return;
        }
        EditText teammateNameEditText = this.getActivity().findViewById(R.id.teammate_name_input);
        EditText teammateEmailEditText = this.getActivity().findViewById(R.id.teammate_email_input);
        teammateNameEditText.clearFocus();
        teammateEmailEditText.clearFocus();
        teammateNameEditText.setText("");
        teammateEmailEditText.setText("");


        TeammateRequest teammateRequest = new TeammateRequest(this.app.getUser(), other);
        this.app.getTeammateRequests().addRequest(teammateRequest);
        Navigation.findNavController(this.getActivity(), this.getId()).navigateUp();
    }
}

