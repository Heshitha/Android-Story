package com.example.heshitha.story;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;


public class SignInActivity extends Activity {

    private static final int PICK_IMAGE = 0;
    public static final String PREFS_NAME = "com.example.heshitha.story.LOGINDETAILS";
    TextView txtWelcomeText;
    EditText txtLoginUserName;
    EditText txtLoginPassWord;
    TextView txtTermsAndPrivacy;
    TextView txtCopyright;
    TextView txtForgotYourPassword;
    TextView txtCreateNewAccount;


    Button btnLoginJoinNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Typeface AovelSans = Typeface.createFromAsset(getAssets(), "AovelSans.ttf");
        Typeface RaleWay = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
        Typeface LatoLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");

        txtWelcomeText = (TextView)findViewById(R.id.txtWelcomeText);
        txtWelcomeText.setTypeface(LatoLight);
        txtTermsAndPrivacy = (TextView)findViewById(R.id.txtTermsAndPrivacy);
        txtTermsAndPrivacy.setTypeface(LatoLight);
        txtCopyright = (TextView)findViewById(R.id.txtCopyright);
        txtCopyright.setTypeface(LatoLight);
        txtForgotYourPassword = (TextView)findViewById(R.id.txtForgotYourPassword);
        txtForgotYourPassword.setTypeface(LatoLight);
        txtCreateNewAccount = (TextView)findViewById(R.id.txtCreateNewAccount);
        txtCreateNewAccount.setTypeface(LatoLight);


        txtLoginUserName = (EditText)findViewById(R.id.txtLoginEmail);
        txtLoginUserName.setTypeface(LatoLight);
        txtLoginPassWord = (EditText)findViewById(R.id.txtLoginPassword);
        txtLoginPassWord.setTypeface(LatoLight);


        btnLoginJoinNow = (Button)findViewById(R.id.btnLoginJoinNow);
        btnLoginJoinNow.setTypeface(RaleWay);

        btnLoginJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Bean userBean = new User_Bean();
                userBean.setEmail(txtLoginUserName.getText().toString());
                userBean.setPassword(txtLoginPassWord.getText().toString());

                new SignInUser(userBean).execute();
                //Intent i = new Intent(SignInActivity.this, StoryWriteActivity.class);
                //startActivity(i);
            }
        });

        txtCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        txtForgotYourPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, RetrievePasswordActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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

    public class SignInUser extends AsyncTask<User_Bean, Void, User_Bean>{

        private User_Bean userBean;
        ProgressDialog pDialog;
        String loggingResponse;
        Bitmap userProfileImage;

        public SignInUser(User_Bean userBean) {
            this.userBean = userBean;
        }

        @Override
        protected User_Bean doInBackground(User_Bean... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.ezcim.com/story/Login");
                JSONObject json = new JSONObject();

                json.put("PhoneOrEmail", userBean.getEmail());
                json.put("Password", userBean.getPassword());

                StringEntity stringEntity = new StringEntity(json.toString());
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(stringEntity);
                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity httpEntity = response.getEntity();

                loggingResponse = EntityUtils.toString(httpEntity);

                String content = loggingResponse;

                JSONObject responseObject = new JSONObject(content);

                int responseCode = responseObject.getInt("Code");

                if(responseCode == 1){
                    JSONObject userJsonObject = responseObject.getJSONObject("Content");

                    String ImageUrl = "http://www.assets.ezcim.com/story/userpics/profilepic/" + userJsonObject.getString("ImageName");
                    URL url = new URL(ImageUrl);
                    userProfileImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Sign In ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(User_Bean user_bean) {
            super.onPostExecute(user_bean);

            pDialog.dismiss();


            try{
                String content = loggingResponse;

                JSONObject responseObject = new JSONObject(content);

                int responseCode = responseObject.getInt("Code");

                if(responseCode == 1){
                    //successfully logged in

                    JSONObject userJsonObject = responseObject.getJSONObject("Content");

                    User_Bean usrBean = new User_Bean();
                    usrBean.setId(userJsonObject.getInt("Id"));
                    usrBean.setName(userJsonObject.getString("Name"));
                    usrBean.setEmail(userJsonObject.getString("Email"));
                    usrBean.setPhoneNumber(userJsonObject.getString("PhoneNumber"));
                    usrBean.setPassword(userJsonObject.getString("Password"));
                    usrBean.setImageName(userJsonObject.getString("ImageName"));
                    usrBean.setStatus(userJsonObject.getInt("Status"));
                    usrBean.setImage(this.userProfileImage);

                    SharedPreferences userDetails = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = userDetails.edit();
                    editor.putInt("Id", usrBean.getId());
                    editor.putString("Name", usrBean.getName());
                    editor.putString("Email", usrBean.getEmail());
                    editor.putString("PhoneNumber", usrBean.getPhoneNumber());
                    editor.putString("Password", usrBean.getPassword());
                    editor.putString("ImageName", usrBean.getImageName());
                    editor.putInt("Status", usrBean.getStatus());

                    editor.commit();

                    CommonDataHolder.LoggedUser = usrBean;

                    Intent i = new Intent(SignInActivity.this, WelcomeBackActivity.class);
                    i.putExtra("WelcomeBackText", false);
                    startActivity(i);
                    finish();

                }else if(responseCode == 2){
                    //user does not exist

                    ShowErrorMessage();


                }else if(responseCode == 3){
                    //password incorrect

                    ShowErrorMessage();
                }

                Log.d("Content", content);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void ShowErrorMessage() {

            try {

                final Dialog dialog = new Dialog(SignInActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.success_message);

                TextView txtMessageContent = (TextView)dialog.findViewById(R.id.txtMessageContent);
                txtMessageContent.setText("Ooops!");


                TextView txtMessageText = (TextView)dialog.findViewById(R.id.txtMessageText);
                txtMessageText.setText("Check email and password and try again");
                txtMessageText.setTextColor(Color.parseColor("#ff3300"));

                ImageView imgSuccessfullImage = (ImageView)dialog.findViewById(R.id.imgMessageGreenTick);
                imgSuccessfullImage.setImageResource(R.drawable.messageboxredcross);

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
                        txtLoginUserName.setText("");
                        txtLoginPassWord.setText("");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
