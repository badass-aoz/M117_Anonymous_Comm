package com.ucla.anonycomm;

import org.abstractj.kalium.crypto.Box;
import org.abstractj.kalium.keys.KeyPair;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Settings extends PreferenceActivity {

	// hardcoded nonce
	public static String NONCE = "555555555555555555555555";
	public static String PREF = "Pref_file";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_settings);
		addPreferencesFromResource(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
		//TODO: If the settings has not been visited yet, encry option should be false
    	try {
			Preference genKey_btn = (Preference)findPreference("genKey_btn");
			
			//TODO: when user click the encry option, keyPair should be generated as well.
			genKey_btn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			                @Override
			                public boolean onPreferenceClick(Preference pref) { 
			                	KeyPair keys = new KeyPair();
			                	
			    	        	Toast.makeText(Settings.this,
			            				"Generating Key Pairs", Toast.LENGTH_LONG).show();
			                	//TODO: store keys to a more secure place
			                	SharedPreferences settings = getSharedPreferences(PREF, 0);
			                	SharedPreferences.Editor editor = settings.edit();
			                	editor.putString("pubKey", keys.getPublicKey().toString());
			                	editor.putString("priKey", keys.getPrivateKey().toString());
//				    	        	Toast.makeText(Settings.this,
//		            				keys.getPublicKey().toString() + " " + keys.getPrivateKey().toString(), Toast.LENGTH_LONG).show();
			                	editor.commit();
			                    return true;
			                }
			            });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
