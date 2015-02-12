package com.mardonaquiz.mardona.com.mardonaquiz.mardona.items;

public class StudentAnswerItem {
    public String question;
    public String answer;
    public String rightAnswer;

    public StudentAnswerItem(String question,String answer, String rightAnswer){
        this.question=question;
        this.answer=answer;
        this.rightAnswer=rightAnswer;
    }
}
