package com.example.project007;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * SearchResult class
 * Perform search and show result
 * called from MainActivity
 */
public class SearchResult extends AppCompatActivity {

    private SubscriptionViewModel subscriptionViewModel;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> experimentDataList;
    private final String TAG = "Sample";
    private String searchKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        searchKey = intent.getStringExtra("Key");

        experimentDataList = new ArrayList<>();
        experimentAdapter = new ExperimentAdapter(this, experimentDataList);
        ListView experimentList = findViewById(R.id.subscript_list);
        experimentList.setAdapter(experimentAdapter);

        final FirebaseFirestore db;
        db = DatabaseController.getDb();
        final CollectionReference collectionReference = db.collection("Experiments");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    experimentDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Experiment oneExperiment = null;
                        if (doc.exists()) {
                            // convert document to POJO
                            oneExperiment = doc.toObject(Experiment.class);
                            if (processData(oneExperiment)){
                                experimentDataList.add(oneExperiment);
                            }

                        } else {
                            System.out.println("No such document!");
                        }

                    }
                    if(experimentDataList.size() == 0) {
                        final Toast test = Toast.makeText(SearchResult.this, "No related result.", Toast.LENGTH_SHORT);
                        test.show();
                        test.cancel();
                        SearchResult.this.finish();
                    }
                }
                experimentAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched

            }
        });

        // listener to access detail of an element
        // package an experiment and position info in intent
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchResult.this, TrailsActivity.class);
                Experiment instanceExperiment = experimentDataList.get(position);
                intent.putExtra("com.example.project007.INSTANCE", instanceExperiment);
                intent.putExtra("com.example.project007.POSITION", position);
                startActivity(intent);
            }
        });

    }

    public boolean processData( Experiment experiment){

        if (!experiment.isPublishCondition()){
            return false;
        }

        if (experiment.getName().contains(searchKey)){
            return true;

        }
        if (experiment.getDescription().contains(searchKey)){
            return true;

        }
        if (experiment.getRegion().contains(searchKey)){
            return true;

        }
        if (experiment.getType().contains(searchKey)){
            return true;

        }
        if (experiment.getDate().contains(searchKey)){
            return true;

        }

        if (experiment.isCondition() && searchKey.equals("End")){
            return true;

        }
        return !experiment.isCondition() && searchKey.equals("Processing");

    }
}