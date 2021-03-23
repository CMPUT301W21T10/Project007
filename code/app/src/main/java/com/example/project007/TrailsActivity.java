package com.example.project007;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrailsActivity extends AppCompatActivity implements AddBinoTrailFragment.FragmentInteractionListener, AddNnCBTrailFragment.FragmentInteractionListener {
    ListView trail_List;
    ArrayAdapter<Trails> trail_Adapter;
    ArrayList<Trails> trails_DataList;
    AddBinoTrailFragment addBinoTrailFragment;
    AddNnCBTrailFragment addNnCBTrailFragment;
    final String TAG = "Trails_Sample";

    TextView descriptionTrail;
    ResultFragment resultFragment;
    QrcodeFragment qrcode;
    private Experiment experiment;
    private Integer position;
    boolean needLocation;
    String type;
    String description;
    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trails_activity_main);
        descriptionTrail = findViewById(R.id.descriptionforTrail);
        //database for unique trails
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        TrailsDatabaseController.setTrail_db(db);
        final CollectionReference collectionReference = db.collection("Trails");
        //database for unique trails


        //receive data from experiment
        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.project007.INSTANCE");
        position = intent.getIntExtra("com.example.project007.POSITION", -1);
        type = experiment.getType();
        title = experiment.getName();
        description = experiment.getDescription();
        needLocation = experiment.isRequireLocation();
        descriptionTrail.setText(description);
        //receive data from experiment





        //toolbar content may vary with the input type
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView)toolbar.findViewById(R.id.toolbarTextView);
        textView.setText(title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar content may vary with the input type


        final FloatingActionButton addButton = findViewById(R.id.experimentBtn);
        if (!experiment.isCondition()){
            addButton.setVisibility(View.INVISIBLE);
        }

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


        //fire store uploading

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    trails_DataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Trail_title")));
                        String trail_title = (String) doc.getData().get("Trail_title");
                        String date = (String) doc.getData().get("Date");
                        String type = (String) doc.getData().get("Type");
                        String time = (String) doc.getData().get("Time");
                        //uncertain value
                        String success = (String) doc.getData().get("Success");
                        String failure = (String) doc.getData().get("Failure");
                        String variesData = (String) doc.getData().get("VariesData");
                        String longitude = (String)  doc.getData().get("longitude");
                        String latitude = (String)  doc.getData().get("latitude");
                        Location location;
                        if (longitude != null & latitude != null){
                            location = new Location( Double.parseDouble(longitude), Double.parseDouble(latitude));//error prone
                        }else{
                            location = null;
                        }
                        //uncertain value

                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);
                        if (experiment.getTrailsId() != null && experiment.getTrailsId().contains(idString)){
                            if (success == null){//case for non-binomial trails
                                if (location != null){
                                    trails_DataList.add(new Trails(trail_title, date, type, time, variesData, ID, location));
                                }else{
                                    trails_DataList.add(new Trails(trail_title, date, type, time, variesData, ID));
                                }
                            }else if(variesData == null){//case for binomial trails
                                if (location != null){
                                    trails_DataList.add(new Trails(trail_title, date, type, time, success, failure, ID, location));
                                }else{
                                    trails_DataList.add(new Trails(trail_title, date, type, time, success, failure, ID));
                                }
                            }
                        }
                    }
                    TrailsDatabaseController.setMaxTrailId(trails_DataList.size());
                    trail_Adapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
            }
        });
        //fire store uploading

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

            /*trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trails newtrail = trail_Adapter.getItem(position);
                    AddBinoTrailFragment fragment = AddBinoTrailFragment.newInstance(newtrail);
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, fragment).addToBackStack(null).commit();
                }
            });*/

        }else{
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //initialize fragment
                    addNnCBTrailFragment = new AddNnCBTrailFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, addNnCBTrailFragment).addToBackStack(null).commit();
                }
            });
            /*trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trails newtrail = trail_Adapter.getItem(position);
                    AddNnCBTrailFragment NcCb_fragment = AddNnCBTrailFragment.newInstance(newtrail);
                    getSupportFragmentManager().beginTransaction().replace(R.id.data_container, NcCb_fragment).addToBackStack(null).commit();
                }
            });*/
        }



        //longClick action for delete data
        trail_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                Trails newtrail = trail_Adapter.getItem(position);
                trail_Adapter.notifyDataSetChanged();
                boolean deleteResult = TrailsDatabaseController.delete_Trails("Trails", newtrail);
                if (deleteResult){
                    Toast.makeText(getApplicationContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
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
        boolean addResult = TrailsDatabaseController.modify_Trails("Trails", trails);
        ArrayList<String> valueList = experiment.getTrails();
        valueList.add(trails.getID().toString());
        DatabaseController.setExperimentTrails(experiment.getId().toString(), valueList );
        if (addResult){
            Toast.makeText(getApplicationContext(), "Add Succeed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Add Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editing_data(Trails trails) {
        trail_Adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Trail:" + trails.getTrail_title() + " edited success!",Toast.LENGTH_SHORT).show();
        boolean addResult = TrailsDatabaseController.modify_Trails("Trails", trails);
        if (addResult){
            Toast.makeText(getApplicationContext(), "Add Succeed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Add Failed", Toast.LENGTH_SHORT).show();
        }
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
            openQuestionActivity();
            return true;
        }else if (id == R.id.viewResult) {
            if(trails_DataList.size()==0){
                Toast toast = Toast.makeText(getApplicationContext(),"There's no trails for this experiment!",Toast.LENGTH_SHORT);
                toast.show();
            }else{
                resultFragment = new ResultFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container, resultFragment).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable("result",trails_DataList);
                resultFragment.setArguments(bundle);
            }
//            startActivity(new Intent(TrailsActivity.this,Test.class));
            return true;
        }else if (id == R.id.ScanQROpt) { // this is where you put generate the qr
            startActivity(new Intent(TrailsActivity.this, ScanActivity.class));

        }else if (id == R.id.QROpt){  // this is where you put the scan yi scan
            if(trails_DataList.size()==0){
                Toast toast = Toast.makeText(getApplicationContext(),"There's no trails for this experiment!",Toast.LENGTH_SHORT);
                toast.show();
            }else {
                qrcode = new QrcodeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
                transaction.replace(R.id.data_container2, qrcode);
                transaction.commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable("result",trails_DataList);
                qrcode.setArguments(bundle);

            }
        }else if (id == R.id.HelpOpt){
            //tips for user
            Toast.makeText(getApplicationContext(),"Welcome! Please note: Long Click item for deleting Short Click item for editting",Toast.LENGTH_SHORT).show();
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
    public String getDescription() {
        return description;
    }
    public String getTitleName(){
        return title;
    }

    public void openQuestionActivity() {
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }
}

