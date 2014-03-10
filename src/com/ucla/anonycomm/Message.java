package com.ucla.anonycomm;

import org.abstractj.kalium.crypto.Box;
import org.abstractj.kalium.encoders.Encoder;
import org.apache.commons.codec.binary.Hex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class Message {
	
	public static final String EXTRA_PUBKEY = "com.ucla.anonycomm.pubkey";
	
	String m_msg;
	Activity activity;
	Box box;
	Bitmap m_bmp;
	
	String m_publicKey;
	
	boolean checkMsg;
	
	public Message(Activity activity, String msg) throws Exception{
		SharedPreferences keySetting = activity.getSharedPreferences(MainActivity.PREF, 0);
		String priKey = keySetting.getString("priKey",null);
		this.activity = activity;
		
		// get all arguments from msg
		String[] args = msg.split("_");
		
		String pubKey = args[2];
		m_publicKey = pubKey;
		box = new Box(pubKey,priKey,Encoder.HEX);
		if (args.length!=3){
			
			throw (new Exception("Wrong Message"));
		}
		if (args[0].contains("imge")){
			String img = args[1];
			char[] data = img != null ? img.toCharArray() : new char[0];
			byte[] imgBytes = Hex.decodeHex(data);
			m_bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);	
			checkMsg = false;
		}
		else if (args[0].contains("text")){
			m_msg = args[1];
			checkMsg = true;
			System.out.println(msg);
		}
		else if (args[0].contains("enImg")){
			String img = args[1];
			char[] charData = img != null ? img.toCharArray() : new char[0];
			byte[] data = Hex.decodeHex(charData);
			byte[] imgBytes;
			try{
				imgBytes = box.decrypt(Settings.NONCE.getBytes(), data);
			}catch(Exception e){
				e.printStackTrace();
				throw (new Exception("Wrong Message"));
			}
			m_bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);	
			checkMsg = false;
		}
		else if (args[0].contains("enText")){
			String recvMsg = args[1];
			char[] charData = recvMsg != null ? recvMsg.toCharArray() : new char[0];
			byte[] data = Hex.decodeHex(charData);
			try{
				byte[] decoded = box.decrypt(Settings.NONCE.getBytes(), data);
				m_msg = new String(decoded, "UTF-8");
				
			}catch(Exception e){
				e.printStackTrace();
				throw (new Exception("Wrong Message"));
			}
			checkMsg = true;
		}
		else{
			throw (new Exception("Wrong input"));
		}
		
		// construct a pub key and private key pair
		
		
		
	}
	
	public Button getButton(){
		
		
		RelativeLayout.LayoutParams rel_bottone = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		Button btn = new Button (activity);
		btn.setLayoutParams(rel_bottone);
		btn.getBackground().setAlpha(90);
		btn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(activity,ReplyActivity.class);
		    	intent.putExtra(EXTRA_PUBKEY,m_publicKey);
		    	activity.startActivity(intent);
			}
		});
		btn.setText(m_msg);
		return btn;
	}
	
	public ImageButton getImageButton(){
		RelativeLayout.LayoutParams rel_bottone = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		ImageButton btn = new ImageButton (activity);
		btn.setImageBitmap(m_bmp);
		btn.setLayoutParams(rel_bottone);
		btn.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(activity,ReplyActivity.class);
		    	intent.putExtra(EXTRA_PUBKEY,m_publicKey);
		    	activity.startActivity(intent);
			}
		});
		
		return btn;
	}
	
	
	
	
	
	
	
	public boolean isImage(){
		return !checkMsg;
	}
}
