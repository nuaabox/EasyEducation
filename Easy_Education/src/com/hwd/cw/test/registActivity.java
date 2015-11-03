package com.hwd.cw.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class registActivity extends Activity{
	

	private EditText username;
	private EditText password;
	private ImageView logup;
	String username_text=new String();
	String password_text=new String();
	int islogup=0;
	private ProgressDialog progressBar;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        ExitApplication.getInstance().addActivity(this);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.register_xml);
	        username=(EditText) this.findViewById(R.id.regin_username);
	        password=(EditText) this.findViewById(R.id.regin_passphrase);
	        logup=(ImageView) this.findViewById(R.id.register_btn);
	        logup.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�	
					username_text=username.getText().toString();
					password_text=password.getText().toString();
					logupTask task=new logupTask();
					task.executeOnExecutor(Executors.newCachedThreadPool(),"");
					
					
				}
	        	
	        });
	 }
	 
	 public class logupTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","logup"));
			param.add(new BasicNameValuePair("username",username_text));
			param.add(new BasicNameValuePair("password",password_text));	
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("result:" + result);             
	            } 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 							
			return result;
		} 
		 @SuppressLint("ShowToast")
		@Override  
	        protected void onPostExecute(String result) {  
			
			 try {
				JSONObject data = new JSONObject(result);		
				if(data.getString("state").equals("1"))
				{
					Toast.makeText(getApplicationContext(), "注册成功", 1000).show();
					islogup=1;
					Intent intent=new Intent();
					intent.putExtra("username", username_text);
					intent.putExtra("password", password_text);
					setResult(RESULT_OK,intent);
					finish();
				}
				else if(data.getString("info").equals("User already exists."))
				{
					Toast.makeText(getApplicationContext(), "用户已存在", 1000).show();
					
				}
				progressBar.dismiss();
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(registActivity.this);

				progressBar.setCancelable(true);

				progressBar.setMessage("注册中 ...");

				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				progressBar.setProgress(0);

				progressBar.setMax(100);

				progressBar.show(); 
		    }  
		  
		 @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        int vlaue = values[0];  
		        progressBar.setProgress(vlaue);  
		    }  
	 }
			
			
	


}
