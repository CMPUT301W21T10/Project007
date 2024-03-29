package com.example.project007;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is Question class
 * This class gives a frame of a single question
 */
public class Question implements Serializable {

    private String question;
    private Integer id;
    private ArrayList<String> answer_id = new ArrayList<>();


    Question(){ }

    Question(Integer id, String question, ArrayList<String> answer_id) {
        if(id == null){
            this.id = id;
        }else{
            this.id = QuestionDatabaseController.generateQuestionId();
        }
        this.question = question;
        if (answer_id != null){
            this.answer_id = answer_id;
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<String> getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(ArrayList<String> answer_id) {
        this.answer_id = answer_id;
    }
}
