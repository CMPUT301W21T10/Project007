package com.example.project007;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;


public class AnswerFragment extends Fragment {

    ListView answerList;
    ArrayAdapter<Answer> answerAdapter;
    ArrayList<Answer> answerDataList;
    Question question;
    String question_detail;
    TextView questionView;

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
        questionView = v.findViewById(R.id.Question_view);


        answerDataList = new ArrayList<>();
        answerAdapter = new answerCustomList(getActivity(), answerDataList);
        answerList.setAdapter(answerAdapter);

        //db initialize
        final String TAG = "Sample";
        db = FirebaseFirestore.getInstance();
        AnswerDatabaseController.setDb(db);
        final CollectionReference collectionReference = db.collection("Answers");
        //db initialize

        //receive data from activity
        QuestionActivity activity = (QuestionActivity) getActivity();
        question = activity.SendQuestion();
        question_detail = question.getQuestion();
        questionView.setText(question_detail);

        //fire store uploading
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                if (error!=null){
                    Log.d(TAG,"Error:"+error.getMessage());
                }else{
                    answerDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Answers")));
                        String answer = (String) doc.getData().get("Answers");

                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);

                        answerDataList.add(new Answer(answer, ID));

                    }
                    answerAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                }
                QuestionDatabaseController.setMaxQuestionId(answerDataList.size());
            }
        });
        //fire store uploading

        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerName = addAnswerEditText.getText().toString();
                if(answerName.length()>0) {
                    Answer answer = new Answer(answerName, AnswerDatabaseController.generateAnswerId());
                    //Toast.makeText(getActivity(), answer.getAnswer(), Toast.LENGTH_SHORT).show();
                    boolean addAnswer = AnswerDatabaseController.add_Answer("Answers", answer);
                    if (addAnswer){
                        Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
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
                answerAdapter.notifyDataSetChanged();
                boolean deleteAnswer = AnswerDatabaseController.delete_Answer("Answers", answer);
                if (deleteAnswer){
                    Toast.makeText(getActivity(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                    answerAdapter.remove(answer);
                }
                else{
                    Toast.makeText(getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return v;
    }

}