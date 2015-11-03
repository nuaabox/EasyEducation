package com.easyeducation.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import io.rong.imkit.RongIM;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.easyeducation.MainActivity;
import com.hwd.cw.test.R;
import com.hwd.cw.test.R.color;

public class login_activity  extends Activity{

	private EditText  username,password;
	private TextView  login,newuser,forgetpassword;
	private String myusername,mypassword;
	private ProgressDialog progressBar;

	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.login_activity_xml);
		InitView();
		Intent myintent=this.getIntent();
		if(myintent!=null)
		{
			String phone= myintent.getStringExtra("phone");
			username.setText(phone);	
		}
		
		
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
		
				if(!username.getText().toString().equals("") && 
						!password.getText().toString().equals(""))
				{
				loginTask task=new loginTask();
				task.execute();
				}
				else Toast.makeText(login_activity.this, "登录名或密码不能为空", 500);
				
			}

		});
		
		newuser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				
				
				RegisterPage registerPage = new RegisterPage();
				registerPage.setRegisterCallback(new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
				// 解析注册结果
				if (result == SMSSDK.RESULT_COMPLETE) {
				@SuppressWarnings("unchecked")
				HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
		
					String country = (String) phoneMap.get("country");
					String phone = (String) phoneMap.get("phone"); 
					
					Intent intent=new Intent();
					intent.putExtra("phone", phone);
					intent.setClass(login_activity.this,regis_acitivity.class);
					startActivity(intent);
				// 提交用户信息
			
				}
				}
				});
				registerPage.show(login_activity.this);
				
			
				
			}

		});
		
		forgetpassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				
			}

		});
		
		
		
		
		
		
		
		
		

	}
	
	private void InitView()
	{
		username=(EditText)findViewById(R.id.login_activity_username);
		password=(EditText)findViewById(R.id.login_activity_password);
		login=(TextView)this.findViewById(R.id.login_acitivity_loginbtn);
		newuser=(TextView)this.findViewById(R.id.login_acitivity_newuser);
		forgetpassword=(TextView)this.findViewById(R.id.login_acitivity_fogetpassword);
		
		
	}
	
	 
	 public class loginTask extends AsyncTask<Object,Integer,String>
	 {
		
		  
	
		 
		 
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/action.php";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			
			param.add(new BasicNameValuePair("action","login"));
			param.add(new BasicNameValuePair("phone",username.getText().toString()) );	
			param.add(new BasicNameValuePair("password",password.getText().toString()));
		
			
			
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("loginresult:" + result);             
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
				if(data.getString("code").equals("1"))
				{
					Toast.makeText(getApplicationContext(), "登陆成功", 500).show();				
					Intent intent=new Intent();
					intent.setClass(login_activity.this,MainActivity.class );
					startActivity(intent);
					
				}
				else if(data.getString("info").equals("Wrong Password!"))
				{
					Toast.makeText(getApplicationContext(), "密码错误", 500).show();
					
				}
				else if(data.getString("info").equals("User does not exists."))
				{
					Toast.makeText(getApplicationContext(), "用户名不存在", 500).show();
					
				}
				
				progressBar.dismiss();
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(login_activity.this);

				progressBar.setCancelable(true);

				progressBar.setMessage("登陆中 ...");

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
