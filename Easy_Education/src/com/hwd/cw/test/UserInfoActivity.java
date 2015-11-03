package com.hwd.cw.test;

import java.io.ByteArrayOutputStream;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity{

	private ImageView edit_back_btn;
	private EditText username_edit;
	private TextView userinfo_edit;
	private TextView username_text;
	private TextView userdetail_text;//job
	private EditText userdetail_edit;
	private TextView lable1_text;
	private EditText lable1_edit;
	private TextView lable2_text;
	private EditText lable2_edit;
	private TextView lable3_text;
	private EditText lable3_edit;
	private ImageView user_head_edit;
	private TextView collection_num;
	private TextView my_detail_text;//个人详细
	private EditText my_detail_edit;
	private byte[] imagebyte;
	private ImageView userhead;
	private 
	static int open_times=0;//share的提交次数
	static int img_open_times=0;
	SharedPreferences  editSharedPreferences;
	SharedPreferences  userinfoPreferences;
	String lable;
	String []lables;
	 @SuppressLint("CutPasteId")
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.edituserinfo_xml);
	        lables=new String[3];
	        lables[0]=" ";
	        lables[1]=" ";
	        lables[2]=" ";
	        editSharedPreferences=this.getSharedPreferences("edit", Activity.MODE_PRIVATE);
	        userinfoPreferences=this.getSharedPreferences("user", MODE_PRIVATE);
	        ExitApplication.getInstance().addActivity(this);
	        edit_back_btn=(ImageView)this.findViewById(R.id.edit_back_main);
	        username_edit=(EditText)this.findViewById(R.id.username_edit);
	        userinfo_edit=(TextView)this.findViewById(R.id.user_info_btn);
	        username_text=(TextView)this.findViewById(R.id.username_text);
	        userdetail_text=(TextView)this.findViewById(R.id.userdetail_text);
	        userdetail_edit=(EditText)this.findViewById(R.id.userdetail_edit);
	        lable1_text=(TextView)this.findViewById(R.id.lable1_text);
	        lable1_edit=(EditText)this.findViewById(R.id.lable1_edit);
	        lable2_text=(TextView)this.findViewById(R.id.lable2_text);
	        lable2_edit=(EditText)this.findViewById(R.id.lable2_edit);
	        lable3_text=(TextView)this.findViewById(R.id.lable3_text);
	        lable3_edit=(EditText)this.findViewById(R.id.lable3_edit);
	        collection_num=(TextView)this.findViewById(R.id.mycollect_num);
	        my_detail_text=(TextView)this.findViewById(R.id.my_detail_text);
			my_detail_edit=(EditText)this.findViewById(R.id.my_detail_edit);	
			userhead=(ImageView)this.findViewById(R.id.change_user_head);
			
			
			byte imgbyte1[]= Base64.decode(userinfoPreferences.getString("headsrc",""), Base64.DEFAULT);
		 	Bitmap bitmap2 = BitmapFactory.decodeByteArray(imgbyte1, 0, imgbyte1.length);
		 	if(bitmap2!=null)
		 	userhead.setImageBitmap(bitmap2);
		 	else
		 	userhead.setBackgroundResource(R.drawable.lst_usrimg1);
		 	
		 	
	        username_text.setText(userinfoPreferences.getString("nickname",""));
	        userdetail_text.setText(userinfoPreferences.getString("job",""));
	        collection_num.setText(userinfoPreferences.getString("stored",""));
	        lable=userinfoPreferences.getString("tag", " | | |");
	        // myuserdetail.setText(userinfoPreferences.getString("hint",""));
	        lables=lable.split("\\|");
	        if(lables.length==3)
	        {
	        lable1_text.setText(lables[0]);
	        lable2_text.setText(lables[1]);
	        lable3_text.setText(lables[2]);
	        }
	        else if(lables.length==2)
	        {
	        	lable1_text.setText(lables[0]);
		        lable2_text.setText(lables[1]);
	        	
	        }
	        else if(lables.length==1)
	        {
	        	lable1_text.setText(lables[0]);
	        	
	        }
	        my_detail_text.setText(userinfoPreferences.getString("hint",""));
	        
	        user_head_edit=(ImageView)this.findViewById(R.id.change_user_head);
	        
	        edit_back_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
				UserInfoActivity.this.finish();
					
					
				}	        	
	        });
	        userinfo_edit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					if(userinfo_edit.getText().equals("编辑"))
					{
					userinfo_edit.setText("完成");
					username_text.setVisibility(View.GONE);
					username_edit.setVisibility(View.VISIBLE);
					userdetail_text.setVisibility(View.GONE);
					userdetail_edit.setVisibility(View.VISIBLE);
					lable1_text.setVisibility(View.GONE);
					lable1_edit.setVisibility(View.VISIBLE);
					lable2_text.setVisibility(View.GONE);
					lable2_edit.setVisibility(View.VISIBLE);
					lable3_text.setVisibility(View.GONE);
					
					lable3_edit.setVisibility(View.VISIBLE);
					username_edit.setText(username_text.getText());
					userdetail_edit.setText(userdetail_text.getText());
					lable1_edit.setText(lable1_text.getText());
					lable2_edit.setText(lable2_text.getText());
					lable3_edit.setText(lable3_text.getText());
					
					my_detail_text.setVisibility(View.GONE);
					my_detail_edit.setVisibility(View.VISIBLE);
					my_detail_edit.setText(my_detail_text.getText().toString());
					}
					else{
						
						userinfo_edit.setText("编辑");
						username_text.setVisibility(View.VISIBLE);
						username_edit.setVisibility(View.GONE);
						username_text.setText(username_edit.getText());	
						userdetail_text.setVisibility(View.VISIBLE);
						userdetail_edit.setVisibility(View.GONE);
						lable1_text.setVisibility(View.VISIBLE);
						lable1_edit.setVisibility(View.GONE);
						lable2_text.setVisibility(View.VISIBLE);
						lable2_edit.setVisibility(View.GONE);
						lable3_text.setVisibility(View.VISIBLE);
						lable3_edit.setVisibility(View.GONE);
						username_text.setText(username_edit.getText());
						userdetail_text.setText(userdetail_edit.getText());
						lable1_text.setText(lable1_edit.getText());
						lable2_text.setText(lable2_edit.getText());
						lable3_text.setText(lable3_edit.getText());
						
						my_detail_text.setVisibility(View.VISIBLE);
						my_detail_edit.setVisibility(View.GONE);
						my_detail_text.setText(my_detail_edit.getText().toString());
						
						/*
						SharedPreferences.Editor editor=editSharedPreferences.edit();
						editor.putString("username", username_text.getText().toString());
						editor.putString("userdetail", userdetail_text.getText().toString());
						editor.putString("lable1", lable1_text.getText().toString());
						editor.putString("lable2", lable2_text.getText().toString());
						editor.putString("lable3", lable3_text.getText().toString());
						editor.putString("my_detail", my_detail_text.getText().toString());
						editor.commit();
						open_times++;
						*/
						System.out.println(editSharedPreferences.getString("username",""));
						changeuserinfoTask changetask=new changeuserinfoTask();
						changetask.executeOnExecutor(Executors.newCachedThreadPool(),"");
						
						
						
					}
				}	        	
	        });
	        user_head_edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
				    intent.addCategory(Intent.CATEGORY_OPENABLE);
				    intent.setType("image/*");
				    intent.putExtra("crop", "true");
				    intent.putExtra("aspectX", 1);
				    intent.putExtra("aspectY", 1);
				    intent.putExtra("outputX", 100);
				    intent.putExtra("outputY", 100);
				    intent.putExtra("scale", "true");
				    intent.putExtra("return-data", true);

				    startActivityForResult(intent, 0);
					
				}
	        	
	        });      
	 }
	 @SuppressLint("CommitPrefEdits")
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if(resultCode!=0)
	  {
	  Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
	  user_head_edit.setImageBitmap(cameraBitmap);
	 // SharedPreferences.Editor editor=editSharedPreferences.edit();
	 // editor.putInt("headimg", cameraBitmap.)
	  ByteArrayOutputStream out = new ByteArrayOutputStream();  
	  cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
	  imagebyte=out.toByteArray();
	  SharedPreferences.Editor editor=editSharedPreferences.edit();
	  String imgString=Base64.encodeToString(imagebyte, Base64.DEFAULT);
	  editor.putString("imagebyte", imgString);
	  editor.commit();
	  
	  SharedPreferences.Editor editor1=userinfoPreferences.edit();
	  editor1.putString("headsrc", imgString);
	  editor1.commit();
	  
	  pushpicTask pictask=new pushpicTask(imgString);
	  pictask.executeOnExecutor(Executors.newCachedThreadPool(),"");
	  
	  img_open_times++;
	  super.onActivityResult(requestCode, resultCode, data);
	  }
	 }
	 @Override 
	 protected void onResume()
	 {
		super.onResume();	
		
		    username_text.setText(userinfoPreferences.getString("nickname",""));
	        userdetail_text.setText(userinfoPreferences.getString("job",""));
	        collection_num.setText(userinfoPreferences.getString("stored",""));
	        lable=userinfoPreferences.getString("tag", " | | |");
	        // myuserdetail.setText(userinfoPreferences.getString("hint",""));
	        lables=lable.split("\\|");
	        if(lables.length==3)
	        {
	        lable1_text.setText(lables[0]);
	        lable2_text.setText(lables[1]);
	        lable3_text.setText(lables[2]);
	        }
	        else if(lables.length==2)
	        {
	        	lable1_text.setText(lables[0]);
		        lable2_text.setText(lables[1]);
	        	
	        }
	        else if(lables.length==1)
	        {
	        	lable1_text.setText(lables[0]);
	        	
	        }
	        my_detail_text.setText(userinfoPreferences.getString("hint",""));
		if(img_open_times!=0)
		{
			
		   //System.out.println("imagebyteString="+editSharedPreferences.getString("imagebyte",""));
		   byte imgbyte[]= Base64.decode(editSharedPreferences.getString("imagebyte",""), Base64.DEFAULT);
		   Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);  
		   userhead.setImageBitmap(bitmap);	     
		     
			
		}
		
		 
	 }
	 public class changeuserinfoTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 自动生成的方法存根
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","change_user_info"));
			param.add(new BasicNameValuePair("uid",userinfoPreferences.getString("id", "")));
			param.add(new BasicNameValuePair("nickname",username_text.getText().toString()));
			param.add(new BasicNameValuePair("job",userdetail_text.getText().toString()));
			param.add(new BasicNameValuePair("hint",my_detail_text.getText().toString()));
			String []lables=new String[3];
			lables[0]=lable1_text.getText().toString();
			lables[1]=lable2_text.getText().toString();
			lables[2]=lable3_text.getText().toString();
			String lable=lables[0]+"|"+lables[1]+"|"+lables[2];
			System.out.println("pushlable="+lable);
			param.add(new BasicNameValuePair("lable",lable));	
			
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	            // 设置httpPost请求参数 
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	            //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                // 第三步，使用getEntity方法活得返回结果 
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
					Editor editor=userinfoPreferences.edit();
					
					String []lables=new String[3];
					lables[0]=lable1_text.getText().toString();
					lables[1]=lable2_text.getText().toString();
					lables[2]=lable3_text.getText().toString();
					String lable=lables[0]+"|"+lables[1]+"|"+lables[2];
					

					editor.putString("nickname", username_text.getText().toString());
					editor.putString("job",userdetail_text.getText().toString());
					editor.putString("tag", lable);
					editor.putString("hint",my_detail_text.getText().toString());
					editor.commit();
					
					
					
					
					
				}
				else if(data.getString("state").equals("0"))
				{
					Toast.makeText(getApplicationContext(), "修改失败", 1000).show();
					
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
		 }
	 }
	 public class pushpicTask extends AsyncTask<Object,Integer,String>
	 {
		 String picString;
		 public pushpicTask(String s)
		 {
			 	this.picString=s;
		 }
		 
		@Override
		protected String doInBackground(Object... map) {
			// TODO 自动生成的方法存根
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","change_user_heading"));
			param.add(new BasicNameValuePair("uid", userinfoPreferences.getString("id", "")));
			param.add(new BasicNameValuePair("headimgString",picString));
		
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	            // 设置httpPost请求参数 
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	            //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                // 第三步，使用getEntity方法活得返回结果 
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("changeheadimgresult:" + result);             
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
				}
				else if(data.getString("state").equals("0"))
				{
					Toast.makeText(getApplicationContext(), "修改失败", 1000).show();
					
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}  
		 }
	 }
			
	
			
}
