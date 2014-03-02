package com.ucla.anonycomm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
		
		TextView t_msg = (TextView) findViewById(R.id.sent_message);
		t_msg.setTextSize(20);
		t_msg.setText(message);

		// Create the image view
		ImageView imageView = new ImageView(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		String path = message;
		Bitmap image = BitmapFactory.decodeFile(path);
		imageView.setImageBitmap(image);

		// possibly needs changing
		//		setContentView(imageView, lp);

		new HTTPConn().execute();
	}
	
	// update m_ip and m_port inside onResume
	// as they need to be updated every time settings_activity is called
	@Override
	protected void onResume() {
		
		// without super.onResume, it will crash
		super.onResume();
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		m_ip = settings.getString("setIP", "108.168.239.90");
		m_port = settings.getString("setPort", "8080");
		m_encry = settings.getBoolean("setEncry", false);
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

	 private class HTTPConn extends AsyncTask<Void, Void, HttpResponse> {
	        @Override
	        protected HttpResponse doInBackground(Void... arg) {
	              
	        	HttpClient httpclient = new DefaultHttpClient();
	    	    HttpPost httppost = new HttpPost("http://"+m_ip+":"+m_port+"/session/send");
	    		
	    	    try {
	    	        // Add your data
	    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	    	        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
	    	        nameValuePairs.add(new BasicNameValuePair("count", "1"));
	    	        nameValuePairs.add(new BasicNameValuePair("wait", "false"));
	    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	    	        // Execute HTTP Post Request
	    	        return httpclient.execute(httppost);
	    	    } catch (Exception e) {
	    	    	
	    	    }
	    	    return null;
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(HttpResponse response) {
    	        if (response!=null && response.getStatusLine().getStatusCode() == 200) {
    	        	Toast.makeText(DisplayMessageActivity.this,
        				"Sent Successfully", Toast.LENGTH_LONG).show();
    	        } else {
    	        	Toast.makeText(DisplayMessageActivity.this,
        				"Failed to send", Toast.LENGTH_LONG).show();
    	        }
	       }

// 		private class HTTPConn extends AsyncTask<Void, Void, Void> { 
// 	    @Override
// 	    protected Void doInBackground(Void... arg) {
// 	    	HttpClient httpclient = new DefaultHttpClient();
// 	        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
// 	        HttpPost httppost = new HttpPost("http://108.168.239.90:8080/session/send");
	        
// 	        try {
// 	        	// Get the image from the path
// 	        	Intent intent = getIntent();
// 	    		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
// 		        File file = new File(message);
// 	        	// Add your data
// 		        MultipartEntity entity = new MultipartEntity();
// 		        ContentBody cbFile = new FileBody(file, "image/*");
// 		        entity.addPart("userfile", cbFile);
	        	
//     	        httppost.setEntity(entity);
//     	        HttpResponse response = httpclient.execute(httppost);
// 	        } catch (Exception e){
// 	        	Log.d("debug", "IOException");
// 	        }
// 	        return null;
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

	private String m_ip;  // Dissent server IP, default to "108.168.239.90"
	private String m_port;  // Dissent server port, default to "8080"
	private boolean m_encry;  // if true, encrypt messages with AES (TODO)
}
