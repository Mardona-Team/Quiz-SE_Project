package com.mardonaquiz.mardona.com.mardonaquiz.mardona.items;


public class PublishedQuizItem {
    public String title;
    public String type;
    public String quiz_id;
    public String group_id;

    public PublishedQuizItem(String title,String type,String quiz_id,String group_id) {
        this.title=title;
        this.type=type;
        this.quiz_id=quiz_id;
        this.group_id=group_id;

    }
}
