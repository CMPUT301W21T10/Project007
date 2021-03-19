package com.example.project007;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ListView questionList;
    ArrayAdapter<Question> questionAdapter;
    ArrayList<Question> questionDataList;
    ArrayList<String> answerDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Button addQuestionButton;
        final EditText addQuestionEditText;
        FirebaseFirestore db;

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionEditText = findViewById(R.id.add_question_text);

        questionList = findViewById(R.id.question_list);
        questionDataList = new ArrayList<Question>();
        answerDataList = new ArrayList<String>();
        questionAdapter = new questionCustomList(this, questionDataList);

        questionList.setAdapter(questionAdapter);
        db = FirebaseFirestore.getInstance();

        //final CollectionReference collectionReference = db.collection("Cities");

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String questionName = addQuestionEditText.getText().toString();
                questionDataList.add(new Question(questionName, answerDataList));
                addQuestionEditText.setText("");

            }
        });

        //click question to view answers
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = questionAdapter.getItem(position);

                FragmentManager fm = getSupportFragmentManager();
                AnswerFragment fragment = new AnswerFragment();
                fm.beginTransaction().replace(R.id.question_activity, fragment).commit();
                Bundle b = new Bundle();
                b.putSerializable("Question Content", question);
                fragment.setArguments(b);
            }
        });



    }
}