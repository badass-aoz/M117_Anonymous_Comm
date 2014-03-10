package com.ucla.anonycomm;

import java.io.ByteArrayOutputStream;

import org.abstractj.kalium.crypto.Box;
import org.abstractj.kalium.encoders.Encoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReplyActivity extends Activity {
	
    public final static String EXTRA_MESSAGE = "com.ucla.AnonyComm.MESSAGE";
    public final static String PREF = "Pref_file";
    private String m_path;
    public final static String EXTRA_PATH = "com.ucla.AnonyComm.PATH";
	
	private String m_ip;
	private String m_port;
	private Box box;
	private String m_publicKey;
	
	private String m_msg;
	
	private EditText editText;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        m_msg=null;
       
      //share preferences from settings
      	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
      	m_ip = sharedPref.getString("setIP", "108.168.239.90");
      	m_port = sharedPref.getString("setPort", "8080");
      	
      	editText = (EditText)findViewById(R.id.edit_message);
      	
      	SharedPreferences keySetting = getSharedPreferences(MainActivity.PREF, 0);
      	
      	//get keys that required for the box
      	String privateKey = keySetting.getString("priKey",null);
      	m_publicKey = keySetting.getString("pubKey", null);
      	Intent intent = getIntent();
      	String otherPubKey = intent.getStringExtra(Message.EXTRA_PUBKEY);
      	
      	//create a box
      	try{
      		box=new Box(otherPubKey,privateKey,Encoder.HEX);
      	}catch(Exception e){
      		e.printStackTrace();
      		return;
      	}
      	     	
		
    }
    
    public void sendMessage(View view){
    	
    	m_msg = editText.getText().toString();
    	
    	new SendMsg().execute(); 
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
            	openSettings();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    

    public void openSettings() {
    	Intent intent = new Intent(this, Settings.class);
    	
    	int requestCode = 1; //a random request code
    	startActivityForResult(intent, requestCode);
    	
    }
    // Added code for getting photo and display it
    public void selectPhoto(View view) {
    	InputMethodManager imm = (InputMethodManager)getSystemService(
    		      Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
    	Intent intent = new Intent();
    	intent.setType("image/*");
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == Activity.RESULT_OK ) {
            if(requestCode == 1) {
            	Uri udata = data.getData();
	    		m_path = getRealPathFromURI(udata);
	    		TextView text = (TextView) findViewById(R.id.image_path_textView);
	    		text.setText("image chosen:\n" + m_path);
            }
        }    	
    }
	// Added code for getting photo and display it

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class SendMsg extends AsyncTask<Void, Void, Integer> {
		 
		 private ProgressDialog dialog = new ProgressDialog(ReplyActivity.this);
		 
		 @Override
		 protected void onPreExecute() {
		        this.dialog.setMessage("Sending. Be prepared to wait till the battery runs up :(");
		        this.dialog.show();
		 }
		 
	        @Override
	        protected Integer doInBackground(Void... arg) {
	    		int resp1 = 0, resp2 = 0;
	    		try {
	    			// send photo only if the path is non empty
	    	    	    if((m_path!=null)&&!m_path.isEmpty()) {
	    	    		HttpClient httpclient2 = new DefaultHttpClient();
	    	    		HttpPost httppost2 = new HttpPost("http://" + m_ip + ":" + m_port + "/session/send");
	    	    		
	    	    		
	    	    		// Get the bytes of the image
	    	    		//File f = new File(m_imagePath);
	    	    		Bitmap bm = BitmapFactory.decodeFile(m_path);
	    	    		Bitmap resized = Bitmap.createScaledBitmap( bm, 80, 80, true);
	    	    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        resized.compress(CompressFormat.JPEG, 40, bos);
	    	    		//byte[] content = org.apache.commons.io.FileUtils.readFileToByteArray(f);
	    	    		byte[] content = bos.toByteArray();
	    	    		byte[] encrypted;
	    	    		try{
	    	    			encrypted = box.encrypt(Settings.NONCE.getBytes(), content);
	    	    		}catch(Exception e){
	    	    			e.printStackTrace();
	    	    			return -1;
	    	    		}
	    	    		
	    	    		String contenthex = new String(Hex.encodeHex(encrypted));
				        // Convert the bytes into hexadecimal string
//	    	    		String contenthex = "";
//	    	    		for (int i = 0; i < content.length; i++) {
//	    	    			String hex = Integer.toHexString(0xFF & content[i]);
//	    	    			if(hex.length() == 1)
//	    	    				contenthex += "0";
//	    	    			contenthex += hex;
//	    	    		}
	    	    		
	    	    		contenthex = "enImg_" + contenthex+"_"+m_publicKey;
	    	    		
	    	    		
	    	    		// Execute the post request
	    	    		HttpEntity entity2 = new ByteArrayEntity(contenthex.getBytes("UTF-8"));
	    	    		httppost2.setEntity(entity2);
	    	    		
	    	    		HttpResponse hresp1 = httpclient2.execute(httppost2);
	    	    		if(hresp1 == null){
	    	    			resp1=-1;
	    	    		}
	    	    		else{
	    	    			resp1 = (hresp1.getStatusLine().getStatusCode() == 200) ? 0 : -1;
	    	    		}
	    	    	}
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			resp1 = -1;
	    		}
	    	    try {
	    	    	// send only when a user needs to send message
	    	    	if (m_msg!=null && !m_msg.isEmpty()) {
	    	        	HttpClient httpclient = new DefaultHttpClient();
	    	    	    HttpPost httppost = new HttpPost("http://" + m_ip + ":" + m_port + "/session/send");

	    	    	        
	    	    		HttpEntity entity;
	    	    		String text;
	    	    		byte[] encrypted;
	    	    		try{
	    	    			encrypted = box.encrypt(Settings.NONCE.getBytes(), m_msg.getBytes());
	    	    		}catch(Exception e){
	    	    			e.printStackTrace();
	    	    			return -1;
	    	    		}
	    	    		String contenthex = new String(Hex.encodeHex(encrypted));
	    	    		
	    	    		
	    	    		text = "enText_" + contenthex  + "_" + m_publicKey;
	    	    		
	    	    		
						entity = new ByteArrayEntity(text.getBytes("UTF-8"));
	    	    		httppost.setEntity(entity);
		    	        // Execute HTTP Post Request
	    	    	        HttpResponse hresp2 = httpclient.execute(httppost);
	    	    	        if (hresp2 == null || hresp2.getStatusLine().getStatusCode() != 200)
	    	    	    	    resp2 = -1;
	    	            }
	    	        } catch (Exception e) {

	    	        	resp2 = -1;
	    	        }
	    	        // if both are zero, should sum to zero
   	    	        return resp2 + resp1;
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(Integer resp) {
	            if (dialog.isShowing()) {
	                dialog.dismiss();
	            }
   	        if (resp == 0) {
   	        	Toast.makeText(ReplyActivity.this,
       				"Sent Successfully", Toast.LENGTH_LONG).show();
   	        } else {
   	        	Toast.makeText(ReplyActivity.this,
       				"Failed to send"  , Toast.LENGTH_LONG).show();
   	        }
	       }
	}	 
    
 
    
}
