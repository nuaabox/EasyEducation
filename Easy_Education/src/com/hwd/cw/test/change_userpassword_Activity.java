package com.hwd.cw.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class change_userpassword_Activity extends Activity{
	
	private EditText now_password;
	private EditText new_password1;
	private EditText new_password2;
	private TextView cancer;
	private TextView confirm;
	private ProgressDialog progressBar;
	private SharedPreferences userinfo;
	
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.change_password_xml);
	        userinfo=this.getSharedPreferences("user", MODE_PRIVATE);
	        
	        ExitApplication.getInstance().addActivity(this);
	        
	        now_password=( EditText)this.findViewById(R.id.change_password_old);
	        new_password1=( EditText)this.findViewById(R.id.change_password_new1);
	        new_password2=( EditText)this.findViewById(R.id.change_password_new2);
	        
	        cancer=(TextView)this.findViewById(R.id.change_password_cancer);
	        confirm=(TextView)this.findViewById(R.id.change_password_confim);
	        
	        cancer.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					finish();
				}
	        	
	        });
	        
	        confirm.setOnClickListener(new OnClickListener(){

				@SuppressLint("ShowToast")
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					if(now_password.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(), "请输入现在的密码", 500).show();
						now_password.setFocusable(true);
						now_password.setFocusable(true);   
						now_password.setFocusableInTouchMode(true);   
						now_password.requestFocus();  	
					}
					else if( !new_password1.getText().toString().equals
							(new_password2.getText().toString()) )
					{
						
						Toast.makeText(getApplicationContext(), "两次输入密码不一致", 500).show();
						
					}
					else if(new_password1.getText().toString().equals
							(new_password2.getText().toString()) )
					{
						
						change_password_Task changeTask=new change_password_Task();
						changeTask.executeOnExecutor(Executors.newCachedThreadPool(),"");
					}
				
				
				}
	        	
	        });
	        
	        
	        
	        
	 }
	 public class change_password_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","change_password"));
			param.add(new BasicNameValuePair("uid",userinfo.getString("id", "")));	
			System.out.println("修改密码的id是："+userinfo.getString("id", ""));
			param.add(new BasicNameValuePair("new_password",new_password1.getText().toString()));
			param.add(new BasicNameValuePair("old_password",now_password.getText().toString()));
			
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("changeresult:" + result);             
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
					Toast.makeText(getApplicationContext(), "修改成功", 1000).show();
					Intent intent=new Intent();
					finish();
					
					intent.setClass(change_userpassword_Activity.this, login_Activity.class);
					
					startActivity(intent);
				}
				else 
				{
					
					
					if(data.getString("info").equals("Wrong password."))				
					Toast.makeText(getApplicationContext(), "原始密码错误，修改失败", 500).show();
					
				}
				progressBar.dismiss();
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(change_userpassword_Activity.this);

				progressBar.setCancelable(true);

				progressBar.setMessage("修改中 ...");

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
