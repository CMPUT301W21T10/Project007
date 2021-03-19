package com.example.project007;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class AnswerFragment extends Fragment {

    ListView answerList;
    ArrayAdapter<String> answerAdapter;
    ArrayList<Question> questionDataList;
    String question;
    ArrayList<String> answerDataList;
    Button addAnswerButton;
    EditText addAnswerEditText;
    FirebaseFirestore db;


    public AnswerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);

        addAnswerEditText = v.findViewById(R.id.add_answer_text);
        addAnswerButton = v.findViewById(R.id.add_answer_button);

        answerList = v.findViewById(R.id.answer_list);
        question = getArguments().getString("Question Content");
        answerDataList = new ArrayList<String>();
        answerAdapter = new answerCustomList(getActivity(), answerDataList);

        answerList.setAdapter(answerAdapter);
        db = FirebaseFirestore.getInstance();

        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerName = addAnswerEditText.getText().toString();
                answerDataList.add(answerName);
                addAnswerEditText.setText("");

            }
        });

        getActivity().onBackPressed();

        return v;
    }

}