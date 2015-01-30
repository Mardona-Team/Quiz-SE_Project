package com.mardonaquiz.mardona;

import java.util.ArrayList;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class AnswerQuiz extends ActionBarActivity {

    public int NUM_Questions;
    public ArrayList<String> Questions = new ArrayList<String>();
    public   ArrayList<String> answer1 = new ArrayList<String>();
    public   ArrayList<String> answer2 = new ArrayList<String>();
    public   ArrayList<String> answer3 = new ArrayList<String>();
    public   ArrayList<String> answer4 = new ArrayList<String>();


    public   ArrayList<Integer> answers = new ArrayList<Integer>();



    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_quiz);


        setContentView(R.layout.activity_answer_quiz);


        Questions.add("question 1");
        Questions.add("question 2");
        Questions.add("question 3");
        Questions.add("question 4");


        answer1.add("answer 1 question 1");
        answer1.add("answer 1 question 2");
        answer1.add("answer 1 question 3");
        answer1.add("answer 1 question 4");

        answer2.add("answer 2 question 1");
        answer2.add("answer 2 question 2");
        answer2.add("answer 2 question 3");
        answer2.add("answer 2 question 4");

        answer3.add("answer 3 question 1");
        answer3.add("answer 3 question 2");
        answer3.add("answer 3 question 3");
        answer3.add("answer 3 question 4");

        answer4.add("answer 4 question 1");
        answer4.add("answer 4 question 2");
        answer4.add("answer 4 question 3");


        answer4.add("answer 4 question 4");

        NUM_Questions=Questions.size();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),Questions,answer1,answer2,answer3,answer4);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {

                if(i!= NUM_Questions+1 &&i!=0) {

                    CheckAnswers(i);
                    Log.e("i selected is", "" + i);

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CheckAnswers(final int question) {




        RadioGroup agroup = (RadioGroup) mSectionsPagerAdapter.getItem(question).getView().findViewById(R.id.rgroup);
        agroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int answerdIndex = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
                Log.d("question ", question-1+" answer " + answerdIndex);
                answers.add(question-1,answerdIndex);

            }
        });




    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /*NUM_Questions is the number of questions , fragment end is the last fragment which contain
        the summery and submit button
        */

        QuestionsFragments[] fragment=new QuestionsFragments[NUM_Questions];

        QuestionsEndFragments fragmentEnd= QuestionsEndFragments.newInstance(NUM_Questions+1);
        QuestionsStartFragments fragmentStart=QuestionsStartFragments.newInstance(0);

        public SectionsPagerAdapter(FragmentManager fm,ArrayList questions,ArrayList a1,ArrayList a2,ArrayList a3,ArrayList a4) {
            super(fm);

            for(int i=0;i< NUM_Questions;i++)
            {
                ArrayList<String> Question_Answers = new ArrayList<String>();
                Question_Answers.add(Questions.get(i).toString());
                Question_Answers.add(a1.get(i).toString());
                Question_Answers.add(a2.get(i).toString());
                Question_Answers.add(a3.get(i).toString());
                Question_Answers.add(a4.get(i).toString());

                fragment[i]=QuestionsFragments.newInstance(i,Question_Answers);

            }
        }

        @Override
        public int getCount() {
            return NUM_Questions +2;
        }


        @Override
        public Fragment getItem(int position) {

            if(position== NUM_Questions+1)
            {
                // last fragment = number of questions + 1 and this one is the first fragment which is intro
                return fragmentEnd;
            }
            else if(position==0)
            {
                //this is the first fragment whic is intro
                return fragmentStart;
            }
            else{
                // i return position-1 to compensate that my first page is taken by onther fragment
                return fragment[position-1];
            }

        }

    }
 /*
   Extension of FragmentStatePagerAdapter which intelligently caches
   all active fragments and manages the fragment lifecycles.
   Usage involves extending from SmartFragmentStatePagerAdapter as you would any other PagerAdapter.
*/


    /**
     this is the fragment we use
     */
    public static class QuestionsFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_Question = "Quest";
        private static final String ARG_A1 = "a1";
        private static final String ARG_A2 = "a2";
        private static final String ARG_A3 = "a3";
        private static final String ARG_A4 = "a4";

        public static QuestionsFragments newInstance(int sectionNumber,ArrayList ques_answers) {
            QuestionsFragments fragment = new QuestionsFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_Question, ques_answers.get(0).toString());
            args.putString(ARG_A1, ques_answers.get(1).toString());
            args.putString(ARG_A2, ques_answers.get(2).toString());
            args.putString(ARG_A3, ques_answers.get(3).toString());
            args.putString(ARG_A4, ques_answers.get(4).toString());

            fragment.setArguments(args);


            return fragment;
        }



        public QuestionsFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz, container, false);
            int secn=getArguments().getInt(ARG_SECTION_NUMBER);

            // Geting variables from arguments
            String ques=getArguments().getString(ARG_Question);

            String answer1=getArguments().getString(ARG_A1);
            String answer2=getArguments().getString(ARG_A2);
            String answer3=getArguments().getString(ARG_A3);
            String answer4=getArguments().getString(ARG_A4);



            //assigning views to variables to be changed
            TextView question=(TextView) rootView.findViewById(R.id.Question);
            RadioButton a1=(RadioButton) rootView.findViewById(R.id.r1);
            RadioButton a2=(RadioButton) rootView.findViewById(R.id.r2);
            RadioButton a3=(RadioButton) rootView.findViewById(R.id.r3);
            RadioButton a4=(RadioButton) rootView.findViewById(R.id.r4);


            //assigning views to variables from arguments
            question.setText(ques);
            a1.setText(answer1);
            a2.setText(answer2);
            a3.setText(answer3);
            a4.setText(answer4);


            return rootView;
        }
    }



    /**
     this is the Last Fragment to view stats about the quiz
     */
    public static class QuestionsEndFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static QuestionsEndFragments newInstance(int sectionNumber) {
            QuestionsEndFragments fragment = new QuestionsEndFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);


            return fragment;
        }



        public QuestionsEndFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz_last, container, false);


            return rootView;
        }
    }



    /**
     this is the Last Fragment to view stats about the quiz
     */
    public static class QuestionsStartFragments extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static QuestionsStartFragments newInstance(int sectionNumber) {
            QuestionsStartFragments fragment = new QuestionsStartFragments();

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);


            return fragment;
        }



        public QuestionsStartFragments() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer_quiz_first, container, false);


            return rootView;
        }
    }

}
