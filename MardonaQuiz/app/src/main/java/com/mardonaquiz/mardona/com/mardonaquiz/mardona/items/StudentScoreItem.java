package com.mardonaquiz.mardona.com.mardonaquiz.mardona.items;

public class StudentScoreItem {
    public String name;
    public String grade;
    public String user_id;
    public String quiz_id;
    public String group_id;
    public boolean passing;

    public StudentScoreItem(String name, String grade,String user_id,String quiz_id,String group_id, boolean passing) {
        this.name = name;
        this.grade = grade;
        this.user_id=user_id;
        this.passing=passing;
        this.quiz_id=quiz_id;
        this.group_id=group_id;

    }
}