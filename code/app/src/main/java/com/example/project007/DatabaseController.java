package com.example.project007;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Connect to the Database
 * perform add, delete, modify Experiment
 */
public class DatabaseController {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;
    private static String UserId;//get this in trails
    private static Integer maxExperimentId;


    public static Integer getMaxExperimentId() {
        return maxExperimentId;
    }

    public static void setMaxExperimentId(Integer maxExperimentId) {
        DatabaseController.maxExperimentId = maxExperimentId;

    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static void setDb(FirebaseFirestore db) {
        DatabaseController.db = db;
    }

    public static boolean modify_experiment(String collection, Experiment experiment){
        // Retrieving the city name and the province name from the EditText fields
        CollectionReference collectionReference =  db.collection(collection);
        String idString = experiment.getId().toString();

        final boolean[] condition = new boolean[1];
        // The set method sets a unique id for the document
        collectionReference
                .document(idString)
                .set(experiment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                        condition[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                        condition[0] = false;
                    }
                });
        return condition[0];
    }

    public static void deleteExperiment(Experiment instance){
        ArrayList<String> ids = instance.getTrailsId();
        for (int i = 0;i<ids.size();i++){
            db.collection("Trails").document(String.valueOf(ids.get(i))).delete();
        }
        Task<Void> writeResult = db.collection("Experiments").document(String.valueOf(instance.getId())).delete();

    }

    public static Integer generateExperimentId(){
        return maxExperimentId + 1;
    }

    public static void setExperimentTrails(String id, ArrayList<String> valueList,ArrayList<String> valueList2){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef.update("trailsId", valueList);
        docRef.update("subscriptionId", valueList2);

    }

    public static String getUserName(String uid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String[] userName = {"anonymity"};

        /*
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UserEntity user = dataSnapshot.getValue(UserEntity.class);
                userName[0] = user.getUsername();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        reference.addValueEventListener(postListener);
    */
        reference.child("data").child(uid).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    userName[0] = String.valueOf(task.getResult().getValue());
                    Log.e("firebase", "Error getting data", task.getException());

                }
            }
        });

        return userName[0];
    }
}
