package com.example.jinglong_trialbook;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class AddExperimentFragment extends DialogFragment {
    private EditText experimentName;
    private EditText experimentDescription;
    private EditText experimentDate;
    private OnFragmentInteractionListener listener;

    public Experiment currentExperiment = null;

    // get an instance means to modify it
    public AddExperimentFragment(Experiment currentExperiment) {
        this.currentExperiment = currentExperiment;
    }

    // no argument means create a new one
    public AddExperimentFragment() {
    }

    // interface
    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);
    }

    // life cycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // create dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // set basic show up information
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_experiment_fragment_layout, null);
        experimentName = view.findViewById(R.id.editTextName);
        experimentDescription = view.findViewById(R.id.editTextDescription);
        experimentDate = view.findViewById(R.id.editTextDate);
        // set saved information if it's modifying
        if (currentExperiment != null){
            experimentName.setText(currentExperiment.getExperimentName());
            experimentDescription.setText(currentExperiment.getExperimentDescription());
            experimentDate.setText(currentExperiment.getExperimentDate());
        }

        // datePicker part
        // prevent type
        experimentDate.setInputType(InputType.TYPE_NULL);
        // start datePicker if clicked
        experimentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int year = cldr.get(Calendar.YEAR);
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                @SuppressLint("DefaultLocale") String value = String.format("%d-%d-%d",year,month+1,day);
                                experimentDate.setText(value);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // dialog part, show UI commit information
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = experimentName.getText().toString();
                        String description = experimentDescription.getText().toString();
                        String date = experimentDate.getText().toString();
                        ArrayList<Boolean> trails = new ArrayList<Boolean>();;
                        listener.onOkPressed(new Experiment(name,description,date,trails) );
                    }}).create();
    }

}


