package com.ucla.anonycomm;

import java.util.ArrayList;
import java.util.List;

import org.abstractj.kalium.keys.KeyPair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
    public final static String EXTRA_MESSAGE = "com.ucla.AnonyComm.MESSAGE";
    public final static String PREF = "Pref_file";
    private String m_path;
    public final static String EXTRA_PATH = "";
    private String m_priKey;
	private String m_pubKey;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        hideSoftKeyboard();
        
        SharedPreferences keySetting = getSharedPreferences(MainActivity.PREF, 0);
		m_priKey = keySetting.getString("priKey",null);
		m_pubKey = keySetting.getString("pubKey",null);
		
		
		if(m_priKey == null || m_pubKey == null){
        	Toast.makeText(this,
    				"Generating Key Pairs", Toast.LENGTH_LONG).show();
        	
        	//generate key pairs
        	KeyPair keys = new KeyPair();
        	m_priKey = keys.getPrivateKey().toString();
        	m_pubKey= keys.getPublicKey().toString();
        	
        	//Add keys to the preference List
        	SharedPreferences settings = getSharedPreferences(MainActivity.PREF, 0);
        	SharedPreferences.Editor editor = settings.edit();
        	editor.putString("pubKey", keys.getPublicKey().toString());
        	editor.putString("priKey", keys.getPrivateKey().toString());
        	editor.commit();
		}
		new ReceiveMsg().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
            	openSettings();
            	return true;
            case R.id.receive_message:
            	receiveMessage();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void sendMessage(View view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
    	intent.putExtra(EXTRA_PATH, m_path);
    	startActivity(intent);
    }
    
    public void receiveMessage() {
    	Intent intent = new Intent(this, ReceiveMessage.class);
    	startActivity(intent);
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
    
    
    private class ReceiveMsg extends AsyncTask<Void,Void,String>{
    	
    	private ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		 
		 @Override
		 protected void onPreExecute() {
		        this.dialog.setMessage("Receiving. Better connect your phone to the charger :)");
		        this.dialog.show();
		 }
		 
		 @Override
		 protected String doInBackground(Void... arg) {
			 HttpResponse response = null;
			 String response_String="";
			 try {
				 // receive only when a user needs to receive message
				 HttpClient httpclient = new DefaultHttpClient();
				 HttpGet httpget = new HttpGet("http://108.168.239.90:8080/session/messages?offset=0&count=100&wait=false");
				 response = httpclient.execute(httpget);
				 response_String = EntityUtils.toString(response.getEntity());	    		
	    	    } catch (Exception e) {
	    	    	Toast.makeText(MainActivity.this,
	        				"exception response"  , Toast.LENGTH_LONG).show();
	    	    }
			 return response_String;
		 }
			 
		 
		 @Override
		 protected void onPostExecute(String responseString){
			 if(dialog.isShowing()){
				 dialog.dismiss();
			 }
			 if(responseString!=""){
				 Toast.makeText(MainActivity.this,
		       				"Received Successfully", Toast.LENGTH_LONG).show();
			 }else{
				 Toast.makeText(MainActivity.this,
		       				"Failed to receive, please try again."  , Toast.LENGTH_LONG).show();
		   	        	    return ;
			 }
			 
			 // all dialogs done
			 String json = responseString;
			 String [] arr;
			 try{
				 JSONObject obj = new JSONObject(json);
				 JSONArray jsonArr = obj.getJSONArray("messages");
				 arr=new String[jsonArr.length()];
				 for(int i=0; i<jsonArr.length();i++)
					 arr[i]=(String) jsonArr.get(i);
			 }catch(Exception e){
				 e.printStackTrace();
				 return;
			 }
			 
			 
			 // generate Message obj
			 List<Message> messageList = new ArrayList<Message>();
			 
			 for (String msg:arr){
				 try{
					 Message m = new Message(MainActivity.this,msg);
					 messageList.add(m);
				 }catch(Exception e){
					 e.printStackTrace();
				 }
			 }
			 
			 LinearLayout layout = (LinearLayout)findViewById(R.id.dynamic);
			 
			 for(Message msg : messageList){
				 if(msg.isImage()){
					 try{
						 layout.addView(msg.getImageButton());
					 }
					 catch (Exception e){
						 e.printStackTrace();
					 }
				 }
				 else{
					 try{
						 layout.addView(msg.getButton());
					 }
					 catch (Exception e){
						 e.printStackTrace();
					 }
					 
				 }
				 
			 }
			 
		 }
		 
    }
    
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    
}
