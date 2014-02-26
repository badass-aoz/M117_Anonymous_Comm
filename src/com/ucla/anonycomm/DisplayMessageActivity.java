package com.ucla.anonycomm;

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

public class DisplayMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		// Create the image view
		ImageView imageView = new ImageView(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		String path = message;
		Bitmap image = BitmapFactory.decodeFile(path);
		imageView.setImageBitmap(image);

		// new HTTPConn().execute();

		setContentView(imageView, lp);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private class HTTPConn extends AsyncTask<Void, Void, Void> {
	    @Override
	    protected Void doInBackground(Void... arg) {
	    	HttpClient httpclient = new DefaultHttpClient();
	        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	        HttpPost httppost = new HttpPost("http://108.168.239.90:8080/session/send");
	        
	        try {
	        	// Get the image from the path
	        	EditText text = (EditText) findViewById(R.id.edit_message);
		        File file = new File(text.getText().toString());
	        	// Add your data
		        MultipartEntity entity = new MultipartEntity();
	        	entity.addPart("offset", new StringBody("0"));
	        	entity.addPart("count", new StringBody("1"));
	        	entity.addPart("wait", new StringBody("false"));
	        	entity.addPart("image", new FileBody(file, "image/bmp"));
	        	
    	        httppost.setEntity(entity);
    	        HttpResponse response = httpclient.execute(httppost);
	        } catch (IOException e){
	        	e.printStackTrace();
	        }
	        return null;
	    }
	    // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Void res) {}
	}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
