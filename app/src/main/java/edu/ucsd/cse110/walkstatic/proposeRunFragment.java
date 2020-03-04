package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.sql.Time;
import java.util.Calendar;
import java.util.Objects;

import edu.ucsd.cse110.walkstatic.runs.Run;

public class proposeRunFragment extends Fragment {
    private Run run;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propose_run, container, false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.proposeCheckButton) {
            String preferencesName = this.getResources().getString(R.string.proposed_time_run);
            Activity activity = Objects.requireNonNull(this.getActivity());
            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    preferencesName, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(preferencesName, this.run.toJSON()).apply();
            Navigation.findNavController(Objects.requireNonNull(this.getView())).navigate(R.id.runActivity);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (this.getArguments() != null && this.getArguments().getSerializable("Run") != null) {
            Run run = (Run) this.getArguments().getSerializable("Run");
            this.run = run;
        }
        EditText dateEditText = getActivity().findViewById(R.id.editDateText);
        EditText timeEditText = getActivity().findViewById(R.id.editTimeText);
        dateEditText.setFocusable(false);
        timeEditText.setFocusable(false);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                if(sHour > cldr.HOUR_OF_DAY && sMinute > cldr.MINUTE){
                                    timeEditText.setText(sHour + ":" + sMinute);
                                } else {
                                    timeEditText.setText("Invalid Time");
                                }
                            }
                        }, hour, minutes, true);
                timePicker.show();
            }
        });
        //EditText timeEditText = getActivity().findViewById(R.id.editTimeText);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });

    }
}
