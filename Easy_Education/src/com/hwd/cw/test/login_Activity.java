package com.hwd.cw.test;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.UserInfo;
import io.rong.imlib.RongIMClient.ConnectCallback.ErrorCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class login_Activity extends Activity {
	
	private EditText username;
	private EditText password;
	private Button login;
	private Button logup;
	String username_text;
	String password_text;
	SharedPreferences userinfo;
	 public static final int REQUSET = 1;  
	 public static String token="ivhGCZIeebnE5UbHucfaW49pCHeLP6BwILcouxyOPNAQBoEPDnBqnJ1MwmOK6zhkQbeaDkXrJCrLzb2vfmBKiQ==";
	 private  ProgressDialog progressBar;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        ExitApplication.getInstance().addActivity(this);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.log_xml);
	        userinfo = getSharedPreferences("user", Activity.MODE_PRIVATE);
	        username=(EditText) this.findViewById(R.id.login_username);
	        password=(EditText)this.findViewById(R.id.login_passphrase);
	        login=(Button)this.findViewById(R.id.login_btn);
	        logup=(Button)this.findViewById(R.id.logup_btn);
	        
	        login.setOnClickListener(new OnClickListener(){

				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					
					
					username_text=username.getText().toString();
					password_text=password.getText().toString();
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("username", username_text);
					map.put("password", password_text);
					loginTask mytask=new loginTask();
					mytask.executeOnExecutor(Executors.newCachedThreadPool(),map);	
					
					
				}	
	        });
	        
	        logup.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					Intent intent = new Intent(login_Activity.this,  
	                        registActivity.class);  
	                //锟斤拷锟斤拷锟斤拷图锟斤拷示为REQUSET=1  
	                startActivityForResult(intent, REQUSET);
					
				}
	        	
	        	
	        });
	       
	        
	 }
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        // TODO Auto-generated method stub  
	        super.onActivityResult(requestCode, resultCode, data);  
	        //requestCode锟斤拷示锟斤拷锟斤拷谋锟绞�   resultCode锟斤拷示锟斤拷锟斤拷锟斤拷  
	        if (requestCode == login_Activity.REQUSET && resultCode == RESULT_OK) { 
	        	
	        	username.setText(data.getStringExtra("username"));
	        	password.setText(data.getStringExtra("password"));
	            
	        }  
	        
	    }  
	 
	 public class loginTask extends AsyncTask<Map<String,Object>,Integer,String>
	 {
		@Override
		protected String doInBackground( Map<String,Object>... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","login"));
			param.add(new BasicNameValuePair("username",username_text));
			param.add(new BasicNameValuePair("password",password_text));	
			System.out.println("username="+username_text);
			System.out.println("password="+password_text);
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	            // 锟斤拷锟斤拷httpPost锟斤拷锟斤拷锟斤拷锟� 
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	            //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                // 锟斤拷锟斤拷锟斤拷锟斤拷使锟斤拷getEntity锟斤拷锟斤拷锟斤拷梅锟斤拷亟锟斤拷 
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("login_result:" + result);             
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
	           
			  
			 	String state=new String("");
				String info=new String("");
				String nickname=new String("");
				String hint=new String("");
				String tag=new String("");
				String headsrc=new String("");
				String stored=new String("");
				String store=new String("");
				String mytoken=new String("");
				String job=new String("");
				String id=new String("");
		//	Toast.makeText(getApplicationContext(), result, 50000).show();
			 try {
				JSONObject data = new JSONObject(result);
				System.out.println(data.toString());
				state=data.getString("state");
				info=data.getString("info");
				nickname=data.getString("nickname");			
				hint=data.getString("hint");
				tag=data.getString("tag");
				headsrc=data.getString("headsrc");
				stored=data.getString("stored");
				store=data.getString("store");	
				mytoken=data.getString("token");
				job=data.getString("job");
				id=data.getString("uid");
			} catch (JSONException e) {
				// TODO 锟皆讹拷锟斤拷锟缴碉拷 catch 锟斤拷
				e.printStackTrace();
			}  
			 progressBar.dismiss();
			 if(info.equals("Succeed"))
			 {
			  SharedPreferences.Editor editor=userinfo.edit();
			  editor.putString("state", state);
			  editor.putString("info", info);
			  editor.putString("nickname", nickname);
			  editor.putString("hint", hint);
			  editor.putString("tag", tag);
			  editor.putString("headsrc", headsrc);
			  editor.putString("stored", stored);
			  editor.putString("store", store);
			  editor.putString("token", mytoken);
			  editor.putString("job", job);
			  editor.putString("id", id);
			  editor.commit();
			  System.out.println("nickname="+userinfo.getString("nickname", ""));
			  MainActivity.islogin=1;
			 
			  Toast.makeText(getApplicationContext(), "登陆成功", 500).show();		  
			  try {
					RongIM.connect(mytoken, new ConnectCallback() {

						@Override
						public void onError(ErrorCode arg0) {
														
						}
						@Override
						public void onSuccess(String arg0) {
							
						}
						}	
						);
					
					
						
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}	  
			  login_Activity.this.finish();
			  
			  
			  
			 }
			 else{
			 Toast.makeText(getApplicationContext(), "登陆失败，请检查用户名和密码", 1000).show();
			 }
	        }  
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(login_Activity.this);

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
	
	  @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
	            // 创建退出对话框  
	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            // 设置对话框标题  
	            isExit.setTitle("退出？");  
	            // 设置对话框消息  
	            isExit.setMessage("确定要退出吗");  
	            // 添加选择按钮并注册监听  
	            isExit.setButton("确定", listener);  
	            isExit.setButton2("取消", listener);  
	            // 显示对话框  
	            isExit.show();  
	  
	        }  
	          
	        return false;  
	          
	    }  
	    /**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	            	ExitApplication.getInstance().exit(login_Activity.this);
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
}

