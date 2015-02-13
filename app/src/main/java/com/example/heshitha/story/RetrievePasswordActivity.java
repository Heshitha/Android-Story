package com.example.heshitha.story;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RetrievePasswordActivity extends Activity {

    TextView txtWelcomeText;
    EditText txtLoginEmail;
    Button btnRetrievePassword;
    TextView txtTermsAndPrivacy;
    TextView txtCopyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        Typeface RaleWay = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
        Typeface LatoLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");

        txtWelcomeText = (TextView)findViewById(R.id.txtWelcomeText);
        txtLoginEmail = (EditText)findViewById(R.id.txtLoginEmail);
        btnRetrievePassword = (Button)findViewById(R.id.btnRetrievePassword);
        txtTermsAndPrivacy = (TextView)findViewById(R.id.txtTermsAndPrivacy);
        txtCopyright = (TextView)findViewById(R.id.txtCopyright);

        txtWelcomeText.setTypeface(LatoLight);
        txtLoginEmail.setTypeface(LatoLight);
        btnRetrievePassword.setTypeface(RaleWay);
        txtTermsAndPrivacy.setTypeface(LatoLight);
        txtCopyright.setTypeface(LatoLight);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_retrieve_password, menu);
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
