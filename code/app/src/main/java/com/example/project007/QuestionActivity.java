package com.example.project007;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionActivity extends AppCompatActivity {

    ListView questionList;
    ArrayAdapter<Question> questionAdapter;
    ArrayList<Question> questionDataList;
    Integer id =  null;
    ArrayList<Integer> answer_id = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Button addQuestionButton;
        final String TAG = "Sample";
        final EditText addQuestionEditText;
        FirebaseFirestore db;

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionEditText = findViewById(R.id.add_question_text);

        questionList = findViewById(R.id.question_list);
        questionDataList = new ArrayList<Question>();

        questionAdapter = new questionCustomList(this, questionDataList);


        questionList.setAdapter(questionAdapter);
        db = FirebaseFirestore.getInstance();

        //final CollectionReference collectionReference = db.collection("Questions");

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
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
        });

        //click question to view answers
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionAdapter.getItem(position);
                if(question != null) {
                    Toast.makeText(getApplicationContext(), "Select1 Succeed", Toast.LENGTH_SHORT).show();
                }
                AnswerFragment fragment = new AnswerFragment();
                //Bundle b1 = new Bundle();
                Bundle b2 = new Bundle();
                Bundle b3 = new Bundle();
                //b1.putString("Question Content", question.getQuestion());
                b2.putString("Question Id", question.getId().toString());
                b3.putString("Answer Id", question.getAnswer_id().toString());
                //fragment.setArguments(b1);
                fragment.setArguments(b2);
                fragment.setArguments(b3);
                //findViewById(R.id.container_answer).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_answer, fragment).addToBackStack(null).commit();

            }
        });

        questionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                Question question = questionAdapter.getItem(position);
                if(question != null) {
                    Toast.makeText(getApplicationContext(), "Select2 Succeed", Toast.LENGTH_SHORT).show();
                }
                questionAdapter.notifyDataSetChanged();
                boolean deleteQuestion = QuestionDatabaseController.delete_Question("Questions", question);
                if (deleteQuestion){
                    Toast.makeText(getApplicationContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                    questionAdapter.remove(question);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });



    }
}