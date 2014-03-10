package com.ucla.anonycomm;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.layout;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveMessage extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_message);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);
	


		new ReceiveMsg().execute();
	}

<<<<<<< HEAD
=======
	

	public static byte convertAHex(String hexStr) {
		return (byte) Integer.decode("0x"+hexStr).intValue();
	}
	
	
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
	 private class ReceiveMsg extends AsyncTask<Void, Void, String> {
		 
		 private ProgressDialog dialog = new ProgressDialog(ReceiveMessage.this);
		 
		 @Override
		 protected void onPreExecute() {
		        this.dialog.setMessage("Receiving. Better connect your phone to the charger :)");
		        this.dialog.show();
		 }
<<<<<<< HEAD
		 
	        @Override
	        protected String doInBackground(Void... arg) {
=======

	        @Override
	    protected String doInBackground(Void... arg) {
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394

	    		HttpResponse response = null;
	    		String response_String="";
	    		try {
	    	    	// receive only when a user needs to receive message
	    	        	HttpClient httpclient = new DefaultHttpClient();
	    	    	    HttpGet httpget = new HttpGet("http://108.168.239.90:8080/session/messages?offset=0&count=100&wait=false");
	    	    		response = httpclient.execute(httpget);
        	    	    response_String = EntityUtils.toString(response.getEntity());	    		
	    	    } catch (Exception e) {
	    	    	Toast.makeText(ReceiveMessage.this,
	        				"exception response"  , Toast.LENGTH_LONG).show();
						
	    	    }
	    	    // if both are zero, should sum to zero
    	    	return response_String;
<<<<<<< HEAD
	        }
	        
	        
	        
	        // onPostExecute displays the results of the AsyncTask.
            @Override
	        protected void onPostExecute(String response_String) {
=======
	    }
	        
	    // onPostExecute displays the results of the AsyncTask.
        @Override
	    protected void onPostExecute(String response_String) {
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
	            if (dialog.isShowing()) {
	                dialog.dismiss();
	            }
    	        if (response_String!=""){ 	
    	        	Toast.makeText(ReceiveMessage.this,
        				"Received Successfully", Toast.LENGTH_LONG).show();
    	        } else {
    	        	Toast.makeText(ReceiveMessage.this,
        				"Failed to receive, please try again."  , Toast.LENGTH_LONG).show();
    	        	    return ;
    	        }

    	        //process the string
    	        String json=response_String;
<<<<<<< HEAD
    	        //List<String> image_String=new ArrayList<String>();
    	        String image="image";
=======
    	        List<String> image_String=new ArrayList<String>();	
    	        final List<Bitmap> bmp_array=new ArrayList<Bitmap>();
    	        String image="";
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
    	        Bitmap bmp = null;
    	        byte[] data=null;
    	        try {
					JSONObject obj = new JSONObject(json);
					JSONArray jsonArr= obj.getJSONArray("messages");
					String[] arr=new String[jsonArr.length()];
					for(int i=0; i<jsonArr.length();i++)
						arr[i]=(String) jsonArr.get(i);
<<<<<<< HEAD
					response_String="Start:";			
=======
					response_String="";			
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
					
					// Split the Json message into Text_part and Image part
					for(int i=0; i<jsonArr.length();i++)
					{
                  
<<<<<<< HEAD
						if(arr[i].length()<=7)
							response_String += arr[i]+"\n\n";
						else
						{
							if(arr[i].substring(0,7).equals("_text_:"))
								response_String += arr[i].substring(7)+"\n\n";
							if(arr[i].substring(0,8).equals("_image_:"))
								image=arr[i].substring(9);
						    if(arr[i].length()>=100 && !arr[i].substring(0, 7).equals("_text_:"))  // FIXME: Only receive the latest image now
								image=arr[i];
=======
						if(arr[i].length()<=7){
						   if(!response_String.contains(arr[i]))
							response_String += arr[i]+"\n\n";}
						else
						{
							if(arr[i].substring(0,7).equals("_text_:")){
							  if(!response_String.contains(arr[i].substring(7)))
								response_String += arr[i].substring(7)+"\n\n";}
							if(arr[i].substring(0,8).equals("_image_:"))
							  if(!image_String.contains(arr[i].substring(8)))
								image_String.add(arr[i].substring(8));
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
						}
												
				    }

					//Convert hex string to byte array			    				    	 
<<<<<<< HEAD
				   if(image!=""){   //FIXME: it only deal with the first graph now 

		            	Toast.makeText(ReceiveMessage.this,
	        				"converting image", Toast.LENGTH_LONG).show();
		            	  System.out.println(image);
		            	  System.out.println("image length="+image.length());
		            	  
		            	  int length = image.length();  
		            	  if (length % 2 == 1)  
		            	  {  
		            	      image = "0" + image;  
		            	      length++;  
		            	  } 
		            	data = new byte[length / 2];
		            	for (int i = 0; i < length; i += 2) {
		                    data[i / 2] = (byte) ((Character.digit(image.charAt(i), 16) << 4)
		                                         + Character.digit(image.charAt(i+1), 16));
		                }
                       System.out.println(data);
					   bmp = BitmapFactory.decodeByteArray(data, 0, data.length);					
				   }
=======
				   if(image_String.size()!=0){  
                     for(int j=0; j<image_String.size();j++) {
		                bmp=null;
		                data=null;
		                image="";
		                
		            	int length = image_String.get(j).length();  
		            	image = image_String.get(j);
		            	if (length % 2 == 1){  
		            	      image = "0" +image;  
		            	      length++;  
		            	  } 
		                data = new byte[length/2];
		                for(int i = 0, k = 0; i < length; i += 2, k++) {
                             String hexStr = image.substring(i, i+2);
	        	             data[k] = convertAHex(hexStr);
                        }
                       System.out.println(data);
					   
                       bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                       if(bmp!=null)
					     bmp_array.add(bmp);
                     }
			       }
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
				 } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
    	        
    	        //Display the message on the screen
    	        final TextView t_msg = (TextView) findViewById(R.id.received_message);
    			t_msg.setTextSize(20);
<<<<<<< HEAD
    			//t_msg.setText(json);
    			t_msg.setText(image);
		        t_msg.setMovementMethod(new ScrollingMovementMethod());
	
		        //Display the image on the screen
		     	final ImageView imageView= (ImageView) findViewById(R.id.received_image);
				if(bmp != null)  // For debugging purpose
				{
					imageView.setImageBitmap(bmp);
					Toast.makeText(ReceiveMessage.this,
	        				"Do have image", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(ReceiveMessage.this,
	        				"Do not have image", Toast.LENGTH_LONG).show();
				}
    	        
                // Button to switch between image and text
				 Button button = (Button) findViewById(R.id.display_image);
				 final Bitmap bmp2=bmp;
				 
				button.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                // Perform action on click		            	
		            	t_msg.setVisibility(View.INVISIBLE);
		            	imageView.setImageBitmap(bmp2);
=======
    			t_msg.setText(response_String);
		        t_msg.setMovementMethod(new ScrollingMovementMethod());
	
		        //Display the image on the screen
		        final RelativeLayout l = ((RelativeLayout)(findViewById(R.id.container)));
		        for (int i = 0; i < bmp_array.size(); i ++) {
		            ImageView image_temp = new ImageView(ReceiveMessage.this);
		           
		            image_temp.setImageBitmap(bmp_array.get(i));
		            l.addView(image_temp);
		            image_temp.setId(i);
		            image_temp.getLayoutParams().height = 500;
		            image_temp.getLayoutParams().width = 500;
                    image_temp.setVisibility(View.INVISIBLE);
		            MarginLayoutParams marginParams = new MarginLayoutParams(image_temp.getLayoutParams());
		            marginParams.topMargin=200+i*480;
		            marginParams.leftMargin=288;
		            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
		            image_temp.setLayoutParams(layoutParams); 
		        }
		        
                // Button to switch between image and text
				 Button button = (Button) findViewById(R.id.display_image);

				 button.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		                // Perform action on click		            	
		            	if(t_msg.getVisibility() == View.VISIBLE){
		            	  t_msg.setVisibility(View.INVISIBLE);
		            	for(int i=0; i<bmp_array.size();i++){
	            		  ImageView imageView= (ImageView) findViewById(i);
		            	  imageView.setVisibility(View.VISIBLE);}       	
         		        }
		            	else
		            	{
			             for(int i=0; i<bmp_array.size();i++){
			            		  ImageView imageView= (ImageView) findViewById(i);
				            	  imageView.setVisibility(View.INVISIBLE);}       	
		         		 t_msg.setVisibility(View.VISIBLE);
		            	}
>>>>>>> 02d11119ac7eeb8ebd63af4e652a7660b368d394
		            }
		        });
            
            }
 	}	   


	 
	 
	 
//	/**
//	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
//	 * simply returns the {@link android.app.Activity} if
//	 * <code>getThemedContext</code> is unavailable.
	 
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}


	

	
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.receive_message, menu);
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

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_receive_message_dummy, container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}


	private String m_ip;  // Dissent server IP, default to "108.168.239.90"
	private String m_port;  // Dissent server port, default to "8080"
	private boolean m_encry;  // if true, encrypt messages with AES (TODO)
	private String m_message;
}
