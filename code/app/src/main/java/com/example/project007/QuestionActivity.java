package com.example.project007;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity shows the questions asked by others of an unique experiment
 * It also alows people to click to brose and modify answers of the question
 */
public class QuestionActivity extends AppCompatActivity {

    private ListView questionList;
    private ArrayAdapter<Question> questionAdapter;
    private ArrayList<Question> questionDataList;
    private ArrayList<String> answer_id = new ArrayList<>();
    private Question question;
    private ArrayList<String> questionsId = new ArrayList<>();
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
        experiment = (Experiment) intent.getSerializableExtra("ExperimentFromTrail");
        //receive data from experiment

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionEditText = findViewById(R.id.add_question_text);

        questionList = findViewById(R.id.question_list);
        questionDataList = new ArrayList<>();
        questionAdapter = new questionCustomList(this, questionDataList);

        questionList.setAdapter(questionAdapter);

        /**
         * Firestore uploading
         */
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }
                else {
                    questionDataList.clear();
                    int questionId = 0;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        String idString = doc.getId();
                        int ID = Integer.parseInt(idString);
                        if (ID > questionId) {
                            questionId = ID;
                        }
                        Question oneQuestion = null;
                        if (experiment.getQuestionId().contains(idString)) {
                            if (doc.exists()) {
                                // convert document to POJO
                                oneQuestion = doc.toObject(Question.class);
                                //System.out.println(oneExperiment);
                                questionDataList.add(oneQuestion);
                            } else {
                                System.out.println("No such document!");
                            }
                        }
                    }
                    QuestionDatabaseController.setMaxQuestionId(questionId);
                    questionAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }

                /*int questionId = 0;
                for (int i = 0; i < questionDataList.size(); i++){
                    if ( questionDataList.get(i).getId() > questionId){
                        questionId = questionDataList.get(i).getId();
                    }
                }*/

            }
        });


        /**
         * Calling add frag
         */

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String questionName = addQuestionEditText.getText().toString();
                if(questionName.length()>0) {
                    Question question = new Question(QuestionDatabaseController.generateQuestionId(), questionName, answer_id);
                    ArrayList<String> valueList = experiment.getQuestionId();
                    valueList.add(question.getId().toString());
                    DatabaseController.setExperimentQuestions(experiment.getId().toString(), valueList);
                    //lock on answer
                    boolean addQuestion = QuestionDatabaseController.add_Question("Questions", question);
                    if (addQuestion){
                        Toast.makeText(getApplicationContext(), "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Add Succeed", Toast.LENGTH_SHORT).show();
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
                String databaseUserId = DatabaseController.getUserId();
                String experimentId = experiment.getUserId();
                if (databaseUserId.matches(experimentId)){
                    Question question = questionAdapter.getItem(position);
                    String idString = question.getId().toString();

                    ArrayList<String> temp =  experiment.getQuestionId();
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).equals(idString)) {
                            temp.remove(i);
                        }
                    }
                    DatabaseController.setExperimentQuestions(experiment.getId().toString(), temp);
                    boolean deleteQuestion = QuestionDatabaseController.delete_Question("Questions", question);
                    questionAdapter.notifyDataSetChanged();
                    if (deleteQuestion){
                        Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Access granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Oops, you have no access to delete this question", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    /**
     * sending value from activity to frag
     * @return question
     */
    public Question SendQuestion(){
        return question;
    }
    //sending value from activity to frag

}
