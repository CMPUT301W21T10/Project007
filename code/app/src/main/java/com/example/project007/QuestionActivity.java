package com.example.project007;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    Integer indexVal;


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
        questionAdapter = new questionCustomList(this, questionDataList);

        questionList.setAdapter(questionAdapter);
        db = FirebaseFirestore.getInstance();

        //final CollectionReference collectionReference = db.collection("Cities");

        

    }
}