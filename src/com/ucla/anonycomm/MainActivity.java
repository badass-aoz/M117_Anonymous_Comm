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
    			Toast.makeText(MainActivity.this,
 		 	   "Settings have not been implemented yet :(", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
