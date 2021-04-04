package com.example.project007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    ListView questionList;
    ArrayAdapter<Question> questionAdapter;
    ArrayList<Question> questionDataList;
    //Integer id;
    ArrayList<String> answer_id = new ArrayList<String>();
    Question question;
    private Experiment experiment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Button addQuestionButton;
        final String TAG = "Sample";
        final EditText addQuestionEditText;

        //database for unique questions
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        QuestionDatabaseController.setQuestion_db(db);
        final CollectionReference collectionReference = db.collection("Questions");
        //database for unique questions

        //receive data from experiment
        Intent intent = getIntent();
        experiment = (Experiment) intent.getSerializableExtra("com.example.project007.INSTANCE");
        //receive data from experiment

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionEditText = findViewById(R.id.add_question_text);

        questionList = findViewById(R.id.question_list);
        questionDataList = new ArrayList<Question>();
        questionAdapter = new questionCustomList(this, questionDataList);

        questionList.setAdapter(questionAdapter);

        /**
         * fire store uploading
         */
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }else{
                    questionDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Question")));
                        String Question = (String) doc.getData().get("Question");
                        ArrayList<String> Answer_id = (ArrayList<String>)doc.getData().get("Answer_Id");
                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);

                        questionDataList.add(new Question(ID, Question, Answer_id));

                    }
                    questionAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
                QuestionDatabaseController.setMaxQuestionId(questionDataList.size());
            }
        });
        //fire store uploading


        /**
         * Calling add frag
         */

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String questionName = addQuestionEditText.getText().toString();
                if(questionName.length()>0) {
                    Question question = new Question(QuestionDatabaseController.generateQuestionId(), questionName, answer_id);
                    //lock on question
                    ArrayList<String> valueList = experiment.getQuestionId();
                    valueList.add(experiment.getId().toString());
                    DatabaseController.setExperimentQuestions(experiment.getId().toString(), valueList);
                    //lock on answer
                    boolean addQuestion = QuestionDatabaseController.add_Question("Questions", question);
                    if (addQuestion){
                        Toast.makeText(getApplicationContext(), "Add Succeed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    addQuestionEditText.setText("");
                }
            }
        });

        //Calling add frag



        /**
         * click question to view answers
         */
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                question = questionAdapter.getItem(position);
                AnswerFragment fragment = new AnswerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_answer, fragment).addToBackStack(null).commit();

            }
        });

        /**
         * Delete event by long click question
         */
        questionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Question question = questionAdapter.getItem(position);
                boolean deleteQuestion = QuestionDatabaseController.delete_Question("Questions", question);
                questionAdapter.notifyDataSetChanged();
                if (deleteQuestion){
                    Toast.makeText(getApplicationContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


    }

    /**
     * sending value from activity to frag
     * @return
     */
    public Question SendQuestion(){
        return question;
    }
    //sending value from activity to frag

}