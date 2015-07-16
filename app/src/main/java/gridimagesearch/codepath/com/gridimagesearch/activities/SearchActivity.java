package gridimagesearch.codepath.com.gridimagesearch.activities;

//import android.app.Activity;
//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gridimagesearch.codepath.com.gridimagesearch.R;
//import gridimagesearch.codepath.com.gridimagesearch.adapters.ImageResultsAdapter;
//import gridimagesearch.codepath.com.gridimagesearch.adapters.ImageResultsAdapter;
import gridimagesearch.codepath.com.gridimagesearch.adapters.ImageResultsAdapter;
import gridimagesearch.codepath.com.gridimagesearch.models.ImageResult;


public class SearchActivity extends ActionBarActivity {
    private EditText ETQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        // create the data
        imageResults= new ArrayList<ImageResult>();
        // attach the data created
        aImageResults= new ImageResultsAdapter(this,imageResults);
        // connect the adapter to adapter View
        gvResults.setAdapter(aImageResults);

    }
    private void setupViews(){
        ETQuery = (EditText) findViewById(R.id.ETQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("url", result.fullUrl);
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

    // method of the pressed button , which open when the
    public void onImageSearch(View v) {
        String query = ETQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(this,"Search for "+query, Toast.LENGTH_SHORT).show();
        //https://ajax.googleapis.com/ajax/services/search/images
        //https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=Android
        String searchUrl ="https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+query +"&rsz=8";

        client.get(searchUrl, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
                Log.d("debug", response.toString());
                JSONArray ImageResultsJson = null;
                try {

                    ImageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();// clear the images
                    aImageResults.addAll(ImageResult.fromJSONArray(ImageResultsJson));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("debug", imageResults.toString());

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
