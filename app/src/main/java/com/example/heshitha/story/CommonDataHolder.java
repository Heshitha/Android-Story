package com.example.heshitha.story;

import android.app.ProgressDialog;

import java.util.List;

/**
 * Created by Heshitha on 2/5/2015.
 */
public class CommonDataHolder {

    public static User_Bean LoggedUser;

    public static List<Story_Category_Bean> lstStoryCategories;

    public static List<Story_Bean> lstStories;

    public static Story_Bean getStory(int storyID){

        Story_Bean story = new Story_Bean();

        if(lstStories != null) {
            for (int i = 0; i < lstStories.size(); i++) {
                if (lstStories.get(i).getStoryID() == storyID) {
                    story = lstStories.get(i);
                    break;
                }
            }
        }

        return story;
    }

}
