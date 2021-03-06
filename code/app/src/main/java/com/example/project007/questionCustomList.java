package com.example.project007;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class customize the context of questions in the listview of question activity
 */
public class questionCustomList extends ArrayAdapter<Question> {

    private final ArrayList<Question> questions;
    private final Context context;

    public questionCustomList(Context context, ArrayList<Question> questions){
        super(context,0, questions);
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_question, parent, false);
        }

        Question question = questions.get(position);
        TextView questionName = view.findViewById(R.id.question_text);

        questionName.setText(question.getQuestion());

        return view;
    }
}
