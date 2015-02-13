package com.example.heshitha.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class ReadStoryActivity extends Activity {

    TextView txtStoryTitle;
    TextView txtStoryDetails;
    TextView txtStoryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        Intent myIntent = getIntent();
        int storyID = myIntent.getIntExtra("storyID", 0);

        Story_Bean story = CommonDataHolder.getStory(storyID);

        txtStoryTitle = (TextView)findViewById(R.id.txtStoryTitle);
        txtStoryDetails = (TextView)findViewById(R.id.txtStoryDetails);
        txtStoryContent = (TextView)findViewById(R.id.txtStoryContent);

        Typeface timesNewRomen = Typeface.createFromAsset(getAssets(), "Times-New-Roman.ttf");
        Typeface timesNewRomenBold = Typeface.createFromAsset(getAssets(), "Times-New-Roman-Bold.ttf");
        Typeface helviticaNeue = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");

        txtStoryTitle.setTypeface(timesNewRomenBold);
        txtStoryDetails.setTypeface(helviticaNeue);
        txtStoryContent.setTypeface(timesNewRomen);

        String writtenDate = new SimpleDateFormat("MMM dd, yyyy").format(story.getCreatedDate());

        String authorName = story.getAuthor().getName();
        if(authorName == "null"){
            authorName = "Anonymous";
        }

        txtStoryTitle.setText(story.getTitle());
        txtStoryDetails.setText( "By " + authorName + " @ " + writtenDate);
        txtStoryContent.setText(story.getShortStory());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_story, menu);
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
