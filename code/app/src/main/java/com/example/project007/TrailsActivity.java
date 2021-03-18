package com.example.project007;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TrailsActivity extends AppCompatActivity implements AddBinoTrailFragment.FragmentInteractionListener, AddNnCBTrailFragment.FragmentInteractionListener {
    ListView trail_List;
    ArrayAdapter<Trails> trail_Adapter;
    ArrayList<Trails> trails_DataList;
    AddBinoTrailFragment addBinoTrailFragment;
    AddNnCBTrailFragment addNnCBTrailFragment;
    TextView descriptionTrail;
    private Experiment experiment;
    private Integer position;
    boolean needLocation;
    String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trails_activity_main);
        descriptionTrail = findViewById(R.id.descriptionforTrail);
        //database for unique trails
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DatabaseController.setDb(db);
        final CollectionReference collectionReference = db.collection("Trails");
        //database for unique trails

        String type = experiment.getType();
        String title = experiment.getName();
        String description = experiment.getDescription();
        //needLocation = experiment.getRequireLocation();
        needLocation = false;
        //String description ="SB!";
        descriptionTrail.setText(description);
        //String title = "Measurement One";

        //type = "NonNegative";

        //toolbar content may vary with the input type
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar content may vary with the input type


        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.project007.INSTANCE");
        position = intent.getIntExtra("com.example.project007.POSITION", -1);

        final FloatingActionButton addButton = findViewById(R.id.experimentBtn);
        trail_List = findViewById(R.id.trail_list);



        trails_DataList = new ArrayList<>();

        if (type.equals("Binomial")){
            trail_Adapter = new TrailList_Bino(this, trails_DataList);
        }else{
            trail_Adapter = new TrailList_OtherFrags(this, trails_DataList);
        }



        trail_List.setAdapter(trail_Adapter);
        //https://stackoverflow.com/questions/41350269/my-listview-is-showing-the-object-and-not-the-contents-of-each-object/41350519
        //answered by stephen Ruda Dec 27 '16 at 18:35


        //add button is where we specify the different experiment trails
        //currently use fixed variable for debugging
        //once firestrore ready this part will get type from database
        if (type.equals("Binomial")){
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //initialize fragment
                    addBinoTrailFragment = new AddBinoTrailFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, addBinoTrailFragment).addToBackStack(null).commit();
                    //addButton.hide();
                }
            });

            trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trails newtrail = trail_Adapter.getItem(position);
                    AddBinoTrailFragment fragment = AddBinoTrailFragment.newInstance(newtrail);
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, fragment).addToBackStack(null).commit();
                }
            });

        }else{
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //initialize fragment
                    addNnCBTrailFragment = new AddNnCBTrailFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, addNnCBTrailFragment).addToBackStack(null).commit();
                }
            });
            trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trails newtrail = trail_Adapter.getItem(position);
                    AddNnCBTrailFragment NcCb_fragment = AddNnCBTrailFragment.newInstance(newtrail);
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, NcCb_fragment).addToBackStack(null).commit();
                }
            });
        }



        //longClick action for delete data
        trail_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                trails_DataList.remove(position);
                trail_Adapter.notifyDataSetChanged();
                return false;
            }
        });
        trail_List.setAdapter(trail_Adapter);
        //https://stackoverflow.com/questions/4834750/how-to-get-the-selected-item-from-listview
        //from xandy's answer Jan 29 '11 at 2:57*/
    }

    @Override
    public void sending_data(Trails trails) {
        trail_Adapter.add(trails);
        Toast.makeText(getApplicationContext(),"New Trail:" + trails.getTrail_title() + " added success!",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void editing_data(Trails trails) {
        trail_Adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Trail:" + trails.getTrail_title() + " edited success!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trails_activity, menu);
        return true;
    }

    //YO!!! This is where you implement those fragments under the if

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.questionsOpt) {
            return true;
        }else if (id == R.id.viewResult) {
            return true;
        }else if (id == R.id.QROpt) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //sending data from activity to frags
    public boolean WhetherTrailsLoc() {
        return needLocation;
    }

    public String getTrailsType() {
        return type;
    }
}

