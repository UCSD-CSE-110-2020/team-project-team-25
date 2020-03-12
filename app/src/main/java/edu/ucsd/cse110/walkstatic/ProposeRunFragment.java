package edu.ucsd.cse110.walkstatic;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;

import edu.ucsd.cse110.walkstatic.runs.Run;
import edu.ucsd.cse110.walkstatic.runs.RunProposal;

public class ProposeRunFragment extends Fragment {
    private Walkstatic app;
    private RunProposal runProposal;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    boolean validTime = false;
    boolean validDate = false;
    boolean isToday = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propose_run, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.propose_run_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.proposeCheckButton && (validTime && validDate)) {
            app.addRunProposal(this.runProposal);
            Navigation.findNavController(this.requireView()).navigate(R.id.runActivity);
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        MenuItem save = menu.findItem(R.id.proposeCheckButton);
        save.setEnabled(validTime && validDate);
        int tint = (validTime && validDate) ? R.color.tintActive : R.color.tintDisabled;
        save.setIconTintList(getContext().getResources().getColorStateList(tint, null));
    }
*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.app = new Walkstatic(this.requireContext());
        if (this.getArguments() != null && this.getArguments().getSerializable("Run") != null) {
            Run run = (Run) this.getArguments().getSerializable("Run");
            this.runProposal = new RunProposal(run);
        }
        TextView runProposalName = getActivity().findViewById(R.id.runProposalName);
        EditText dateEditText = getActivity().findViewById(R.id.editDateText);
        EditText timeEditText = getActivity().findViewById(R.id.editTimeText);
        runProposalName.setText(this.runProposal.getRun().getName());
        dateEditText.setFocusable(false);
        timeEditText.setFocusable(false);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                //sHour >= hour && sMinute >= minutes
                //&& (sHour < hour && sMinute < minutes)
                // time picker dialog
                timePicker = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                            if(isToday == true && ((sHour == hour && sMinute < minutes) || sHour < hour)  ){
                                    timeEditText.setText("Please select a valid time");
                                    validTime = false;
                                }
                            else {
                                String time = sHour + ":" + sMinute;
                                if(sMinute < 10){
                                    time = sHour + ":0" + sMinute;
                                }
                                timeEditText.setText(time);
                                runProposal.setTime(time);
                                validTime = true;
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
                            public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {
                                String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + years;
                                dateEditText.setText(date);

                                runProposal.setDate(date);
                                validDate = true;
                                if(years == year && monthOfYear == month && dayOfMonth == day){
                                    isToday = true;
                                }
                                else{
                                    isToday = false;
                                }

                            }
                        }, year, month, day);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });

    }
    @Override
    public void onDestroy(){
        this.app.destroy();
        this.app = null;
        super.onDestroy();
    }
}
