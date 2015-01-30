package com.mardonaquiz.mardona;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ViewGroup extends ActionBarActivity {

    private final String KEY_TITLE = "title";
    private final String KEY_SUBJECT = "subject";
    private final String KEY_YEAR = "year";
    private final String KEY_ID = "id";
    private final String KEY_Desc = "description";

    private String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            group_id    =  extras.getString(KEY_ID);

            ((TextView) findViewById(R.id.Gsubject)).setText(extras.getString(KEY_SUBJECT));
            ((TextView) findViewById(R.id.Gyear)).setText( extras.getString(KEY_YEAR));
            ((TextView) findViewById(R.id.Gdesc)).setText(extras.getString(KEY_Desc));
            ((TextView) findViewById(R.id.Gtitle)).setText(extras.getString(KEY_TITLE));


        }





    }
    public void openAnswersQuiz(View view) {
        //TODO replace this function with a function calling the group creation activity
        Intent intent = new Intent(this, AnswerQuiz.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_group, menu);
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
}
