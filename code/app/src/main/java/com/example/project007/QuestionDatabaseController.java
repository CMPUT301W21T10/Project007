package com.example.project007;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class QuestionDatabaseController {

    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore question_db;
    private static String UserId = "1";
    private static Integer maxQuestionId=0;

    public static FirebaseFirestore getQuestion_db() {
        return question_db;
    }

    public static void setQuestion_db(FirebaseFirestore question_db) {
        QuestionDatabaseController.question_db = question_db;
    }

    public static String getUserId() {
        return UserId;
    }

    public static void setUserId(String userId) {
        UserId = userId;
    }

    public static Integer getMaxQuestionId() {
        return maxQuestionId;
    }

    public static void setMaxQuestionId(Integer maxQuestionId) {
        QuestionDatabaseController.maxQuestionId = maxQuestionId;
    }

    /**
     * This returns a boolean and create a new table for storing Question in DB
     * @return
     * Return Boolean shows adding result
     */
    public static boolean add_Question(String collection, Question question){
        CollectionReference collectionReference =  question_db.collection(collection);
        HashMap<String, Object> data = new HashMap<>();

        String idString = question.getId().toString();
        data.put("Question", question.getQuestion());
        if (question.getAnswer_id() != null){
            data.put("Answer_Id", question.getAnswer_id());
        }else{
            data.put("Answer_Id", null);
        }

        final boolean[] condition = new boolean[1];
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
    /**
     * This returns a boolean and delete a Question in DB
     * @return
     * Return Boolean shows deleting result
     */
    public static boolean delete_Question(String collection, Question question){
        CollectionReference collectionReference =  question_db.collection(collection);
        String idString = question.getId().toString();
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


    /**
     * This function is design to Generate new id for Question
     * @return
     * Return Integer
     */
    public static Integer generateQuestionId(){
        return maxQuestionId + 1;
    }

    /**
     * This function is design to delete a connection between Question and Answers when deleting Questions
     * @return
     * Return Void
     */
    public static boolean setQuestionanswer(String id, ArrayList<String> valueList){
        DocumentReference docRef = question_db.collection("Questions").document(id);
        docRef.update("Answer_Id", valueList);
        return true;
    }
}
