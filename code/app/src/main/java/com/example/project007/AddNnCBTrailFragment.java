package com.example.project007;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNnCBTrailFragment extends Fragment {
    private EditText date_generate;
    private EditText title;
    private EditText NnCBData;
    private EditText time_generate;
    private AddBinoTrailFragment.FragmentInteractionListener listener;

    //https://stackoverflow.com/questions/37121091/passing-data-from-activity-to-fragment-using-interface
    //Answered by Masum at May 9 '16 at 17:57
    public interface FragmentInteractionListener{
        void sending_data(Experiment experiment);
        void editing_data(Experiment experiment);
    }


    static AddBinoTrailFragment newInstance(Experiment experiment){
        Bundle args = new Bundle();
        args.putSerializable("result", experiment);
        AddBinoTrailFragment fragment = new AddBinoTrailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public boolean checkText (Experiment experiment){
        //https://stackoverflow.com/questions/18259644/how-to-check-if-a-string-matches-a-specific-format
        //answered by arshajii  Aug 15 '13 at 18:55


        String NnCBData_info = experiment.getVariesData();
        String Trail_title = experiment.getTrail_title();


        if (!NnCBData_info.matches("[0-9]+") & !NnCBData_info.equals("")){
            Toast.makeText(getActivity(),"Input int number plz!",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment by Senimii Jul 17 '13 at 14:26
    }


    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        if (context instanceof AddBinoTrailFragment.FragmentInteractionListener){
            listener = (AddBinoTrailFragment.FragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement FragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =inflater.inflate(R.layout.fragment_add_trail, container, false);
        title = view.findViewById(R.id.trail_Title_editText);
        date_generate = view.findViewById(R.id.date_editText);
        NnCBData = view.findViewById(R.id.SuccessText);
        time_generate = view.findViewById(R.id.time_editText);

        Button okButton= view.findViewById(R.id.ok_pressed );

        //get local date and time and put it into the edittext
        SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());
        //https://stackoverflow.com/questions/21917107/automatic-date-and-time-in-edittext-android
        //answered by Smile2Life Feb 20

        date_generate.setText(date);
        time_generate.setText(time);


        if (getArguments() == null){
            //add items
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_info = title.getText().toString();
                    String date_info = date_generate.getText().toString();
                    String time_info = time_generate.getText().toString();
                    String NnCBData_info = NnCBData.getText().toString();
                    String type_info = "Binomial";
                    //temp written as this

                    Experiment experiment = new Experiment(title_info, time_info, date_info, NnCBData_info, type_info);
                    //error prone
                    if (checkText(experiment)){
                        listener.sending_data(experiment);
                        getParentFragmentManager().popBackStack();
                        //https://stackoverflow.com/questions/43043936/close-a-fragment-on-button-click-which-is-inside-that-fragment
                        //answered by DrGregoryHouse Apr 23 '19 at 13:48
                    }
                }
            });
        }else{
            Experiment argument = (Experiment) getArguments().get("result");
            title.setText(argument.getTrail_title());
            date_generate.setText(argument.getDate());
            time_generate.setText(argument.getDate());
            NnCBData.setText(argument.getVariesData());
            //edit items
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_info = title.getText().toString();
                    String date_info= date_generate.getText().toString();
                    String time_info = time_generate.getText().toString();
                    String NnCBData_info = NnCBData.getText().toString();
                    //String rate_text = "";

                    if (checkText(argument)) {
                        listener.editing_data(argument);
                        argument.setTrail_title(title_info);
                        argument.setDate(date_info);
                        argument.setTime(time_info);
                        argument.setSuccess(NnCBData_info);
                        getParentFragmentManager().popBackStack();

                    }
                }
            });

        }
        return view;
    }
}
