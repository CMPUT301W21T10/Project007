package com.example.project007;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a terminal and called from homeFragment and contain multiple functionality
 * <p>
 * implements AddBinoTrailFragment.FragmentInteractionListener, AddNnCBTrailFragment.FragmentInteractionListener.<br>
 * </p>
 * */
public class TrailsActivity extends AppCompatActivity implements AddBinoTrailFragment.FragmentInteractionListener, AddNnCBTrailFragment.FragmentInteractionListener {
    private ListView trail_List;
    private ArrayAdapter<Trails> trail_Adapter;
    private ArrayList<Trails> trails_DataList;
    private ArrayList<Location> location_DataList;
    private ArrayList<String> trailsTitle_DataList;
    private AddBinoTrailFragment addBinoTrailFragment;
    private AddNnCBTrailFragment addNnCBTrailFragment;
    private AllMapViewFragment ViewAllMapFragment;
    private final String TAG = "Trails_Sample";
    private double currentLat = 0;
    private double currentLong = 0;
    private boolean ignoreCheck = false;


    private TextView descriptionTrail;
    private ResultFragment resultFragment;
    private QrcodeFragment qrcode;
    private Experiment experiment;
    private boolean needLocation;
    private String type;
    private String binomial_type="null";

    private String description;
    private String title;


    android.location.Location currrentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trails_activity_main);
        //database for unique trails
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        TrailsDatabaseController.setTrail_db(db);
        final CollectionReference collectionReference = db.collection("Trails");
        //database for unique trails

        //get device current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); //require activity
        if (ActivityCompat.checkSelfPermission(TrailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(TrailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLoc();
        } else {
            //request permission here
            ActivityCompat.requestPermissions(TrailsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 100);
        }


        //receive data from experiment
        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.project007.INSTANCE");
        Integer position = intent.getIntExtra("com.example.project007.POSITION", -1);
        //receive data from experiment

        type = experiment.getType();
        needLocation = experiment.isRequireLocation();
        description = experiment.getDescription();
        title = experiment.getName();

        TextView nameView = findViewById(R.id.name_view);
        TextView process = findViewById(R.id.process);
        TextView locationView = findViewById(R.id.location);
        TextView owner = findViewById(R.id.owner);
        TextView descriptionView = findViewById(R.id.description);
        TextView region = findViewById(R.id.region);
        TextView minimumTrails = findViewById(R.id.minimumTrails);
        TextView dateView = findViewById(R.id.date);
        TextView typeView = findViewById(R.id.type);
        ImageView imageView = findViewById(R.id.experiment_image);

        nameView.setText(title);
        typeView.setText(type);
        descriptionView.setText(description);
        region.setText(experiment.getRegion());
        minimumTrails.setText(experiment.getMinimumTrails().toString());


        dateView.setText(experiment.getDate());

        switch (type) {
            case "Binomial":
                imageView.setImageResource(R.drawable.b);
                break;
            case "Measurement":
                imageView.setImageResource(R.drawable.m);
                break;
            case "Count-base":
                imageView.setImageResource(R.drawable.c);
                break;
            case "Non-negative":
                imageView.setImageResource(R.drawable.n);
                break;
        }

        if (needLocation) {
            locationView.setVisibility(View.VISIBLE);
        }
        if (experiment.isCondition()) {
            process.setVisibility(View.VISIBLE);
        } else {
            process.setVisibility(View.VISIBLE);
            process.getBackground().setColorFilter(getResources().getColor(R.color.clearRed), PorterDuff.Mode.SRC_ATOP);
            process.setText("End");
        }

        if (experiment.getUserId().equals(DatabaseController.getUserId())) {
            owner.setVisibility(View.VISIBLE);
        }


        trail_List = findViewById(R.id.trail_list);

        trails_DataList = new ArrayList<>();
        location_DataList = new ArrayList<>();
        trailsTitle_DataList = new ArrayList<>();

        if (type.equals("Binomial")) {
            trail_Adapter = new TrailList_Bino(this, trails_DataList);
        } else {
            trail_Adapter = new TrailList_OtherFrags(this, trails_DataList);
        }


        trail_List.setAdapter(trail_Adapter);

        //https://stackoverflow.com/questions/41350269/my-listview-is-showing-the-object-and-not-the-contents-of-each-object/41350519
        //answered by stephen Ruda Dec 27 '16 at 18:35


        //fire store uploading

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("ShowToast")
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error != null) {
                    Log.d(TAG, "Error:" + error.getMessage());
                } else {
                    trails_DataList.clear();
                    int trailId = 0;
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
                        String longitude = (String) doc.getData().get("longitude");
                        String latitude = (String) doc.getData().get("latitude");
                        String ignore = (String) doc.getData().get("IgnoreCondition");
                        String UserId = (String) doc.getData().get("UserId");
                        Location location;

                        boolean ignoreCondition = Boolean.parseBoolean(ignore);

                        if (longitude != null & latitude != null) {
                            location = new Location(Double.parseDouble(longitude), Double.parseDouble(latitude));//error prone
                        } else {
                            location = null;
                        }
                        //uncertain value

                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);
                        if (ID > trailId) {
                            trailId = ID;
                        }
                        if (experiment.getTrailsId().contains(idString)) {
                            if (success == null) {//case for non-binomial trails
                                if (location != null) {
                                    trails_DataList.add(new Trails(trail_title, date, type, time, variesData, ID, location, ignoreCondition, UserId));
                                    location_DataList.add(location);
                                    trailsTitle_DataList.add(trail_title);
                                } else {
                                    trails_DataList.add(new Trails(trail_title, date, type, time, variesData, ID, ignoreCondition, UserId));
                                }
                            } else if (variesData == null) {//case for binomial trails
                                if (location != null) {
                                    trails_DataList.add(new Trails(trail_title, date, type, time, success, failure, ID, location, ignoreCondition, UserId));
                                    location_DataList.add(location);
                                    trailsTitle_DataList.add(trail_title);
                                } else {
                                    trails_DataList.add(new Trails(trail_title, date, type, time, success, failure, ID, ignoreCondition, UserId));
                                }
                            }
                        }
                    }

                    TrailsDatabaseController.setMaxTrailId(trailId);
                    trail_Adapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
            }
        });
        //fire store uploading

        //add button is where we specify the different experiment trails
        //once firestrore ready this part will get type from database
        if (type.equals("Binomial")) {

            if (experiment.isCondition()) {
                trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Trails newtrail = trail_Adapter.getItem(position);
                        int pos = parent.getPositionForView(view);
                        PopupMenu popup = new PopupMenu(TrailsActivity.this, view);
                        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                String databaseUserId = DatabaseController.getUserId();
                                String experimentId = experiment.getUserId();
                                chooseIgnore(id, newtrail, databaseUserId, experimentId);
                                trail_Adapter.notifyDataSetChanged();
                                return false;
                            }
                        });
                        //
                        popup.show();

                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "You can't modify this trail while experiment is ended!", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (experiment.isCondition()) {

                trail_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Trails newtrail = trail_Adapter.getItem(position);
                        int pos = parent.getPositionForView(view);
                        PopupMenu popup = new PopupMenu(TrailsActivity.this, view);
                        popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                String databaseUserId = DatabaseController.getUserId();
                                String experimentId = experiment.getUserId();
                                //compare with experimenter id
                                chooseIgnore(id, newtrail, databaseUserId, experimentId);
                                trail_Adapter.notifyDataSetChanged();
                                return false;
                            }
                        });
                        popup.show();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "You can't modify this trail while experiment is ended!", Toast.LENGTH_SHORT).show();
            }
        }


        //longClick action for delete data
        if (experiment.isCondition()) {
            trail_List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //Delete event with long click vibrate
                    //https://stackoverflow.com/questions/9509840/how-can-i-add-a-vibrate-event-to-the-onlongclick-method
                    //by blessenm answered Mar 1 '12 at 3:50
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(10);
                    String databaseUserId = DatabaseController.getUserId();
                    String experimentId = experiment.getUserId();
                    if (databaseUserId.matches(experimentId)) {
                        Trails newtrail = trail_Adapter.getItem(position);
                        String idString = newtrail.getID().toString();

                        ArrayList<String> temp = experiment.getTrailsId();
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).equals(idString)) {
                                temp.remove(i);
                            }
                        }
                        DatabaseController.setExperimentTrails(experiment.getId().toString(), temp, experiment.getSubscriptionId());

                        trail_Adapter.notifyDataSetChanged();

                        boolean deleteResult = TrailsDatabaseController.delete_Trails("Trails", newtrail);
                        if (deleteResult) {
                            Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You don't have access to delete this trail", Toast.LENGTH_SHORT).show();
                    }

                    //revert logic
                    return false;
                }
            });
            trail_List.setAdapter(trail_Adapter);
            //https://stackoverflow.com/questions/4834750/how-to-get-the-selected-item-from-listview
            //from xandy's answer Jan 29 '11 at 2:57*/
        }
    }


    private void chooseIgnore(int id, Trails newtrail, String remoteUserId, String localUserId) {
        final FirebaseFirestore db;
        db = DatabaseController.getDb();
        if (id == R.id.ignoreOpt){
            if (remoteUserId.matches(localUserId)){
                Toast.makeText(getApplicationContext(),"Trail Ignored! Click again for Undo",Toast.LENGTH_SHORT).show();
                newtrail.setIgnoreCondition(true);
                ignoreCheck = true;
                boolean addResult = TrailsDatabaseController.modify_Trails("Trails", newtrail);
            }else{
                Toast.makeText(getApplicationContext(),"Experimenter cannot Ignore Trails",Toast.LENGTH_SHORT).show();
            }

        }else if(id == R.id.UndoOpt){
            if (remoteUserId.matches(localUserId)){
                Toast.makeText(getApplicationContext(),"Trail Un-Ignored!",Toast.LENGTH_SHORT).show();
                newtrail.setIgnoreCondition(false);
                ignoreCheck = false;
                boolean addResult = TrailsDatabaseController.modify_Trails("Trails", newtrail);
            }else{
                Toast.makeText(getApplicationContext(),"Experimenter cannot Ignore Trails",Toast.LENGTH_SHORT).show();
            }
        }else if(id==R.id.QRcodeOpt) {
            if (newtrail.getType().equals("Binomial")) {
                showNormalDialog(newtrail);
            } else {
                qrcode = new QrcodeFragment(newtrail, "trail", binomial_type);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.data_container2, qrcode).addToBackStack(null);
                transaction.commit();
            }
        }
        else if(id==R.id.addBarcode){
            Intent intent = new Intent(TrailsActivity.this,ReadBarCodeActivity.class);
            intent.putExtra("IsBarcode","true");
            intent.putExtra("ID",newtrail.getUserId());
            intent.putExtra("trail_id",newtrail.getID().toString());
            startActivity(intent);
        }
        else if(id==R.id.deleteBarcode){
            final DocumentReference DocumentReference = db.collection("Trails").document(newtrail.getID().toString());
            Map<String,Object> updates = new HashMap<>();
            updates.put(newtrail.getUserId(), FieldValue.delete());
            DocumentReference.update(updates);
            Toast toast = Toast.makeText(getApplicationContext(),"Successfully clear barcode.",Toast.LENGTH_SHORT);
            toast.show();


        }
    }
    private void showNormalDialog(Trails newtrail){
        /* @setMessage set message
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(TrailsActivity.this);
        normalDialog.setTitle("Select Success or Failure type for generating qrcode.");
        normalDialog.setPositiveButton("Success",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binomial_type="success";
                        qrcode=new QrcodeFragment(newtrail,"trail",binomial_type);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.data_container2, qrcode).addToBackStack(null);
                        transaction.commit();
                    }
                });
        normalDialog.setNegativeButton("Failure",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binomial_type="failure";
                        qrcode=new QrcodeFragment(newtrail,"trail",binomial_type);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.data_container2, qrcode).addToBackStack(null);
                        transaction.commit();
                    }
                });
        normalDialog.show();
    }


    @SuppressLint("MissingPermission")//supress all permission request
    private void getCurrentLoc() {
        //where we use to get the current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    android.location.Location location = task.getResult();
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                    } else {
                        //if it doesn't have a location we will make a request for location
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                android.location.Location Alocation = locationResult.getLastLocation();
                                currentLat = Alocation.getLatitude();
                                currentLong = Alocation.getLongitude();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void sending_data(Trails trails) {
        trail_Adapter.add(trails);
        boolean addResult = TrailsDatabaseController.modify_Trails("Trails", trails);
        ArrayList<String> valueList = experiment.getTrailsId();
        valueList.add(trails.getID().toString());
        ArrayList<String> subscriptionList = experiment.getSubscriptionId();
        if (!subscriptionList.contains(DatabaseController.getUserId())){
            subscriptionList.add(DatabaseController.getUserId());
        }
        DatabaseController.setExperimentTrails(experiment.getId().toString(), valueList,subscriptionList );
        if (addResult){
            Toast.makeText(getApplicationContext(), "Add Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Add Succeed", Toast.LENGTH_SHORT).show();
        }
        //revert logic
    }

    @Override
    public void editing_data(Trails trails) {//still malfunctioning...
        trail_Adapter.notifyDataSetChanged();
        boolean editResult = TrailsDatabaseController.modify_Trails("Trails", trails);
        ArrayList<String> valueList = experiment.getTrailsId();
        ArrayList<String> subList = experiment.getSubscriptionId();
        DatabaseController.setExperimentTrails(experiment.getId().toString(), valueList, subList);
        if (editResult){
            Toast.makeText(getApplicationContext(), "Edit Succeed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Edit Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0]) + grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLoc();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied, Cannot access current location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!experiment.isCondition()) {
            menu.findItem(R.id.action_add).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
        return true;
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
                ArrayList<Trails> trails_list = null;
                for(Trails t:trails_DataList){
                    if(!t.isIgnoreCondition()){
                        trails_list.add(t);
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container, resultFragment).addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable("result",trails_list);
                resultFragment.setArguments(bundle);
            }
            return true;
        }else if (id == R.id.QROpt){  // this is where you put the scan yi scan
            if(trails_DataList.size()==0){
                Toast toast = Toast.makeText(getApplicationContext(),"There's no trails for this experiment!",Toast.LENGTH_SHORT);
                toast.show();
            }else {
                qrcode = new QrcodeFragment("experiment");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.data_container2, qrcode);
                transaction.addToBackStack(null).commit();
                Bundle bundle = new Bundle();
                bundle.putSerializable("result",trails_DataList);
                qrcode.setArguments(bundle);

            }
            return true;
        }else if (id == R.id.LocView){
            //view all location for this experiment
            //extends a new frag to show this
            ViewAllMapFragment = new AllMapViewFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.data_container2, ViewAllMapFragment).addToBackStack(null).commit();
            return true;
        }else if (id == R.id.HelpOpt){
            //tips for user
            Toast.makeText(getApplicationContext(),"Welcome! Please note: Long Click item for item deleting~",Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_add){
            if (type.equals("Binomial")) {
                addBinoTrailFragment = new AddBinoTrailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container2, addBinoTrailFragment).addToBackStack(null).commit();
            }else{
                addNnCBTrailFragment = new AddNnCBTrailFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.data_container2, addNnCBTrailFragment).addToBackStack(null).commit();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    //sending data from activity to frags

    /**
     * This returns Boolean for needing location
     * @return
     * Return Boolean value
     */
    public boolean WhetherTrailsLoc() {
        return needLocation;
    }

    /**
     * This returns String for CurrentLatitude
     * @return
     * Return String type value
     */
    public double sendCurrentLat() {
        return currentLat;
    }

    /**
     * This returns String for CurrentLongitude
     * @return
     * Return String type value
     */
    public double sendCurrentLong() {
        return currentLong;
    }

    public ArrayList<Location> sendLocationData(){
        return  location_DataList;
    }

    public ArrayList<String> sendTrailsTitleData(){
        return  trailsTitle_DataList;
    }
    /**
     * This returns String for type
     * @return
     * Return String type value
     */
    public String getTrailsType() {
        return type;
    }

    /**
     * This returns String for Trails description
     * @return
     * Return String value description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This returns String for Title name of trails
     * @return
     * Return String type value
     */
    public String getTitleName(){
        return title;
    }

    /**
     * Start question activity
     */
    public void openQuestionActivity() {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("ExperimentFromTrail", experiment);
        startActivity(intent);
    }

}

