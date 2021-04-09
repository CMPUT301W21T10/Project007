package com.example.project007;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;


/**
 * This fragment allows people to brose and modify the answer of a question
 */
public class AnswerFragment extends Fragment {

    private ListView answerList;
    private ArrayAdapter<Answer> answerAdapter;
    private ArrayList<Answer> answerDataList;
    private Question question;
    private String question_detail;
    private TextView questionView;

    private Button addAnswerButton;
    private EditText addAnswerEditText;
    private FirebaseFirestore db;


    public AnswerFragment() {
        // Required empty public constructor
    }

    /**
     * Inflate the layout for this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

        /**
         * db initialize
         */
        final String TAG = "Sample";
        db = FirebaseFirestore.getInstance();
        AnswerDatabaseController.setDb(db);
        final CollectionReference collectionReference = db.collection("Answers");
        //db initialize

        /**
         * receive data from activity
         */
        QuestionActivity activity = (QuestionActivity) getActivity();
        if (activity != null){
            question = activity.SendQuestion();
            question_detail = question.getQuestion();
            questionView.setText(question_detail);
        }
        //receive data from activity

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
                    answerDataList.clear();
                    int answerId = 0;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d(TAG, String.valueOf(doc.getData().get("Answer")));
                        String answer = (String) doc.getData().get("Answer");
                        String idString = doc.getId();
                        Integer ID = Integer.parseInt(idString);
                        if (ID > answerId) {
                            answerId = ID;
                        }

                       /* answerDataList.add(new Answer(answer, ID));
                        Toast.makeText(getActivity(), "I am here" + question.getAnswer_id(), Toast.LENGTH_SHORT).show();*/

                        if (question.getAnswer_id().contains(idString)) {
                            if (answer != null){
                                //Toast.makeText(getActivity(), answer, Toast.LENGTH_SHORT).show();
                                answerDataList.add(new Answer(answer, ID));
                        /*if (question.getAnswer_id() != null && question.getAnswer_id().contains(idString)){////id duplicate => error
                            Toast.makeText(getActivity(), "I am here", Toast.LENGTH_SHORT).show();
                            answerDataList.add(new Answer(answer, ID));
                        }*/
                            }
                        }

                    }
                    answerAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                    AnswerDatabaseController.setMaxAnswerId(answerId);
                }
            }
        });


        /**
         * add answer by click the button
         */
        addAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answerName = addAnswerEditText.getText().toString();
                if(answerName.length()>0) {
                    Answer answer = new Answer(answerName, AnswerDatabaseController.generateAnswerId());
                    boolean addAnswer = AnswerDatabaseController.add_Answer("Answers", answer);
                    //lock on answer
                    ArrayList<String> valueList = question.getAnswer_id();
                    valueList.add(answer.getId().toString());
                    QuestionDatabaseController.setQuestionanswer(question.getId().toString(), valueList);
                    //lock on answer
                    if (addAnswer){
                        Toast.makeText(getActivity(), "Add Failed", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Add Succeed", Toast.LENGTH_SHORT).show();
                    }
                    //revert logic
                    addAnswerEditText.setText("");
                }
            }
        });

        /**
         * delete answer by long click answer
         */
        answerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Delete event
                Answer answer = answerAdapter.getItem(position);
                String idString = answer.getId().toString();

                ArrayList<String> temp =  question.getAnswer_id();
                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).equals(idString)) {
                        temp.remove(i);
                    }
                }
                QuestionDatabaseController.setQuestionanswer(question.getId().toString(),temp);

                answerAdapter.notifyDataSetChanged();
                boolean deleteAnswer = AnswerDatabaseController.delete_Answer("Answers", answer);
                if (deleteAnswer){
                    Toast.makeText(getActivity(), "Delete Failed", Toast.LENGTH_SHORT).show();
                    answerAdapter.remove(answer);
                }
                else{
                    Toast.makeText(getActivity(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                }
                //revert logic
                return true;
            }
        });

        return v;
    }

}