package com.example.project007;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TrailsDatabaseController {
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore Trail_db;
    private static String UserId = "1";//wtf is this???
    private static Integer maxTrailId;

    public static Integer getMaxTrailId() {
        return maxTrailId;
    }

    public static void setMaxTrailId(Integer maxTrailId) {
        TrailsDatabaseController.maxTrailId = maxTrailId;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static FirebaseFirestore getTrail_db() {
        return Trail_db;
    }

    public static void setTrail_db(FirebaseFirestore trail_db) {
        Trail_db = trail_db;
    }

    public static boolean modify_Trails(String collection, Trails trails){
        // Retrieving the city name and the province name from the EditText fields
        CollectionReference collectionReference =  Trail_db.collection(collection);
        HashMap<String, String> data = new HashMap<>();

        String idString = trails.getID().toString();
        data.put("Trail_title", trails.getTrail_title());
        data.put("Date", trails.getDate());
        data.put("Type", trails.getType());
        data.put("Time", trails.getTime());
        if (trails.getSuccess()!=null){
            data.put("Success", trails.getSuccess());
        }else{
            data.put("Success", null);
        }
        if (trails.getFailure()!=null){
            data.put("Failure", trails.getFailure());
        }else{
            data.put("Failure", null);
        }
        if (trails.getVariesData()!=null){
            data.put("VariesData", trails.getVariesData());
        }else{
            data.put("VariesData", null);
        }
        if (trails.getLocation()!=null){
            data.put("Location", trails.getLocation().toString());
        }else{
            data.put("Location", null);
        }



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
    public static boolean delete_Trails(String collection, Trails trails){
        CollectionReference collectionReference =  Trail_db.collection(collection);
        String idString = trails.getID().toString();
        final boolean[] condition = new boolean[1];
        collectionReference
                .document(idString)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been deleted successfully!");
                        condition[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be deleted!" + e.toString());
                        condition[0] = false;
                    }
                });
        return condition[0];
    }

    /*public static boolean edit_Trails(String collection, Trails trails){
        CollectionReference collectionReference =  Trail_db.collection(collection);
        String idString = trails.getID().toString();
        final boolean[] condition = new boolean[1];
        collectionReference
                .document(idString)
                .update((Map<String, Object>) trails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been deleted successfully!");
                        condition[0] = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be deleted!" + e.toString());
                        condition[0] = false;
                    }
                });
        return condition[0];
    }*/


    public static void deleteExperiment(String name){
        Task<Void> writeResult = Trail_db.collection("Trails").document(name).delete();

    }

    public static Integer generateTrailsId(){
        return maxTrailId + 1;
    }
}
