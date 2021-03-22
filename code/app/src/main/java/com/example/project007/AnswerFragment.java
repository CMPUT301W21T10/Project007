package com.example.project007;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;


public class AnswerFragment extends Fragment {

    ListView answerList;
    ArrayAdapter<Answer> answerAdapter;

    String question;
    ArrayList<Answer> answerDataList;
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
        /*String question_Id = getArguments().getString("Question Id");
        String answer_Id = getArguments().getString("Answer Id");*/
        String question_Id = "1";
        String answer_Id = "1";

        answerDataList = new ArrayList<>();
        answerAdapter = new answerCustomList(getActivity(), answerDataList);

        answerList.setAdapter(answerAdapter);
        db = FirebaseFirestore.getInstance();


        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerName = addAnswerEditText.getText().toString();
                Toast.makeText(getActivity(), "answer is " + answerName, Toast.LENGTH_SHORT).show();
                Answer answer = new Answer(Integer.parseInt(answer_Id), answerName, Integer.parseInt(question_Id));
                answerAdapter.add(answer);
                //answerAdapter.notifyDataSetChanged();
                addAnswerEditText.setText("");
                //addAnswerEditText.setText("");
                /*if(answerName.length()>0) {
                    Answer answer = new Answer(Integer.parseInt(answer_Id), answerName, Integer.parseInt(question_Id));
                    answerAdapter.add(answer);
                    boolean addAnswer = AnswerDatabaseController.add_Answer("Answers", answer);
                    if (addAnswer){
                        Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    addAnswerEditText.setText("");

                }*/

            }
        });

        answerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                Answer answer = answerAdapter.getItem(position);
                answerAdapter.remove(answer);
                answerAdapter.notifyDataSetChanged();/*
                boolean deleteAnswer = AnswerDatabaseController.delete_Answer("Answers", answer);
                if (deleteAnswer){
                    Toast.makeText(getActivity(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                    answerAdapter.remove(answer);
                }
                else{
                    Toast.makeText(getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }*/
                return true;
            }
        });

        return v;
    }

}