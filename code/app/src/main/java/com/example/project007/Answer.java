package com.example.project007;

import java.io.Serializable;

public class Answer implements Serializable {

    private String answer;
    private Integer id;
    private Integer question_id;

    Answer(Integer id, String answer, Integer question_id) {
        this.answer = answer;
        if (id != null){
            this.id = id;
        }
        else{
            this.id = AnswerDatabaseController.generateAnswerId();
        }
        this.question_id = question_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }
}
