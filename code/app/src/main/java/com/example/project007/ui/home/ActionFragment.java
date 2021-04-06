package com.example.project007.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.project007.DatabaseController;
import com.example.project007.Experiment;
import com.example.project007.R;

import java.util.Calendar;
/**
 * This is ActionFragment
 * This class performances three button click
 * Only use from HomeFragment
 */
public class ActionFragment extends DialogFragment {

    Button edit;
    Button delete;
    Button end;
    Button publish;

    // create dialog
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // set basic show up information
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.choose_action, null);
        publish = view.findViewById(R.id.button4);
        edit = view.findViewById(R.id.button);
        delete = view.findViewById(R.id.button2);
        end = view.findViewById(R.id.button3);

        if (!DatabaseController.isPublish()){
            publish.setText("Publish");
        }
        else{
            publish.setText("UnPublish");
        }

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("action", "publish");
                getParentFragmentManager().setFragmentResult("actionRequest", result);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("action", "edit");
                getParentFragmentManager().setFragmentResult("actionRequest", result);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("action", "delete");
                getParentFragmentManager().setFragmentResult("actionRequest", result);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                result.putString("action", "end");
                getParentFragmentManager().setFragmentResult("actionRequest", result);
            }
        });

        // dialog part, show UI commit information
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Choose your Action").create();
    }

}
