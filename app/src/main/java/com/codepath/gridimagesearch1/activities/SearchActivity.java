package com.codepath.gridimagesearch1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
//import android.widget.ListAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.ArrayAdapter;
//import android.widget.AdapterView.OnItemClickListener;

import com.codepath.gridimagesearch1.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch1.models.ImageResult;
import com.codepath.gridimagesearch1.R;
import com.codepath.gridimagesearch1.adapters.ImageResultsAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;


public class SearchActivity extends Activity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //creates the data source
        imageResults = new ArrayList<ImageResult>();
        //attaches the data source to on adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //link the adapter to the adapterview (gridview)
      //  gvResults.setAdapter((ListAdapter) aImageResults);
        gvResults.setAdapter(aImageResults);
    }
    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResult);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch the image display activity
                // Create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                // Get the image result to display
                ImageResult result = imageResults.get(position);
                // Pass image result into the intent
               i.putExtra("result", result);//need to either be serializable or parcelable
              //  i.putExtra("url", result.fullUrl); //need to either be serializable or parcelable
                // Launch the new activity
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
    //Fired whenever the button is pressed (android:onclick property)
    public void onImageSearch(View v){
        String query = etQuery.getText().toString();
     //   Toast.makeText(this, "Search for:" + query, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Search for:" + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        //https://ajax.goopleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
        String searchUrl = "https://ajax.goopleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              // super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); //clear the existing images from the array (in cases where its a new search)
                   // imageResults.addAll();
                   aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
                  //  aImageResults.ad

                    //when you make the adapter, it does notify underlying data
                } catch (JSONException e){
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }

        });
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
