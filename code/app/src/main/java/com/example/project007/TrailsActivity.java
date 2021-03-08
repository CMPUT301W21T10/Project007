package com.example.project007;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TrailsActivity extends AppCompatActivity implements AddBinoTrailFragment.FragmentInteractionListener {
    ListView trail_List;
    ArrayAdapter<Experiment> trail_Adapter;
    ArrayList<Experiment> experi_DataList;
    Button addButton;
    AddBinoTrailFragment addBinoTrailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final FloatingActionButton addButton = findViewById(R.id.experimentBtn);
        trail_List = findViewById(R.id.trail_list);

        String []experiments = {};
        String []date = {};
        String []success ={};
        String []time ={};
        String []fail = {};
        String []type = {};

        experi_DataList = new ArrayList<>();

        for(int i=0; i<experiments.length; i++){
            experi_DataList.add((new Experiment(experiments[i], date[i], time[i], success[i], fail[i], type[i])));
        }

        trail_Adapter = new CustomList(this, experi_DataList);
        trail_List.setAdapter(trail_Adapter);
        //https://stackoverflow.com/questions/41350269/my-listview-is-showing-the-object-and-not-the-contents-of-each-object/41350519
        //answered by stephen Ruda Dec 27 '16 at 18:35



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize fragment
                addBinoTrailFragment = new AddBinoTrailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container, addBinoTrailFragment).addToBackStack(null).commit();
                //addButton.hide();
            }
        });

        //short click action for editting data
        trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Experiment newtrail = trail_Adapter.getItem(position);
                AddBinoTrailFragment fragment = AddBinoTrailFragment.newInstance(newtrail);
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container, fragment).addToBackStack(null).commit();
            }
        });


        //longClick action for delete data
        trail_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                experi_DataList.remove(position);
                trail_Adapter.notifyDataSetChanged();
                return false;
            }
        });
        trail_List.setAdapter(trail_Adapter);
        //https://stackoverflow.com/questions/4834750/how-to-get-the-selected-item-from-listview
        //from xandy's answer Jan 29 '11 at 2:57*/
    }

    @Override
    public void sending_data(Experiment experiment) {
        trail_Adapter.add(experiment);
    }
    @Override
    public void editing_data(Experiment experiment) {
        trail_Adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trails_activity, menu);
        return true;
    }

    //YO!!! This is where you inplement those fragements under the if

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
}

