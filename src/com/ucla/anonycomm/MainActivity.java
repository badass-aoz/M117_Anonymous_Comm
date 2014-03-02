package com.ucla.anonycomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.ucla.AnonyComm.MESSAGE";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
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

    // who calls this function? Ao
    // display a float box when the encryCheckBox is checked
    public void encryChkboxClick(View view) {
    	
    	if (((CheckBox) view).isChecked()) {
    		Toast.makeText(MainActivity.this,
    				"Using AES encryption (not implemented yet)", Toast.LENGTH_LONG).show();
    	}
    }

    
    public void sendMessage(View view) {
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String message = editText.getText().toString();
    	intent.putExtra(EXTRA_MESSAGE, message);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
//    private boolean fileExist(String fname){
//        return getBaseContext().getFileStreamPath(fname).exists();
//    }
// 
//    
//    public void saveSettings(String ip, String port) {
//		try {
//			FileOutputStream fo = openFileOutput(setting_file, MODE_PRIVATE);
//			String content = ip+":"+port;
//			fo.write(content.getBytes());
//			
//			//close file stream
//			fo.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//	
//	public static String server_ip_addr;
//	public static String server_port;
}
