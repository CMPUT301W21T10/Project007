package com.example.project007;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class ModifyExperimentFragment extends DialogFragment {


    private EditText experimentName;
    private EditText experimentDescription;
    private EditText experimentDate;
    private EditText experimentType;
    private OnFragmentInteractionListener listener;

    public Experiment currentExperiment = null;

    // get an instance means to modify it
    public ModifyExperimentFragment(Experiment currentExperiment) {
        this.currentExperiment = currentExperiment;
    }

    // no argument means create a new one
    public ModifyExperimentFragment() {
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.modify_experiment_fragement, null);
        experimentName = view.findViewById(R.id.editTextName);
        experimentDescription = view.findViewById(R.id.editTextDescription);
        experimentDate = view.findViewById(R.id.editTextDate);
        experimentType = view.findViewById(R.id.editTextType);
        // set saved information if it's modifying
        if (currentExperiment != null){
            experimentName.setText(currentExperiment.getName());
            experimentDescription.setText(currentExperiment.getDescription());
            experimentDate.setText(currentExperiment.getDate());
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
                        String type = experimentType.getText().toString();
                        listener.onOkPressed(new Experiment(name,description,date,type,null) );
                    }}).create();
    }
}
