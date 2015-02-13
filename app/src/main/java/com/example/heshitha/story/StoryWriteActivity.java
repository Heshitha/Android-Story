package com.example.heshitha.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class StoryWriteActivity extends Activity {

    TextView txtStoryTitle;
    TextView txtStoryContent;
    Spinner spnStoryCategory;
    ImageButton imgBtnSaveStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_write);
        txtStoryTitle = (TextView)findViewById(R.id.txtStoryTitle);
        txtStoryContent = (TextView)findViewById(R.id.txtStoryContent);
        spnStoryCategory = (Spinner)findViewById(R.id.spnStoryCategory);

        imgBtnSaveStory = (ImageButton)findViewById(R.id.imgBtnSaveStory);

        Typeface timesNewRomen = Typeface.createFromAsset(getAssets(), "Times-New-Roman.ttf");
        Typeface timesNewRomenBold = Typeface.createFromAsset(getAssets(), "Times-New-Roman-Bold.ttf");
        //Typeface latoLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");

        txtStoryTitle.setTypeface(timesNewRomenBold);
        txtStoryContent.setTypeface(timesNewRomen);


        imgBtnSaveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtStoryContent.getText().toString().isEmpty() || txtStoryTitle.getText().toString().isEmpty() ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoryWriteActivity.this);

                    builder.setTitle("Warning");
                    builder.setMessage("Please fill all the text fields.");
                    builder.setPositiveButton("OK",null);
                    AlertDialog theAlertDialog = builder.create();
                    theAlertDialog.show();
                }
                else {
                    new PublishStoryParseJson().execute();
                }
            }
        });
        fillCategoryListSpinner();
    }

    public class PublishStoryParseJson extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        @Override
        protected String doInBackground(String... params) {
            postData();

            return "";
        }

        @Override
        protected void onPostExecute(String strFromDoInBg){
            pDialog.dismiss();
            ShowSuccessMessage();
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(StoryWriteActivity.this);
            pDialog.setMessage("Saving...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }
    }

    public void postData(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.ezcim.com/story/savestory");
        JSONObject json = new JSONObject();

        try{
            Story_Category_Bean storyCatagory = (Story_Category_Bean)spnStoryCategory.getSelectedItem();

            json.put("Category.Id", storyCatagory.getId());
            json.put("Author.Id", 1);
            json.put("Title", txtStoryTitle.getText());
            json.put("ShortStory", txtStoryContent.getText());

            StringEntity stringEntity = new StringEntity(json.toString());

            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPost);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e("TAG", ex.getMessage());
        }
    }

    private void fillCategoryListSpinner(){
        try{
            if(CommonDataHolder.lstStoryCategories.size() > 0){
                ArrayAdapter<Story_Category_Bean> adp1 = new ArrayAdapter<Story_Category_Bean>(StoryWriteActivity.this, android.R.layout.simple_spinner_item, CommonDataHolder.lstStoryCategories);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnStoryCategory.setAdapter(adp1);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_write, menu);
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

    public void ShowSuccessMessage() {

        try {

            final Dialog dialog = new Dialog(StoryWriteActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.success_message);




            //dialog.setTitle(Html.fromHtml("<font color='#FFFFFF'>Phone Number Verification</font>"));

            //dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_header);
            dialog.setCanceledOnTouchOutside(true);

            //TextView txtWhat = (TextView) dialog.findViewById(R.id.txtWhat);
            //txtWhat.setTypeface(lato_regular);

            //Button btnEdit = (Button) dialog.findViewById(R.id.btnNo);
            //btnEdit.setTypeface(lato_regular);

            //btnEdit.setText("Edit");

            //Button btnOk = (Button) dialog.findViewById(R.id.btnYes);
            //btnOk.setTypeface(lato_regular);

            //btnOk.setText("Ok");

            //txtWhat.setText(" Is this your correct number ? \n\n "
                   // + edtPhoneNumber.getText().toString()
                   // + "\n\n"
                   // + "A Negomee message with an access code will be sent to your device");

            //btnOk.setOnClickListener(new OnClickListener() {
//
            //    @Override
            //    public void onClick(View v) {
            //        // TODO Auto-generated method stub
//
            //        if (!isNetworkConnected()) {
            //            Toast.makeText(getApplicationContext(),
            //                    "Internet is not available", Toast.LENGTH_LONG)
            //                    .show();
            //        }
//
            //        else {
            //            new Async_Phone_Device().execute();
            //        }
//
            //        dialog.dismiss();
            //    }
            //});
//
            //btnEdit.setOnClickListener(new OnClickListener() {
//
            //    @Override
            //    public void onClick(View v) {
            //        // TODO Auto-generated method stub
//
            //        dialog.dismiss();
            //    }
            //});

            Display display = getWindowManager().getDefaultDisplay();

            WindowManager.LayoutParams lp;

            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int) (display.getWidth() * 1);

            //
            dialog.getWindow().setAttributes(lp);

            dialog.show();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Intent i = new Intent(StoryWriteActivity.this, MainActivity.class);
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
