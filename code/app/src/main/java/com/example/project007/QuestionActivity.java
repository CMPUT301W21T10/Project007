package com.example.project007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
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
    Integer id;
    ArrayList<String> answer_id = new ArrayList<String>();
    Question question;


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

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionEditText = findViewById(R.id.add_question_text);

        questionList = findViewById(R.id.question_list);
        questionDataList = new ArrayList<Question>();
        questionAdapter = new questionCustomList(this, questionDataList);


        questionList.setAdapter(questionAdapter);

        //fire store uploading

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


                       /*if (experiment.getTrailsId() != null && experiment.getTrailsId().contains(idString)){

                       }*/
                    }
                    questionAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
                QuestionDatabaseController.setMaxQuestionId(questionDataList.size());
            }
        });
        //fire store uploading


        //Calling add frag

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String questionName = addQuestionEditText.getText().toString();
                if(questionName.length()>0) {
                    Question question = new Question(QuestionDatabaseController.generateQuestionId(), questionName, answer_id);
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

        /*addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String questionName = addQuestionEditText.getText().toString();

                if(questionName.length()>0) {
                    Question question = new Question(id, questionName, answer_id);
                    questionAdapter.add(question);
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
        });*/

        //click question to view answers
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                question = questionAdapter.getItem(position);
                /*if(question != null) {
                    Toast.makeText(getApplicationContext(), "Select1 Succeed", Toast.LENGTH_SHORT).show();
                }*/
                AnswerFragment fragment = new AnswerFragment();
                //Bundle b1 = new Bundle();
                /*Bundle b2 = new Bundle();
                Bundle b3 = new Bundle();*/
                //b1.putString("Question Content", question.getQuestion());
                /*b2.putString("Question Id", question.getId().toString());
                b3.putString("Answer Id", question.getAnswer_id().toString());*/
                //fragment.setArguments(b1);
                /*fragment.setArguments(b2);
                fragment.setArguments(b3);*/
                //findViewById(R.id.container_answer).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_answer, fragment).addToBackStack(null).commit();

            }
        });

        //Delete event
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
    //sending value from activity to frag
    public Question SendQuestion(){
        return question;
    }
    public Question SendQuestionId(){
        return question;
    }
    //sending value from activity to frag

}