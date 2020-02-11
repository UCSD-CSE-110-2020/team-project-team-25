package edu.ucsd.cse110.walkstatic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.speech.SpeechListener;
import edu.ucsd.cse110.walkstatic.speech.VoiceDictationFactory;
import edu.ucsd.cse110.walkstatic.speech.VoiceDictation;

public class EditRunFragment extends Fragment implements SpeechListener {
    private enum RunElement {
        NAME,
        STARTING_POINT
    }
    private static String TYPE_KEY = "runKey";

    private VoiceDictation voiceDictation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.voiceDictation = VoiceDictationFactory.getVoiceDictation(this.getContext());
        this.voiceDictation.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_run, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.addSpeechListeners();
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

    private void addSpeechListeners(){
        ImageButton nameButton = this.getActivity().findViewById(R.id.dictate_name);
        Bundle nameBundle = new Bundle();
        nameBundle.putInt(TYPE_KEY, RunElement.NAME.ordinal());
        nameButton.setOnClickListener(new VoiceDictationClickListener(nameBundle));

        ImageButton startingPointButton = this.getActivity().findViewById(R.id.dictate_starting_point);
        Bundle startingPointBundle = new Bundle();
        startingPointBundle.putInt(TYPE_KEY, RunElement.STARTING_POINT.ordinal());
        startingPointButton.setOnClickListener(new VoiceDictationClickListener(startingPointBundle));
    }

    private class VoiceDictationClickListener implements View.OnClickListener {
        Bundle bundle;
        public VoiceDictationClickListener(Bundle bundle){
            this.bundle = bundle;
        }

        @Override
        public void onClick(View v) {
            voiceDictation.doRecognition(this.bundle);
        }
    }

    @Override
    public void onSpeech(@NonNull String received, @Nullable Bundle options) {
        RunElement element = RunElement.values()[options.getInt(TYPE_KEY)];
        EditText editText = null;
        if(element == RunElement.NAME) {
            editText = this.getActivity().findViewById(R.id.run_name_text);
        }
        if(element == RunElement.STARTING_POINT) {
            editText = this.getActivity().findViewById(R.id.starting_point_text);
        }
        editText.setText(received);
    }
}
