package com.example.project007;

import java.io.Serializable;

public class Answer implements Serializable {

    private String answer;
    private Integer id;

    public Answer(String answer, Integer id) {
        this.answer = answer;
        if (id != null){
            this.id = id;
        }
        else{
            this.id = AnswerDatabaseController.generateAnswerId();
        }
    }
//private Integer question_id;



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


}
