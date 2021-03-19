package com.example.project007;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {

    private String question;
    private Integer id;
    private Integer answer_id;


    Question(Integer id, String question, Integer answer_id) {
        this.question = question;
        if (id != null){
            this.id = id;
        }
        else{
            this.id = QuestionDatabaseController.generateQuestionId();
        }
        this.answer_id = answer_id;
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

    public Integer getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
    }
}
