package com.example.project007;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    private String question;
    private ArrayList<String> answer;

    Question(String question, ArrayList answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList answer) {
        this.answer = answer;
    }
}
