package com.example.heshitha.story;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SignUpActivity extends Activity {

    static final int SELECT_PICTURE = 1;

    String selectedImagePath;

    TextView txtWelcomeText;
    ImageButton imgBtnAddNewPhoto;
    EditText txtUserName;
    EditText txtEmail;
    EditText txtPassword;
    Button btnSignUp;
    TextView txtSignIn;
    TextView txtCopyright;
    RoundImage roundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface RaleWay = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
        Typeface LatoLight = Typeface.createFromAsset(getAssets(), "Lato-Light.ttf");

        txtWelcomeText = (TextView)findViewById(R.id.txtWelcomeText);
        imgBtnAddNewPhoto = (ImageButton)findViewById(R.id.imgBtnAddNewPhoto);
        txtUserName = (EditText)findViewById(R.id.txtSignUpUsername);
        txtEmail = (EditText)findViewById(R.id.txtSignUpEmail);
        txtPassword = (EditText)findViewById(R.id.txtSignUpPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUpJoinNow);
        txtSignIn = (TextView)findViewById(R.id.txtSignIn);
        txtCopyright = (TextView)findViewById(R.id.txtCopyright);

        txtWelcomeText.setTypeface(LatoLight);
        txtUserName.setTypeface(LatoLight);
        txtEmail.setTypeface(LatoLight);
        txtPassword.setTypeface(LatoLight);
        btnSignUp.setTypeface(RaleWay);
        txtSignIn.setTypeface(LatoLight);
        txtCopyright.setTypeface(LatoLight);


        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        imgBtnAddNewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtUserName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                Log.d("username", userName);
                Log.d("password", password);
                Log.d("email", email);

                User_Bean userBean = new User_Bean();
                userBean.setName(userName);
                userBean.setEmail(email);
                userBean.setPassword(password);

                TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                String number = tm.getLine1Number();

                if(number != null){
                    userBean.setPhoneNumber(number);
                }
                else{
                    userBean.setPhoneNumber("0000");
                }



                new SignUpNewUser().execute(userBean);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                Uri selectedImageUri = data.getData();

                String newSelectedPath = "";

                if(Build.VERSION.SDK_INT < 11){
                    newSelectedPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, selectedImageUri);
                }else if(Build.VERSION.SDK_INT < 19){
                    newSelectedPath = RealPathUtil.getRealPathFromURI_API11to18(this, selectedImageUri);
                }else{
                    newSelectedPath = RealPathUtil.getRealPathFromURI_API19(this, selectedImageUri);
                }

                selectedImagePath = newSelectedPath;

                if(Build.VERSION.SDK_INT < 19){

                    Bitmap image = BitmapFactory.decodeFile(newSelectedPath);

                    roundImage = new RoundImage(image);

                    imgBtnAddNewPhoto.setImageDrawable(roundImage);
                }
                else{
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                        roundImage = new RoundImage(image);
                        imgBtnAddNewPhoto.setImageDrawable(roundImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String uploadFile(String sourceFileUri){

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if(!sourceFile.isFile()){
            Log.e("UploadFile", "Source file not exist : "+selectedImagePath);
            return "No Image";
        }
        else{

            try{
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://www.ezcim.com/story/SaveProfilePicture");
                conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", "file Name");

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + "fileName" + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable  = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0 , bufferSize);

                while (bytesRead > 0){
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0 , bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);



                int serverRespondsCode = conn.getResponseCode();
                String serverRespondsMessage = conn.getResponseMessage();

                InputStream in = conn.getInputStream();
                int ch;
                StringBuffer sb = new StringBuffer();
                while((ch = in.read()) != -1){
                    sb.append((char)ch);
                }

                String response = sb.toString();

                JSONObject jsonObject = new JSONObject(response);

                String fileNameJson = jsonObject.getString("Content");

                Log.i("uploadFile", "HTTP Response is : " + serverRespondsMessage + ": " + serverRespondsCode);

                fileInputStream.close();
                dos.flush();
                dos.close();

                return fileNameJson;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml.
        //
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SignUpNewUser extends AsyncTask<User_Bean, Void, User_Bean>{

        ProgressDialog pDialog;

        @Override
        protected User_Bean doInBackground(User_Bean... params) {


            try{

                String fileName = uploadFile(selectedImagePath);

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.ezcim.com/story/SaveProfile");
                JSONObject json = new JSONObject();
                User_Bean userBean = params[0];

                json.put("Email", userBean.getEmail());
                json.put("Name", userBean.getName());
                json.put("Password", userBean.getPassword());
                json.put("PhoneNumber", userBean.getPhoneNumber());
                json.put("ImageName", fileName);

                StringEntity stringEntity = new StringEntity(json.toString());
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(stringEntity);
                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity httpEntity = response.getEntity();
                String content = EntityUtils.toString(httpEntity);
                Log.d("Content", content);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Saving...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(User_Bean user_bean) {
            super.onPostExecute(user_bean);

            pDialog.dismiss();

            ShowSuccessMessage();

        }
    }

    public void ShowSuccessMessage() {

        try {

            final Dialog dialog = new Dialog(SignUpActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.success_message);


            TextView txtMessageContent = (TextView)dialog.findViewById(R.id.txtMessageContent);
            txtMessageContent.setText("Success!");


            TextView txtMessageText = (TextView)dialog.findViewById(R.id.txtMessageText);
            txtMessageText.setText("Your account created successfully. Please sign in using your credentials.");


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
                    Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
