package com.example.project007;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
    ArrayList<Answer> answerDataList;
    Button addAnswerButton;
    EditText addAnswerEditText;
    Integer answer_Id = 1;
    //FirebaseFirestore db;


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
        answerDataList = new ArrayList<>();
        answerAdapter = new answerCustomList(getActivity(), answerDataList);
        String question_Id = getArguments().getString("Question Id");
        //String answer_Id = getArguments().getString("Answer Id");

        answerList.setAdapter(answerAdapter);
        //db = FirebaseFirestore.getInstance();

        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerName = addAnswerEditText.getText().toString();

                if(answerName.length()>0) {
                    Answer answer = new Answer(answer_Id, answerName, Integer.parseInt(question_Id));
                    answerAdapter.add(answer);
                    boolean addAnswer = AnswerDatabaseController.add_Answer("Answers", answer);
                    if (addAnswer){
                        Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
                        AnswerDatabaseController.setMaxAnswerId(answerDataList.size());
                    }
                    else{
                        Toast.makeText(getActivity(), "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    addAnswerEditText.setText("");

                }

            }
        });

        answerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                Answer answer = answerAdapter.getItem(position);
                boolean deleteAnswer = AnswerDatabaseController.delete_Answer("Answers", answer);
                if (deleteAnswer){
                    Toast.makeText(getActivity(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                    answerAdapter.remove(answer);
                    AnswerDatabaseController.setMaxAnswerId(answerDataList.size());
                    answerAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        return v;
    }

}