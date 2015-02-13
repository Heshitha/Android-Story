package com.example.heshitha.story;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StoryListActivity extends Activity {

    GridView gridView;
    gridViewAdapter gridViewAdapterObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        Intent intent = getIntent();
        int categoryID = Integer.parseInt(intent.getStringExtra("storyCategoryId"));

        gridView = (GridView)findViewById(R.id.gridView);
        new LoadStoryList(categoryID).execute();
    }

    public class LoadStoryList extends AsyncTask<Story_Bean, Void, Story_Bean>{

        String jsonUrl = "http://www.ezcim.com/story/getStories?categoryid=";

        JSONArray dataJsonMainArr = null;

        List<Story_Bean> storyList = new ArrayList<Story_Bean>();

        ProgressDialog pDialog;

        int categoryID = 0;
        public LoadStoryList(int categoryID){
            this.categoryID = categoryID;
            jsonUrl = "http://www.ezcim.com/story/getStories?categoryid="+categoryID;
        }

        @Override
        protected Story_Bean doInBackground(Story_Bean... params) {
            try{

                JsonParser jParser = new JsonParser();

                JSONObject json = jParser.getJSONFromUrl(jsonUrl);

                dataJsonMainArr = json.getJSONArray("Content");

                storyList = new ArrayList<Story_Bean>();

                for(int i = 0; i < dataJsonMainArr.length(); i++){
                    try {
                        JSONObject storyObject = dataJsonMainArr.getJSONObject(i);

                        JSONObject categoryObject = storyObject.getJSONObject("Category");
                        Story_Category_Bean storyCategoryBean = new Story_Category_Bean();
                        storyCategoryBean.setId(categoryObject.getInt("Id"));
                        storyCategoryBean.setName(categoryObject.getString("Name"));
                        storyCategoryBean.setCount(categoryObject.getInt("Count"));

                        JSONObject authorObject = storyObject.getJSONObject("Author");
                        Author_Bean authorBean = new Author_Bean();
                        authorBean.setId(authorObject.getInt("Id"));
                        authorBean.setName(authorObject.getString("Name"));

                        Story_Bean storyBean = new Story_Bean();
                        storyBean.setStoryID(storyObject.getInt("Id"));
                        storyBean.setCategory(storyCategoryBean);
                        storyBean.setAuthor(authorBean);
                        storyBean.setTitle(storyObject.getString("Title"));
                        storyBean.setShortStory(storyObject.getString("ShortStory"));
                        storyBean.setRate(storyObject.getDouble("Rate"));

                        String ackwardDate = storyObject.getString("CreatedDate");
                        Calendar calender = Calendar.getInstance();
                        String ackwardRipOff = ackwardDate.replace("/Date(", "").replace(")/", "");
                        Long timeInMillis = Long.valueOf(ackwardRipOff);
                        calender.setTimeInMillis(timeInMillis);

                        Date createdDate = calender.getTime();

                        ackwardDate = storyObject.getString("ModifiedDate");
                        calender = Calendar.getInstance();
                        ackwardRipOff = ackwardDate.replace("/Date(", "").replace(")/", "");
                        timeInMillis = Long.valueOf(ackwardRipOff);
                        calender.setTimeInMillis(timeInMillis);

                        Date modifiedDate = calender.getTime();

                        storyBean.setCreatedDate(createdDate);
                        storyBean.setModifiedDate(modifiedDate);

                        URL url = new URL("http://www.assets.ezcim.com/story/storypics/storypic/story.jpg");
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        storyBean.setImage(bmp);

                        storyList.add(storyBean);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(StoryListActivity.this);
            pDialog.setMessage("Loading! Please Wait.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Story_Bean story_bean) {
            super.onPostExecute(story_bean);

            CommonDataHolder.lstStories = storyList;

            gridViewAdapterObject = new gridViewAdapter(StoryListActivity.this, storyList);
            gridView.setAdapter(gridViewAdapterObject);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int storyID = Integer.parseInt(((TextView)view.findViewById(R.id.txtStoryID)).getText().toString());
                    String shortStory = ((TextView)view.findViewById(R.id.txtFullStory)).getText().toString();
                    String author = ((TextView)view.findViewById(R.id.txtStoryAuthor)).getText().toString();
                    String title = ((TextView)view.findViewById(R.id.txtStoryName)).getText().toString();

                    Intent i = new Intent(StoryListActivity.this, ReadStoryActivity.class);
                    i.putExtra("storyID", storyID);
                    startActivity(i);

                }
            });

            pDialog.dismiss();
        }
    }

    public class gridViewAdapter extends ArrayAdapter{

        private Context mContext;
        private List<Story_Bean> storyList;

        public gridViewAdapter(Context context, List<Story_Bean> storyList) {
            super(context, R.layout.activity_story_list, storyList);
            this.storyList = storyList;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = convertView;
            RecordHolder holder = null;

            if(row == null){
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                row = inflater.inflate(R.layout.story_item, parent, false);

                holder = new RecordHolder();

                holder.storyImage = (ImageView) row.findViewById(R.id.imgStoryImage);
                holder.txtStoryName = (TextView) row.findViewById(R.id.txtStoryName);
                holder.txtStoryAuthor = (TextView) row.findViewById(R.id.txtStoryAuthor);
                holder.txtStorySummery = (TextView) row.findViewById(R.id.txtStorySummery);
                holder.txtPublishedDate = (TextView) row.findViewById(R.id.txtPublishedDate);
                holder.txtNoofReviews = (TextView) row.findViewById(R.id.txtNoofReviews);
                holder.txtStoryID = (TextView)row.findViewById(R.id.txtStoryID);
                holder.txtFullStory = (TextView)row.findViewById(R.id.txtFullStory);
                row.setTag(holder);
            }else {
                holder = (RecordHolder)row.getTag();
            }

            Story_Bean item = storyList.get(position);

            holder.txtStoryName.setText(item.getTitle());

            String authorName = item.getAuthor().getName();

            if(authorName != "null"){
                holder.txtStoryAuthor.setText("By " + item.getAuthor().getName());
            }
            else{
                holder.txtStoryAuthor.setText("By Anonymous");
            }

            String storyContent = item.getShortStory();

            if(storyContent.length() > 100){
                storyContent = storyContent.substring(0, 100) + "... ";
            }

            holder.txtStorySummery.setText(storyContent);
            holder.txtPublishedDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(item.getCreatedDate()));
            holder.txtNoofReviews.setText(item.getRate() + " reviews");
            holder.storyImage.setImageBitmap(item.getImage());
            holder.txtStoryID.setText(String.valueOf(item.getStoryID()));
            holder.txtFullStory.setText(item.getShortStory());

            return row;
        }
    }

    public class RecordHolder{
        ImageView storyImage;
        TextView txtStoryName;
        TextView txtStoryAuthor;
        TextView txtStorySummery;
        TextView txtPublishedDate;
        TextView txtNoofReviews;
        TextView txtStoryID;
        TextView txtFullStory;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_list, menu);
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
