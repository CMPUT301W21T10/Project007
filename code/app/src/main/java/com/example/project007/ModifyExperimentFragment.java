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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * This is ModifyExperimentFragment
 * Use to add or modify Experiment
 */
public class ModifyExperimentFragment extends DialogFragment{

    private EditText experimentName;
    private EditText experimentDescription;
    private TextView experimentDate;
    private Spinner experimentRegion;
    private EditText experimentMinimumTrails;
    CheckBox location;

    private String type = "None";
    public Experiment currentExperiment = null;
    private final String[] types = {"Count-based", "Binomial", "Non-negative","Measurement"};
    private final String[] regions = {"Mr.Ma's heart","China","USA","Canada"};
    private boolean requireLocation = false;
    private Integer minimumTrails;
    private String region;
    private Integer id = null;
    private ArrayList<String> trailsId = null;
    private ArrayList<String> questionId = null;
    private ArrayList<String> subscriptionId = null;
    private boolean condition = true;
    private boolean publishCondition = false;



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
        location = view.findViewById(R.id.checkBox);
        experimentRegion = view.findViewById(R.id.regionSpinner);
        experimentMinimumTrails = view.findViewById(R.id.minimumTrails);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.experimentType, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(),
                R.array.regionValue, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        experimentRegion.setAdapter(adapter2);

        if (currentExperiment != null){
            experimentName.setText(currentExperiment.getName());
            experimentDescription.setText(currentExperiment.getDescription());
            experimentDate.setText(currentExperiment.getDate());
            location.setChecked(currentExperiment.isRequireLocation());
            experimentMinimumTrails.setText(currentExperiment.getMinimumTrails().toString());

            id = currentExperiment.getId();
            trailsId = currentExperiment.getTrailsId();
            subscriptionId = currentExperiment.getSubscriptionId();
            condition = currentExperiment.isCondition();
            publishCondition = currentExperiment.isPublishCondition();
            questionId = currentExperiment.getQuestionId();

            int spinnerPosition2 = adapter2.getPosition(currentExperiment.getRegion());
            experimentRegion.setSelection(spinnerPosition2);
            int spinnerPosition = adapter.getPosition(currentExperiment.getType());
            typeSpinner.setSelection(spinnerPosition);
        }

        //auto set date
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        experimentDate.setText(date);
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



        ImageView image = view.findViewById(R.id.instance_image);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = types[position];
                switch (type){
                    case "Count-based": image.setImageResource(R.drawable.c); break;
                    case "Binomial": image.setImageResource(R.drawable.b); break;
                    case "Non-negative": image.setImageResource(R.drawable.n); break;
                    case "Measurement": image.setImageResource(R.drawable.m); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //experimentType.setText("NONE");
            }
        });

        experimentRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region = regions[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //experimentType.setText("NONE");
            }
        });
        // dialog part, show UI commit information
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(view)
                .setTitle("Add/Edit Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null).create();

        return dialog;
    }
    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = false;
                String name="",description="",date="";
                try{
                    name = experimentName.getText().toString();
                    description = experimentDescription.getText().toString();
                    date = experimentDate.getText().toString();
                    requireLocation = location.isChecked();
                    minimumTrails = Integer.parseInt(experimentMinimumTrails.getText().toString());
                    success = true;
                }catch(Exception e){Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();}
                if (success){
                    Bundle result = new Bundle();
                    result.putSerializable("com.example.project007.modifiedExperiment",
                            new Experiment(name,description,date,type,id,trailsId,
                                    subscriptionId,requireLocation,condition,minimumTrails,region,publishCondition,questionId) );
                    getParentFragmentManager().setFragmentResult("homeRequest", result);
                    alertDialog.dismiss();
                }
            }
        });
    }
}
