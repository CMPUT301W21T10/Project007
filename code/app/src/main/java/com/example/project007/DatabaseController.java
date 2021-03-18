package com.example.project007;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DatabaseController {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;
    private static String UserId = "1";
    private static Integer maxExperimentId;

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
        HashMap<String, String> data = new HashMap<>();

        String idString = experiment.getId().toString();
        data.put("Name", experiment.getName());
        data.put("Description", experiment.getDescription());
        data.put("Date", experiment.getDate());
        data.put("Type", experiment.getType());
        data.put("trailsId", experiment.getUserId());
        data.put("subscriptionId", experiment.getUserId());

        final boolean[] condition = new boolean[1];
        // The set method sets a unique id for the document
        collectionReference
                .document(idString)
                .set(data)
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


    public static Integer generateExperimentId(){
        return maxExperimentId + 1;
    }
}
