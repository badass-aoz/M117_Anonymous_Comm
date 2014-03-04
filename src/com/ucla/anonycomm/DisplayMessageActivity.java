package com.ucla.anonycomm;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
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
import android.view.View;
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
		m_message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		TextView t_msg = (TextView) findViewById(R.id.sent_message);
		t_msg.setTextSize(20);
		t_msg.setText(m_message);

		// Create the image view
		ImageView imageView = (ImageView) findViewById(R.id.sent_image);
		m_imagePath = intent.getStringExtra(MainActivity.EXTRA_PATH);
		Bitmap image = BitmapFactory.decodeFile(m_imagePath);
		imageView.setImageBitmap(image);

		new SendMsg().execute();
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

	 private class SendMsg extends AsyncTask<Void, Void, Integer> {
	        @Override
	        protected Integer doInBackground(Void... arg) {
	    		int resp1 = 0, resp2 = 0;
	    	    try {
	    	    	
	    	    	// Kwun, please make a separate httpost and send the image (if m_path is non-empty).
	    	    	// put the response into resp2 (like what I did below)
	    	    	
	    	        // Add your data
//	    	    	MultipartEntity entity = new MultipartEntity();
//	 		        File file = new File(m_imagePath);
//	    	    	ContentBody cbFile = new FileBody(file, "image/*");
//	    	    	entity.addPart("userfile", cbFile);
//	    	    	httppost.setEntity(entity);

	    	    	// TODO: fix the async bug
	    	    	// update: for some reason, it still fails sometimes. But since network 
	    	    	// is unstable anyway, we tend to ignore it.
	    	    	
	    	    	// send only when a user needs to send message
	    	    	if (!m_message.isEmpty()) {
	    	        	HttpClient httpclient = new DefaultHttpClient();
	    	    	    HttpPost httppost = new HttpPost("http://"+m_ip+":"+m_port+"/session/send");
	    	    		
	    	    	    HttpEntity entity = new ByteArrayEntity(m_message.getBytes("UTF-8"));
	    	    	    httppost.setEntity(entity);
		    	        // Execute HTTP Post Request
	    	    	    HttpResponse hresp2 = httpclient.execute(httppost);
	    	    	    if (hresp2 == null)
	    	    	    	resp2 = -1;
	    	    	    else
	    	    	    	resp2 = (hresp2.getStatusLine().getStatusCode() == 200)?0:-1;
	    	    	}
	    	    	// if both are zero, should sum to zero
	    	    	return resp2+resp1;
	    	    } catch (Exception e) {
	    	    	
	    	    }
	    	    return -1;
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(Integer resp) {
    	        if (resp == 0) {
    	        	Toast.makeText(DisplayMessageActivity.this,
        				"Sent Successfully", Toast.LENGTH_LONG).show();
    	        } else {
    	        	Toast.makeText(DisplayMessageActivity.this,
        				"Failed to send, response code "  , Toast.LENGTH_LONG).show();
    	        	//response==null?"NULL":""+response.getStatusLine().getStatusCode()
    	        }
	       }
 	}	   
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}

	public void resend_msg(View view){
		new SendMsg().execute();
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
	private String m_imagePath;
	private String m_message;
}
