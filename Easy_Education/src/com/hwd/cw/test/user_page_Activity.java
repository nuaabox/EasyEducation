package com.hwd.cw.test;

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

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConversationType;
import io.rong.imlib.RongIMClient.UserInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class user_page_Activity extends Activity{
	
	private ImageView userpage_back_btn;
	private TextView collection;
	private TextView collection_num;
	private TextView chat_btn;
	private ImageView userhead;
	String username;
	String nickname;
	 String[] stores;
	 SharedPreferences userinfo;
	 String uid;
	 private ProgressDialog progressBar;
	
	 private TextView nametext,jobtext,lable1,lable2,lable3,username_title,isonline;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.user_page_xml);
	        ExitApplication.getInstance().addActivity(this);
	        initView();
	        Intent intent=this.getIntent();
	        nickname=intent.getExtras().getString("nickname");
	        if(intent.getStringExtra("origin").equals("MainActivity"))
	        {
	        	username=MainActivity.username_list.get(nickname);
	        	
	        }
	        else if(intent.getStringExtra("origin").equals("univercity_page_Activity"))
	        {
	        	username=univercity_page_Activity.hotusername_list.get(nickname);
	        }
	        else if(intent.getStringExtra("origin").equals("collection_Activity"))
	        {
	        	username=collection_Activity.collec_username_list.get(nickname);
	        }
	        else if(intent.getStringExtra("origin").equals("search_Activity"))
	        {
	        	username=search_Activity.search_username_list.get(nickname);
	        }
	        
	        Bitmap bitmap=(Bitmap) intent.getExtras().getParcelable("headimg");
        	if(bitmap!=null)
        	userhead.setImageBitmap((Bitmap) intent.getExtras().getParcelable("headimg"));
        	else 
        	userhead.setBackgroundResource(R.drawable.lst_usrimg1);
        	
	        getinfoTask mytask=new getinfoTask();//获取个人信息
			userinfo=getSharedPreferences("user", MODE_PRIVATE);	
		    mytask.executeOnExecutor(Executors.newCachedThreadPool(),"");

	        
	        userpage_back_btn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					user_page_Activity.this.finish();
				}	        	
	        } );
	        collection.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					
					TextView arg0=(TextView)v;
					if(arg0.getText().toString().equals("收藏"))
					{
						collection.setBackgroundColor(Color.GRAY);
						collection.setText("取消收藏");
					storeTask task1=new storeTask();
					task1.executeOnExecutor(Executors.newCachedThreadPool(),"");//执行收藏方法
					getstoreTask getstoretask=new getstoreTask();
				    getstoretask.executeOnExecutor(Executors.newCachedThreadPool(),"");//更新是否收藏
					}
					else 
					{			
						collection.setBackgroundColor(Color.parseColor("#aaddaf"));
						collection.setText("收藏");
						removestoreTask task2=new removestoreTask();
						task2.executeOnExecutor(Executors.newCachedThreadPool(),"");//执行取消收藏方法
						getstoreTask getstoretask=new getstoreTask();
					    getstoretask.executeOnExecutor(Executors.newCachedThreadPool(),"");//更新是否收藏;
					}
					
				}

	        });
	        chat_btn=(TextView)this.findViewById(R.id.chat_btn);
	        chat_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
									
					RongIM.getInstance().startConversation(user_page_Activity.this,ConversationType.PRIVATE,username, nickname);
					
					
					
					
					
				}
	        }
				);
}

	private void initView() {
		userpage_back_btn=(ImageView)this.findViewById(R.id.userpage_back_main);
		collection=(TextView)this.findViewById(R.id.userpagecollection_btn);
		collection_num=(TextView)this.findViewById(R.id.userpage_collection_num);
		nametext=(TextView)this.findViewById(R.id.userpage_username_text);
		jobtext=(TextView)this.findViewById(R.id.userpage_userdetail_text);
		lable1=(TextView)this.findViewById(R.id.userpage_lable1_text);
		lable2=(TextView)this.findViewById(R.id.userpage_lable2_text);
		lable3=(TextView)this.findViewById(R.id.userpage_lable3_text);
		username_title=(TextView)this.findViewById(R.id.userpage_user_name);
		isonline=(TextView)this.findViewById(R.id.isonline);
		userhead=(ImageView)this.findViewById(R.id.userpage_userhead);
		
		
		
	}
	 public class getinfoTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","get_info"));
			param.add(new BasicNameValuePair("username",username));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("getinfo_result:" + result);    
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
			getstoreTask getstoretask=new getstoreTask();//获取收藏信息
		    getstoretask.executeOnExecutor(Executors.newCachedThreadPool(),"");
			 try {
				JSONObject data = new JSONObject(result);		
				if(data.getString("state").equals("1"))
				{
					uid=data.getString("uid");
					nametext.setText(nickname);
					username_title.setText(nickname);
					jobtext.setText(data.getString("job"));
					String[] lables;
					lables=data.getString("tag").split("\\|");
					if(lables.length==3)
					{
						lable1.setText(lables[0]);
						lable2.setText(lables[1]);
						lable3.setText(lables[2]);
					}
					else if(lables.length==2)
					{
						lable1.setText(lables[0]);
						lable2.setText(lables[1]);
					}
					else if (lables.length==1)
					{
						if(lables[0].equals("")){}
						else
						{
						lable1.setText(lables[0]);
						}
					}
					collection_num.setText(data.getString("stored"));
					if(data.getString("online").equals("0"))
					{
						isonline.setTextColor(Color.GRAY);
						isonline.setText("离线");
					}
					else 
					{
						
						isonline.setTextColor(Color.parseColor("#6597cf"));
						isonline.setText("在线");
						
					}
					progressBar.dismiss();					
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
			 
			    
				progressBar = new ProgressDialog(user_page_Activity.this);

				progressBar.setCancelable(true);

				progressBar.setMessage("加载中 ...");

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
			
	 public class storeTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","store"));
			param.add(new BasicNameValuePair("store_id",uid));
			param.add(new BasicNameValuePair("my_id",userinfo.getString("id", "")));
			System.out.println("my id and store id is "+userinfo.getString("id", "")+"   "+uid);
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("storeresult:" + result);             
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
						
							int i=Integer.parseInt(collection_num.getText().toString());
							i++;
							String s=""+i;
							collection_num.setText(s);
							Toast.makeText(getApplicationContext(), "收藏成功", 500).show();
							
						}
	
						
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
			 
			 
			 
		 }
	 }
			
			
	 public class removestoreTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","remove_store"));
			param.add(new BasicNameValuePair("store_id",uid));
			param.add(new BasicNameValuePair("my_id",userinfo.getString("id", "")));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("remove_result:" + result);             
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
			 JSONObject data;
			try {
				data = new JSONObject(result);
				if(data.getString("state").equals("1"))
				{
				
					int i=Integer.parseInt(collection_num.getText().toString());
					i--;
					String s=""+i;
					collection_num.setText(s);
					Toast.makeText(getApplicationContext(), "取消成功", 500).show();
					
				}
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
				

				
			 
			
		 }
	 }
		
	 public class getstoreTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","getstore"));
		
			param.add(new BasicNameValuePair("uid",userinfo.getString("id", "")));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("getstore_result:" + result);             
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
			 JSONObject data;
			try {
				data = new JSONObject(result);
				String []stores=data.getString("store").split("\\|");
				int i=0;
				System.out.println("testid="+uid);
				for(i=0;i<stores.length;i++)//已经收藏这个人
				{
					
					if(stores[i].equals(uid))
					{
						collection.setBackgroundColor(Color.GRAY);
						collection.setText("取消收藏");
					
					}	
				}
				if(i>stores.length)//未收藏了这个人
				{
					collection.setBackgroundColor(Color.parseColor("#aaddaf"));
					collection.setText("收藏");		
				}	
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		 }
	 }
			
			
			
			
			
			
	
	
	
	 
}
