package com.example.project007;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * AddNnCBTrailFragment
 * perform add, delete Non-negative countBase trail
 */
public class AddNnCBTrailFragment extends Fragment {
    private TextView date_generate;
    private EditText title;
    private EditText NnCBData;
    private EditText time_generate;
    private final Integer ID = null;
    private final String UserId = null;
    private AddBinoTrailFragment.FragmentInteractionListener listener;
    private TextView latitude;
    private TextView longitude;
    private Location location;
    private boolean needLocation;
    private String type;
    private boolean ignorance;

    //https://stackoverflow.com/questions/37121091/passing-data-from-activity-to-fragment-using-interface
    //Answered by Masum at May 9 '16 at 17:57
    public interface FragmentInteractionListener{
        void sending_data(Trails trails);
        void editing_data(Trails trails);
    }

    /**
     * This returns a new Instance of fragment
     * @return
     * Return Fragment
     */
    static AddNnCBTrailFragment newInstance(Trails trails){
        Bundle args = new Bundle();
        args.putSerializable("result", trails);
        AddNnCBTrailFragment fragment = new AddNnCBTrailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * This returns Boolean for checking text
     * @return
     * Return Boolean value
     */
    public boolean checkText (Trails trails){
        //https://stackoverflow.com/questions/18259644/how-to-check-if-a-string-matches-a-specific-format
        //answered by arshajii  Aug 15 '13 at 18:55


        String NnCBData_info = trails.getVariesData();
        String Trail_title = trails.getTrail_title();
        Location location = trails.getLocation();
        String type = trails.getType();


        if (type.equals("Measurement")){
            if (NnCBData_info.equals("") || !NnCBData_info.matches("([0-9]*[.])[0-9]+")){
                Toast.makeText(getActivity(),"Input a positive float number plz!",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if (!type.equals("Measurement")&!type.equals("Binomial")){
            if (NnCBData_info.equals("") ||!NnCBData_info.matches("[0-9]+")){
                Toast.makeText(getActivity(),"Input int number plz!",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else if(Trail_title.equals("")){
            Toast.makeText(getActivity(),"Input a title plz!",Toast.LENGTH_SHORT).show();
            return false;
        }else if(needLocation & location == null){
            Toast.makeText(getActivity(),"Enter a location plz!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getChildFragmentManager()
                .setFragmentResultListener("showLocation", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        // Do something with the result
                        location = (Location) bundle.getSerializable("Location");
                    }
                });
    }
    //disable menu in frag
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view =inflater.inflate(R.layout.fragment_add_othertrail, container, false);
        title = view.findViewById(R.id.trail_Title_editText);
        date_generate = view.findViewById(R.id.date_editText);
        NnCBData = view.findViewById(R.id.ResultText);
        time_generate = view.findViewById(R.id.time_editText);
        latitude = view.findViewById(R.id.latitude_editText );
        longitude = view.findViewById(R.id.longitude_editText );
        Button okButton= view.findViewById(R.id.ok_pressed );
        Button mapButton = view.findViewById(R.id.map_button);


        //receive data from activity
        TrailsActivity activity = (TrailsActivity) getActivity();
        needLocation = activity.WhetherTrailsLoc();
        type = activity.getTrailsType();

        Fragment fragment = new MapFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.map_container, fragment).commit();
        if (!needLocation){
            //mapButton.setVisibility(View.INVISIBLE);
            //if doesn't require the location
        }else{
            //initialize map content
            /*Fragment fragment = new MapFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container, fragment).commit();*/
            //Toast.makeText(getActivity(), "This trail require you to enter location data!",Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Warning!")
                    .setMessage("This trail require you to enter location data!")
                    .setPositiveButton(android.R.string.yes,  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).create().show();
            //warn experimenter for location data acquire
        }

        //initialize map content
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Location location =(Location)getArguments().getSerializable("location");
                if (location!=null){
                    latitude.setText(String.valueOf(location.getLatitude()));
                    longitude.setText(String.valueOf(location.getLongitude()));

                    Toast.makeText(getActivity(),"location selected!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"NO location selected!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get local date and time and put it into the edittext
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = timeF.format(Calendar.getInstance().getTime());
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        //https://stackoverflow.com/questions/21917107/automatic-date-and-time-in-edittext-android
        //answered by Smile2Life Feb 20
        time_generate.setText(time);
        date_generate.setText(date);



        // datePicker part
        // prevent type
        date_generate.setInputType(InputType.TYPE_NULL);
        // start datePicker if clicked
        date_generate.setOnClickListener(new View.OnClickListener() {
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
                                date_generate.setText(value);
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        if (getArguments() == null){
            //add items
            latitude.setText("N/A");
            longitude.setText("N/A");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_info = title.getText().toString();
                    String date_info = date_generate.getText().toString();
                    String time_info = time_generate.getText().toString();
                    String NnCBData_info = NnCBData.getText().toString();
                    //temp written as this
                    if (!needLocation){
                        Trails trails = new Trails(title_info, date_info, type, time_info, NnCBData_info, ID, ignorance, UserId);
                    }
                    Trails trails = new Trails(title_info, date_info, type, time_info, NnCBData_info, ID, location, ignorance, UserId);

                    //error prone
                    if (checkText(trails)){
                        listener.sending_data(trails);
                        getParentFragmentManager().popBackStack();
                        //https://stackoverflow.com/questions/43043936/close-a-fragment-on-button-click-which-is-inside-that-fragment
                        //answered by DrGregoryHouse Apr 23 '19 at 13:48
                    }
                }
            });
        }else{
            Trails argument = (Trails) getArguments().get("result");
            title.setText(argument.getTrail_title());
            date_generate.setText(argument.getDate());
            time_generate.setText(argument.getTime());
            NnCBData.setText(argument.getVariesData());
            Integer id = argument.getID();
            Location oldLocation = argument.getLocation();
            if (oldLocation != null){
                Toast.makeText(getActivity(),"need location is "+ needLocation,Toast.LENGTH_SHORT).show();
                latitude.setText(String.valueOf(oldLocation.getLatitude()));
                longitude.setText(String.valueOf(oldLocation.getLongitude()));
            }else{
                latitude.setText("N/A");
                longitude.setText("N/A");
            }
            //edit items
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_info = title.getText().toString();
                    String date_info= date_generate.getText().toString();
                    String time_info = time_generate.getText().toString();
                    String NnCBData_info = NnCBData.getText().toString();

                    if (checkText(argument)) {
                        listener.editing_data(argument);
                        argument.setTrail_title(title_info);
                        argument.setDate(date_info);
                        argument.setTime(time_info);
                        argument.setSuccess(NnCBData_info);
                        argument.setID(id);
                        if (needLocation){
                            argument.setLocation(location);
                        }
                        getParentFragmentManager().popBackStack();

                    }
                }
            });

        }
        return view;
    }
}
