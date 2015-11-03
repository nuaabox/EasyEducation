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

import com.google.gson.JsonObject;
import com.hwd.cw.test.MainActivity.MyAdspter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class univercity_page_Activity extends Activity{
	
	private ImageView univercity_back_btn;
	private TextView univercity_collection_num;
	private TextView univercity_collection_btn;
	private ListView univercity_recommond_list;
	private ImageView univercitypage_head;
	private String name;
	 public static Map<String,String>hotusername_list;
	private ProgressDialog progressBar;
	private SharedPreferences userinfo;
	private String unid;
	private Drawable drawable;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.univercity_page_xml);
        hotusername_list=new HashMap<String,String>();
        Intent intent=this.getIntent();
        name=intent.getStringExtra("name");
        unid=intent.getStringExtra("unid");
        univercitypage_head =(ImageView)this.findViewById(R.id.univercitypage_head);
        ExitApplication.getInstance().addActivity(this);
        
        if(intent.getStringExtra("orgin").equals("collection_Activity"))
        {
        	drawable=collection_Activity.pic_list.get(name);
        }
        
        else if(intent.getStringExtra("orgin").equals("search_Activity"))
        {
        	 drawable=search_Activity.search_pic_list.get(name);
        }
        univercitypage_head.setBackground(drawable);
        
        userinfo=getSharedPreferences("user", MODE_PRIVATE);
        univercity_back_btn=(ImageView)this.findViewById(R.id.univercity_back_main);
        univercity_collection_num=(TextView)this.findViewById(R.id.univercity_collection_num);
        univercity_collection_btn=(TextView)this.findViewById(R.id.univercitycollection_btn);
        univercity_recommond_list=(ListView)this.findViewById(R.id.univercitypage_list);
        get_univercity_info_Task getuniinfo_task=new get_univercity_info_Task();
        getuniinfo_task.executeOnExecutor(Executors.newCachedThreadPool(),"");
        
        get_hotuser_Task gethotuser=new get_hotuser_Task();
        gethotuser.executeOnExecutor(Executors.newCachedThreadPool(),"");
        
        get_unistore_Task getunistore=new get_unistore_Task();
        getunistore.executeOnExecutor(Executors.newCachedThreadPool(),"");
        univercity_recommond_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				Intent intent=new Intent();
				String nickname=((TextView)arg1.findViewById(R.id.recommond_name)).getText().toString();

				Bitmap bitmap=(Bitmap) ((ImageView)arg1.findViewById(R.id.recommond_head)).getTag();
				intent.putExtra("headimg",bitmap);
				intent.putExtra("nickname",nickname);
				intent.putExtra("origin", "univercity_page_Activity");		
				intent.setClass(univercity_page_Activity.this, user_page_Activity.class);
				startActivity(intent);		
			}
        	
        	
        	
        });
        univercity_back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				univercity_page_Activity.this.finish();
			}
        });
        
        univercity_collection_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				
				TextView arg0=(TextView)v;
				
				if(arg0.getText().toString().equals("收藏"))
				{
					store_uni_Task storetask=new store_uni_Task();
					storetask.executeOnExecutor(Executors.newCachedThreadPool(),"");	
				int i=Integer.parseInt(univercity_collection_num.getText().toString());
				i++;
				String s=""+i;
				univercity_collection_num.setText(s);
				}
				else
				{
					
						removestore_uni_Task removestoretask=new removestore_uni_Task();
						removestoretask.executeOnExecutor(Executors.newCachedThreadPool(),"");	
					int i=Integer.parseInt(univercity_collection_num.getText().toString());
					i--;
					String s=""+i;
					univercity_collection_num.setText(s);
					
					
				}
				
			}   	
        });
        
        }
	  public class MyAdspter extends BaseAdapter {  
	        
	        private List<Map<String, Object>> data;  
	        private LayoutInflater layoutInflater;  
	        private Context context;  
	        public MyAdspter(Context context,List<Map<String, Object>> data){  
	            this.context=context;  
	            this.data=data;  
	            this.layoutInflater=LayoutInflater.from(context);  
	        }  
	        /** 
	         * 锟斤拷锟斤拷锟斤拷希锟斤拷锟接ist.xml锟叫的控硷拷 
	         * @author Administrator 
	         */  
	        public final class Zujian{  
	            public ImageView head_img;  
	            public TextView name;
	            public TextView job;
	            public TextView lable1;  
	            public TextView lable2;  
	            public TextView lable3;  
	        }  
	        @Override  
	        public int getCount() {  
	            return data.size();  
	        }  
	        /** 
	         * 锟斤拷锟侥骋晃伙拷玫锟斤拷锟斤拷锟� 
	         */  
	        @Override  
	        public Object getItem(int position) {  
	            return data.get(position);  
	        }  
	        /** 
	         * 锟斤拷锟轿ㄒ伙拷锟绞� 
	         */  
	        @Override  
	        public long getItemId(int position) {  
	            return position;  
	        }  
	      
	        @SuppressLint("NewApi")
			@Override  
	        public View getView(int position, View convertView, ViewGroup parent) {  
	            Zujian zujian=null;  
	            if(convertView==null){  
	                zujian=new Zujian();  
	                //锟斤拷锟斤拷锟斤拷锟斤拷实锟斤拷锟斤拷锟斤拷锟�  
	                convertView=layoutInflater.inflate(R.layout.user_item, null);  
	                zujian.head_img=(ImageView)convertView.findViewById(R.id.recommond_head);  
	               zujian.name=(TextView)convertView.findViewById(R.id.recommond_name);  
	               zujian.job=(TextView)convertView.findViewById(R.id.recommond_job);  
	                zujian.lable1=(TextView)convertView.findViewById(R.id.lable1);  
	                zujian.lable2=(TextView)convertView.findViewById(R.id.lable2);  
	                zujian.lable3=(TextView)convertView.findViewById(R.id.lable3);  
	                convertView.setTag(zujian);  
	            }else{  
	                zujian=(Zujian)convertView.getTag();  
	            }  
	            //锟斤拷锟斤拷锟斤拷  
	        
	            zujian.name.setText((String)data.get(position).get("name"));  
	            zujian.job.setText((String)data.get(position).get("job"));  
	            zujian.lable1.setText((String)data.get(position).get("lable1"));  
	            zujian.lable2.setText((String)data.get(position).get("lable2"));  
	            zujian.lable3.setText((String)data.get(position).get("lable3"));  
	            if((Bitmap)data.get(position).get("headimg")!=null)
	            {
	            	zujian.head_img.setBackground(null);
	            	zujian.head_img.setTag(null);
		            zujian.head_img.setImageBitmap((Bitmap)data.get(position).get("headimg"));
		            zujian.head_img.setTag((Bitmap)data.get(position).get("headimg"));
	            }
	            else
	            {
	            zujian.head_img.setImageBitmap(null);
	            zujian.head_img.setBackgroundResource(R.drawable.lst_usrimg1);
	            }
	            return convertView;  
	        }  
	      
	    }  
	
	
	 public class get_univercity_info_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","get_univercity_info"));
			param.add(new BasicNameValuePair("name",name));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("get univercity info result:" + result);             
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
				univercity_collection_num.setText(data.getString("stored"));
				((TextView)findViewById(R.id.univercitypage_name)).setText(data.getString("name"));
				String detail=data.getString("intro");
				if(!detail.equals("null"))
				((TextView)findViewById(R.id.univercitypage_detail)).setText(detail);
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		 }
	 }
	 public class get_hotuser_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","get_univercity_hotuser"));
			param.add(new BasicNameValuePair("name",name));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("get hotuser result:" + result);             
	            } 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 							
			return result;
		} 
		 @SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		@Override  
	        protected void onPostExecute(String result) {  
			 JSONObject data;
			try {
				data = new JSONObject(result);
				if(data.getString("state").equals("1"))
				{
				int n=Integer.parseInt(data.getString("n"));
				JSONObject array=new JSONObject(data.getString("list"));
				ArrayList hotuserlist=new ArrayList<Map<String,Object>>();  
		        for (int i = 1; i < n+1; i++) {  
		            Map<String, Object> map=new HashMap<String, Object>();  
		            String item=""+i;
		            if(!array.getJSONObject(item).getString("headsrc").equals(""))
		            {
		            	byte imgbyte[]= Base64.decode(array.getJSONObject(item).getString("headsrc"), Base64.DEFAULT);
		            	Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
		            	if(bitmap!=null)
		            	{
			            map.put("headimg", bitmap);
		            	}
		            }
		            map.put("name", array.getJSONObject(item).getString("nickname"));  		            
		            map.put("job", array.getJSONObject(item).getString("job"));
		            String []lables;		         
		            lables=array.getJSONObject(item).getString("tag").split("\\|");
		            hotusername_list.put(array.getJSONObject(item).getString("nickname"),
		            		array.getJSONObject(item).getString("username"));
		            if(lables.length==3)
		            {
		            map.put("lable1", lables[0]);
		            map.put("lable2", lables[1]);
		            map.put("lable3", lables[2]);   
		            }
		            else if(lables.length==2)
		            {
		            	 map.put("lable1", lables[0]);
				         map.put("lable2", lables[1]);	
		            }
		            else 
		            {		
		            	map.put("lable1", lables[0]);
		            }
		            
		            hotuserlist.add(map);  
		        }  
		        univercity_recommond_list.setAdapter(new MyAdspter(univercity_page_Activity.this, hotuserlist));
				}
		        progressBar.dismiss();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(univercity_page_Activity.this);

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
	 //get stored uinivercity info.used to check is stored;
	 public class get_unistore_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","get_my_univercity"));
			param.add(new BasicNameValuePair("uid",userinfo.getString("id", "")));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("get my univercity result:" + result);             
	            } 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 							
			return result;
		} 
		 @SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		@Override  
	        protected void onPostExecute(String result) {  
			 JSONObject data;
			try {
				data = new JSONObject(result);
				if(data.getString("state").equals("1"))
				{
					String[] ustore=data.getString("ustore").split("\\|");
					int n=ustore.length;
					int i=0;
					System.out.println("unid="+unid);
					
					for( i=0;i<n;i++)
					{
						System.out.println("ustore="+ustore[i]);
						if(ustore[i].equals(unid))
						{					
							univercity_collection_btn.setText("取消收藏");
							univercity_collection_btn.setBackgroundColor(Color.GRAY);
							break;
						}			
					}
					if(i==n)
					{
						univercity_collection_btn.setText("收藏");
						univercity_collection_btn.setBackgroundColor(Color.parseColor("#aaddaf"));
						
					}
				
				}
				
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		 }
		 @Override  
		    protected void onPreExecute() {  
				
		    }  
		  
		 @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        int vlaue = values[0];  
		        progressBar.setProgress(vlaue);  
		    }  
	 }
	
	 public class store_uni_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","store_univercity"));
			param.add(new BasicNameValuePair("uid",userinfo.getString("id", "")));
			param.add(new BasicNameValuePair("univercity_id",unid));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("store univercity result:" + result);             
	            } 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 							
			return result;
		} 
		 @SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		@Override  
	        protected void onPostExecute(String result) {  
			 JSONObject data;
			 try {
				data=new JSONObject(result);
				if(data.getString("state").equals("1"))
				 {		
				 get_unistore_Task getunistore=new get_unistore_Task();
				 getunistore.executeOnExecutor(Executors.newCachedThreadPool(),"");
				 Toast.makeText(getApplicationContext(), "收藏成功", 500).show();
				 }
			} catch (JSONException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			 
			
		 }
		 @Override  
		    protected void onPreExecute() {  
				
		    }  
		  
		 @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        int vlaue = values[0];  
		        progressBar.setProgress(vlaue);  
		    }  
	 }
	
	 public class removestore_uni_Task extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","remove_store_univercity"));
			param.add(new BasicNameValuePair("uid",userinfo.getString("id", "")));
			param.add(new BasicNameValuePair("univercity_id",unid));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("remove store univercity result:" + result);             
	            } 
	        } catch (ClientProtocolException e) { 
	            e.printStackTrace(); 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 							
			return result;
		} 
		 @SuppressWarnings("unchecked")
		@SuppressLint("ShowToast")
		@Override  
	        protected void onPostExecute(String result) {  
			 JSONObject data;

			 try {
				data=new JSONObject(result);
				if(data.getString("state").equals("1"))
				 {		
				 get_unistore_Task getunistore=new get_unistore_Task();
				 getunistore.executeOnExecutor(Executors.newCachedThreadPool(),"");
				 Toast.makeText(getApplicationContext(), "取消收藏成功", 500).show();
				 }
				
			} catch (JSONException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		 }
		 @Override  
		    protected void onPreExecute() {  
				
		    }  
		  
		 @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        int vlaue = values[0];  
		        progressBar.setProgress(vlaue);  
		    }  
	 }
	

		 

		 
		 
		 
	

}
