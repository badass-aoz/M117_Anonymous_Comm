package com.ucla.anonycomm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
		
		// Create the text view
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);
		
		new HTTPConn().execute();
		
		setContentView(textView);
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
	    	    HttpPost httppost = new HttpPost("http://108.168.239.90:8080/session/send");
	    		
	    	    try {
	    	        // Add your data
	    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    	        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
	    	        nameValuePairs.add(new BasicNameValuePair("count", "1"));
	    	        nameValuePairs.add(new BasicNameValuePair("wait", "false"));
	    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	    	        // Execute HTTP Post Request
	    	        HttpResponse response = httpclient.execute(httppost);
	    	    } catch (Exception e) {
	    	    }
	    	    return null;
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(Void res) {
	       }
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
