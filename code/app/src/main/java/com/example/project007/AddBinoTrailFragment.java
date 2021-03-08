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

public class AddBinoTrailFragment extends Fragment{
    private EditText date_generate;
    private EditText title;
    private EditText success;
    private EditText fail;
    private EditText time_generate;
    private FragmentInteractionListener listener;

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


        String success = experiment.getSuccess();
        String fail = experiment.getFailure();
        String Trail_title = experiment.getTrail_title();


        if (!success.matches("[0-9]+") & !success.equals("")){
            Toast.makeText(getActivity(),"Input number plz!",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!fail.matches("[0-9]+")& !fail.equals("")){
            Toast.makeText(getActivity(),"Input number plz!",Toast.LENGTH_SHORT).show();
            return false;
        }else if (Trail_title.equals("")){
            Toast.makeText(getActivity(),"Title is empty!",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
        //https://stackoverflow.com/questions/10770055/use-toast-inside-fragment by Senimii Jul 17 '13 at 14:26
    }


    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener){
            listener = (FragmentInteractionListener) context;
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
        success = view.findViewById(R.id.SuccessText);
        fail = view.findViewById(R.id.failText);
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
                    String success_info = success.getText().toString();
                    String fail_info = fail.getText().toString();
                    String type_info = "Binomial";
                    //temp written as this
                    if (!success_info.matches("[0-9]+") & !success_info.equals("")){
                    }else if(!fail_info.matches("[0-9]+")& !fail_info.equals("")) {
                    }

                    Experiment experiment = new Experiment(title_info, time_info, date_info, success_info, fail_info, type_info);
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
            success.setText(argument.getSuccess());
            fail.setText(argument.getFailure());
            //edit items
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_info = title.getText().toString();
                    String date_info= date_generate.getText().toString();
                    String time_info = time_generate.getText().toString();
                    String success_info = success.getText().toString();
                    String fail_info = fail.getText().toString();
                    //String rate_text = "";
                    if (!success_info.matches("[0-9]+")&!success_info.equals("")){
                        Toast.makeText(getActivity(),"Input number plz!",Toast.LENGTH_SHORT).show();
                    }else if(!fail_info.matches("[0-9]+")&!fail_info.equals("")) {
                        Toast.makeText(getActivity(), "Input number plz!", Toast.LENGTH_SHORT).show();
                    }else{
                        if (checkText(argument)) {
                            listener.editing_data(argument);
                            argument.setTrail_title(title_info);
                            argument.setDate(date_info);
                            argument.setTime(time_info);
                            argument.setSuccess(success_info);
                            argument.setFailure(fail_info);
                            getParentFragmentManager().popBackStack();
                        }
                    }
                }
            });

        }
        return view;
    }
/*    public String rate(String success, String fail){
        String rate_text;
        if (success.equals("") || fail.equals("")){
            rate_text = "0";
            return rate_text;
        }
        float success_count = Float.valueOf(success).floatValue();
        float fail_count = Float.valueOf(fail).floatValue();
        float rate = 0;
        rate = success_count/(success_count + fail_count);
        rate_text = String.valueOf(rate);
        return rate_text;
    }
    //https://www.tutorialspoint.com/convert-from-string-to-float-in-java#:~:text=To%20convert%20String%20to%20float%2C%20use%20the%20valueOf()%20method.
    //By Samual Sam 10-Dec-2018 16:33:58
    //https://stackoverflow.com/questions/5071040/java-convert-integer-to-string
    //By Bozho Feb 21 '11 at 20:45*/
}
