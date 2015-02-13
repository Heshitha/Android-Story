package com.example.heshitha.story;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class WelcomeBackActivity extends Activity {

    TextView txtWelcomeText;
    TextView txtUserName;
    TextView txtEmail;
    TextView txtCopyright;
    Button btnOk;
    Button btnSignOut;
    ImageView imgProfileImage;
    public static final String PREFS_NAME = "com.example.heshitha.story.LOGINDETAILS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);

        Typeface RaleWay = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
        Typeface LatoLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");

        txtWelcomeText = (TextView)findViewById(R.id.txtWelcomeText);
        txtUserName = (TextView)findViewById(R.id.txtSignUpUsername);
        txtEmail = (TextView)findViewById(R.id.txtSignUpEmail);
        btnOk = (Button)findViewById(R.id.btnSignUpJoinNow);
        btnSignOut = (Button)findViewById(R.id.btnSignOut);
        imgProfileImage = (ImageView)findViewById(R.id.imgBtnAddNewPhoto);
        txtCopyright = (TextView)findViewById(R.id.txtCopyright);

        txtWelcomeText.setTypeface(LatoLight);
        txtUserName.setTypeface(LatoLight);
        txtEmail.setTypeface(LatoLight);
        btnOk.setTypeface(RaleWay);
        btnSignOut.setTypeface(RaleWay);
        txtCopyright.setTypeface(LatoLight);

        Intent myIntent = getIntent();
        boolean WelcomeBackText = myIntent.getBooleanExtra("WelcomeBackText", true);

        User_Bean userBean = CommonDataHolder.LoggedUser;

        if(WelcomeBackText){
            txtWelcomeText.setText("Welcome back to story");
        }
        else{
            txtWelcomeText.setText("Welcome to story");
        }

        txtUserName.setText(userBean.getName());
        txtEmail.setText(userBean.getEmail());

        Bitmap image = userBean.getImage();
        RoundImage roundImage = new RoundImage(image);
        imgProfileImage.setImageDrawable(roundImage);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeBackActivity.this, StoryWriteActivity.class);
                startActivity(i);

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userDetails = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = userDetails.edit();
                editor.clear();
                editor.commit();

                CommonDataHolder.LoggedUser = null;

                Intent i = new Intent(WelcomeBackActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome_back, menu);
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
