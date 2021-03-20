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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class ModifyExperimentFragment extends DialogFragment{


    private EditText experimentName;
    private EditText experimentDescription;
    private EditText experimentDate;
    private EditText experimentRegion;
    private EditText experimentMinimumTrails;


    private String type = "None";
    public Experiment currentExperiment = null;
    private final String[] types = {"Binomial", "Measurement", "Count","IntCount"};
    private boolean requireLocation = false;
    private Integer minimumTrails;
    private String region;
    private Integer id = null;
    private ArrayList<String> trailsId = null;
    private ArrayList<String> subscriptionId = null;
    private boolean condition = true;



    // get an instance means to modify it
    public ModifyExperimentFragment(Experiment currentExperiment) {
        this.currentExperiment = currentExperiment;
    }

    // no argument means create a new one
    public ModifyExperimentFragment() {
    }

    // create dialog
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // set basic show up information
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.modify_experiment_fragement, null);
        experimentName = view.findViewById(R.id.editTextName);
        experimentDescription = view.findViewById(R.id.editTextDescription);
        experimentDate = view.findViewById(R.id.editTextDate);
        Spinner typeSpinner = view.findViewById(R.id.typeChooser);
        CheckBox location = view.findViewById(R.id.checkBox);
        experimentRegion = view.findViewById(R.id.regionEditText);
        experimentMinimumTrails = view.findViewById(R.id.minimumTrails);

        // set saved information if it's modifying
        if (currentExperiment != null){
            experimentName.setText(currentExperiment.getName());
            experimentDescription.setText(currentExperiment.getDescription());
            experimentDate.setText(currentExperiment.getDate());
            location.setChecked(currentExperiment.isRequireLocation());
            experimentRegion.setText(currentExperiment.getRegion());
            experimentMinimumTrails.setText(currentExperiment.getMinimumTrails().toString());

            id = currentExperiment.getId();
            trailsId = currentExperiment.getTrailsId();
            subscriptionId = currentExperiment.getSubscriptionId();
            condition = currentExperiment.isCondition();
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.experimentType, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);
        ImageView image = view.findViewById(R.id.instance_image);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = types[position];
                switch (type){
                    case "Binomial": image.setImageResource(R.drawable.b); break;
                    case "Measurement": image.setImageResource(R.drawable.m); break;
                    case "Count": image.setImageResource(R.drawable.c); break;
                    case "IntCount": image.setImageResource(R.drawable.n); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //experimentType.setText("NONE");
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
                        requireLocation = location.isChecked();
                        minimumTrails = Integer.parseInt(experimentMinimumTrails.getText().toString());
                        region = experimentRegion.getText().toString();

                        Bundle result = new Bundle();
                        result.putSerializable("com.example.project007.modifiedExperiment",
                                new Experiment(name,description,date,type,id,trailsId,
                                        subscriptionId,requireLocation,condition,minimumTrails,region) );
                        getParentFragmentManager().setFragmentResult("homeEditRequest", result);
                    }}).create();
    }

}
