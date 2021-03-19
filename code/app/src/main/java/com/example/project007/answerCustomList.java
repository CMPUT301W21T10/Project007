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

public class answerCustomList extends ArrayAdapter<String> {

    private ArrayList<String> answers;
    private Context context;

    public answerCustomList(Context context, ArrayList<String> answers){
        super(context,0, answers);
        this.answers = answers;
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

        String answer = answers.get(position);
        TextView answerName = view.findViewById(R.id.question_text);

        answerName.setText(answer);

        return view;
    }
}
