package com.codepath.gridimagesearch1.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by APPWISKA on 7/17/2015.
 */
public class ImageResult implements Serializable {
    private static final long serialVersionUID = -2893089570992474768L;
    public String fullUrl;
    public String thumbUrl;
    public String title;

    //new imageresult(...row item json ... )
    public ImageResult(JSONObject json){
        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.fullUrl = json.getString("title");

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    //Toke on array of json image and return arraylist of image result
    //ImageResult.fromarray([...,...])
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for(int i = 0; i < array.length(); i++){
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
