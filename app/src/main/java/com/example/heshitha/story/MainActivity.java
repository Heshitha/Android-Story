package com.example.heshitha.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public static final String PREFS_NAME = "com.example.heshitha.story.LOGINDETAILS";
    TextView txtReadStory;
    TextView txtWriteStory;
    ImageButton imgBtnWriteStory;
    ImageButton imgBtnReadStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBtnReadStory = (ImageButton)findViewById(R.id.imgButtonTapToRead);
        imgBtnWriteStory = (ImageButton)findViewById(R.id.imgButtonTapToWrite);
        txtReadStory = (TextView)findViewById(R.id.txtReadStory);
        txtWriteStory = (TextView)findViewById(R.id.txtWriteStory);

        Typeface raavi = Typeface.createFromAsset(getAssets(), "raavi.ttf");

        txtReadStory.setTypeface(raavi);
        txtWriteStory.setTypeface(raavi);

        imgBtnReadStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StoryCatagoryListActivity.class);
                startActivity(i);
            }
        });

        imgBtnWriteStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonDataHolder.LoggedUser == null) {
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(MainActivity.this, WelcomeBackActivity.class);
                    i.putExtra("WelcomeBackText", true);
                    startActivity(i);
                }
            }
        });

        new FillStoryCategoryList().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class FillStoryCategoryList extends AsyncTask<String, String, String>{

        String jsonUrl = "http://www.ezcim.com/story/getcategories";

        JSONArray dataJsonArr = null;

        List<Story_Category_Bean> categoryList = new ArrayList<Story_Category_Bean>();

        ProgressDialog pDialog;

        @Override
        protected String doInBackground(String... params) {
            try{
                JsonParser jParser = new JsonParser();

                JSONObject json = jParser.getJSONFromUrl(jsonUrl);

                dataJsonArr = json.getJSONArray("Content");

                categoryList = new ArrayList<Story_Category_Bean>();

                for(int i = 0; i < dataJsonArr.length(); i++){
                    JSONObject c = dataJsonArr.getJSONObject(i);

                    int id = c.getInt("Id");
                    String name = c.getString("Name");
                    int count = c.getInt("Count");

                    categoryList.add(new Story_Category_Bean(id, name, count));
                }

                SharedPreferences userDetails = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int userID = userDetails.getInt("Id", 0);

                if(userID != 0){
                    User_Bean usrBean = new User_Bean();
                    usrBean.setName(userDetails.getString("Name", ""));
                    usrBean.setEmail(userDetails.getString("Email", ""));
                    usrBean.setPhoneNumber(userDetails.getString("PhoneNumber", ""));
                    usrBean.setPassword(userDetails.getString("Password", ""));
                    usrBean.setImageName(userDetails.getString("ImageName", ""));
                    usrBean.setStatus(userDetails.getInt("Status", 0));

                    String ImageUrl = "http://www.assets.ezcim.com/story/userpics/profilepic/" + usrBean.getImageName();
                    URL url = new URL(ImageUrl);
                    Bitmap userProfileImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    usrBean.setImage(userProfileImage);

                    CommonDataHolder.LoggedUser = usrBean;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            try {
                Thread.sleep(1000);

                pDialog.dismiss();

                if(categoryList.size() > 0){
                    CommonDataHolder.lstStoryCategories = categoryList;

                }
                else{
                    CommonDataHolder.lstStoryCategories = new ArrayList<Story_Category_Bean>();

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    dialogBuilder.setTitle("Warning!");
                    dialogBuilder.setMessage("Oops! Something went wrong. Please check your data connection.");
                    dialogBuilder.setPositiveButton("OK", null);
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
