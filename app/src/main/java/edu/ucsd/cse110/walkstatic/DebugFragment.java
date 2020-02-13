package edu.ucsd.cse110.walkstatic;


import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.walkstatic.fitness.DefaultBlueprints;
import edu.ucsd.cse110.walkstatic.fitness.FitnessListener;
import edu.ucsd.cse110.walkstatic.fitness.FitnessService;
import edu.ucsd.cse110.walkstatic.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.walkstatic.fitness.MockFitAdapter;
import edu.ucsd.cse110.walkstatic.time.TimeMachine;

public class DebugFragment extends Fragment implements FitnessListener {
    private static final long INCREMENT_AMOUNT = 500;
    private static final String TIME_FORMAT = "HH:mm:ss.SSS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initAddSteps();
        loadFirstSteps();
        loadTime();
    }

    private void initAddSteps(){
        Button button = this.getActivity().findViewById(R.id.add_steps);
        button.setOnClickListener(view -> {
            addFiveHundredSteps();
        });
    }

    private void addFiveHundredSteps(){
        FitnessService service = FitnessServiceFactory.create(this.getActivity());
        service.setListener(this);
        service.updateStepCount();
        FitnessServiceFactory.setDefaultFitnessServiceKey(DefaultBlueprints.DEBUG);
    }

    @Override
    public void onNewSteps(long newTotal) {
        long incrementedSteps = newTotal + INCREMENT_AMOUNT;
        fillSteps(incrementedSteps);
        FitnessServiceFactory.put(DefaultBlueprints.DEBUG, activity -> {
            return new MockFitAdapter(incrementedSteps);
        });
    }

    private void loadFirstSteps(){
        FitnessService service = FitnessServiceFactory.create(this.getActivity());
        service.setListener(newTotal -> {
            fillSteps(newTotal);
        });
        service.updateStepCount();
    }

    private void fillSteps(long steps){
        TextView currentSteps = this.getActivity().findViewById(R.id.step_count);
        currentSteps.setText(Long.toString(steps));
    }

    private void loadTime(){
        LocalDateTime time = TimeMachine.now();
        Button saveTimeButton = this.getActivity().findViewById(R.id.save_time);
        EditText timeText = this.getActivity().findViewById(R.id.time_text);
        timeText.setText(time.toLocalTime().toString());
        timeText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String attemptedTime = timeText.getText().toString();
                try{
                    LocalTime.parse(attemptedTime, DateTimeFormatter.ofPattern(TIME_FORMAT));
                    timeText.setError(null);
                    saveTimeButton.setEnabled(true);
                } catch (DateTimeParseException exception){
                    timeText.setError(getContext().getResources().getString(R.string.time_parse_error));
                    saveTimeButton.setEnabled(false);
                }
            }
        });
        timeText.setInputType(InputType.TYPE_CLASS_DATETIME);

        saveTimeButton.setOnClickListener(view -> {
            String attemptedTime = timeText.getText().toString();
            try{
                LocalTime newTime = LocalTime.parse(attemptedTime, DateTimeFormatter.ofPattern(TIME_FORMAT));
                LocalDate day = TimeMachine.now().toLocalDate();
                LocalDateTime newDateTime = day.atTime(newTime);
                TimeMachine.setNow(newDateTime);
            } catch (DateTimeParseException exception){
                Log.e("Debug Fragment", exception.toString());
            }
        });
    }
}
