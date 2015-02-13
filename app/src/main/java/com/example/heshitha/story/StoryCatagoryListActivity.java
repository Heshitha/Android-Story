package com.example.heshitha.story;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class StoryCatagoryListActivity extends Activity {

    //TableLayout tableLayoutCategories;
    TextView txtCategoryTitle;
    ListView lstViewStoryCategories;

    Typeface centurygothic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_catagory_list);

        lstViewStoryCategories = (ListView)findViewById(R.id.lstViewStoryCategories);

        txtCategoryTitle = (TextView)findViewById(R.id.txtCategoryTitle);
        centurygothic = Typeface.createFromAsset(getAssets(), "Century-Gothic.ttf");

        CustomListViewAdapter adap = new CustomListViewAdapter(StoryCatagoryListActivity.this, R.layout.story_category_item, CommonDataHolder.lstStoryCategories);
        lstViewStoryCategories.setAdapter(adap);

        lstViewStoryCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String storyCategoryID = (String) ((TextView)view.findViewById(R.id.txtCategoryId)).getText();

                Intent intent = new Intent(StoryCatagoryListActivity.this, StoryListActivity.class);
                intent.putExtra("storyCategoryId", storyCategoryID);
                startActivity(intent);

            }
        });
    }

    public class CustomListViewAdapter extends ArrayAdapter<Story_Category_Bean> {

        Context context;

        public CustomListViewAdapter(Context context, int resourceId,
                                     List<Story_Category_Bean> items) {
            super(context, resourceId, items);
            this.context = context;
        }

        /*private view holder class*/
        private class ViewHolder {
            TextView txtCategoryName;
            TextView txtCategoryId;
            TextView txtCategoryStoryCount;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Story_Category_Bean rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.story_category_item, null);
                holder = new ViewHolder();
                holder.txtCategoryName = (TextView) convertView.findViewById(R.id.txtCategoryName);
                holder.txtCategoryId = (TextView)convertView.findViewById(R.id.txtCategoryId);
                holder.txtCategoryStoryCount = (TextView)convertView.findViewById(R.id.txtCategoryStoryCount);
                holder.txtCategoryName.setTypeface(centurygothic);
                holder.txtCategoryStoryCount.setTypeface(centurygothic);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.txtCategoryName.setText(rowItem.getName());
            holder.txtCategoryId.setText(String.valueOf(rowItem.getId()));
            holder.txtCategoryStoryCount.setText(rowItem.getCount() + " Stories");

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_catagory_list, menu);
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
