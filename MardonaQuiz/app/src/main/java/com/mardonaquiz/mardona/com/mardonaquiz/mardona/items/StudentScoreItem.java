package com.mardonaquiz.mardona.com.mardonaquiz.mardona.items;

public class StudentScoreItem {
    public String name;
    public String grade;
    public String id;
    public boolean passing;

    public StudentScoreItem(String name, String grade,String id,boolean passing) {
        this.name = name;
        this.grade = grade;
        this.id=id;
        this.passing=passing;

    }
}