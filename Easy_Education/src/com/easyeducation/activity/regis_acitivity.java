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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.easyeducation.home.first_page.MyPagerAdapter;
import com.hwd.cw.test.R;
import com.hwd.cw.test.registActivity;
import com.hwd.cw.test.R.color;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class regis_acitivity extends Activity{
 
	private  Intent   myintent;
	private  TextView student_regis;
	private  TextView parent_regis;
	private  TextView teacher_regis;
	private  ViewPager regis_pager;
	private  ImageView backbtn,tri1,tri2,tri3;
	private  View student,parent,teacher;
	private  ArrayList  ViewList;
	private  int userkind=0;//0 student 1 parent 2 teacher
	private  TextView  nextbtn;
	private EditText st_studyid,st_id,st_pass,st_pass1,
					 pt_id,pt_pass,pt_pass1,
					 tc_id,tc_pass,tc_pass1;
	private ProgressDialog progressBar;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.register_activity_xml);
		initView();
		initViewPager();
		myintent=this.getIntent();
		
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				finish();
			}	
		});
		
		student_regis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
				student_regis.setTextColor(getResources().getColor(R.color.color_theme));
				parent_regis.setTextColor(Color.parseColor("#ffffff"));
				teacher_regis.setTextColor(Color.parseColor("#ffffff"));
				
				student_regis.setBackgroundColor(Color.parseColor("#fee8ef"));
				parent_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				teacher_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
			
				regis_pager.setCurrentItem(0);
				userkind=0;
					
				tri1.setVisibility(View.VISIBLE);
				tri2.setVisibility(View.INVISIBLE);
				tri3.setVisibility(View.INVISIBLE);
			}
		});
		parent_regis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				arg0.setBackgroundColor(Color.parseColor("#fee8ef"));
				student_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				teacher_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
				student_regis.setTextColor(Color.parseColor("#ffffff"));
				parent_regis.setTextColor(getResources().getColor(R.color.color_theme));
				teacher_regis.setTextColor(Color.parseColor("#ffffff"));
				
				tri1.setVisibility(View.INVISIBLE);
				tri2.setVisibility(View.VISIBLE);
				tri3.setVisibility(View.INVISIBLE);
				regis_pager.setCurrentItem(1);
				userkind=1;
			}
		});
		teacher_regis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				arg0.setBackgroundColor(Color.parseColor("#fee8ef"));
				parent_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				student_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
				student_regis.setTextColor(Color.parseColor("#ffffff"));
				parent_regis.setTextColor(Color.parseColor("#ffffff"));
				teacher_regis.setTextColor(getResources().getColor(R.color.color_theme));
				
				tri1.setVisibility(View.INVISIBLE);
				tri2.setVisibility(View.INVISIBLE);
				tri3.setVisibility(View.VISIBLE);
				regis_pager.setCurrentItem(2);
				userkind=2;
			}
		});
		
	  nextbtn.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			
			
			HashMap<String,String> map = new HashMap<String,String>();
			String phone=myintent.getStringExtra("phone");
			String idcard=null;
			String studyid=null;
			String passcode = null;
			String passcode1 = null;
			int type=userkind;
			if(userkind==0)
			{
				idcard=st_id.getText().toString();
				studyid=st_studyid.getText().toString();
				passcode=st_pass.getText().toString();
				passcode1=st_pass1.getText().toString();	
			}
			else if(userkind==1)
			{				
				idcard=pt_id.getText().toString();	
				passcode=pt_pass.getText().toString();
				passcode1=pt_pass1.getText().toString();	
				
			}
			else if(userkind==2)
			{				
				idcard=tc_id.getText().toString();	
				passcode=tc_pass.getText().toString();
				passcode1=tc_pass1.getText().toString();		
			}
			
			if(passcode.equals(passcode1)==false)
			{
				Toast.makeText(regis_acitivity.this, "两次输入密码不一致，请重新输入", 500).show();
			}
			else 
			{
				if( passcode=="" || idcard=="")
					Toast.makeText(regis_acitivity.this, "相关信息不能为空", 500).show();
				else {	
				map.put("idcard", idcard);
				map.put("passcode", passcode);
				map.put("phone", phone);
				map.put("studyid", studyid);
				regisTask task=new regisTask(type,map);
				task.execute();
				}

			}

		}});
		
		
		
		
		
		
		
		

	}
	private void initView()
	{
		student_regis=(TextView) this.findViewById(R.id.regis_student);
		parent_regis=(TextView) this.findViewById(R.id.regis_parent);
		teacher_regis=(TextView) this.findViewById(R.id.regis_teacher);
		regis_pager=(ViewPager)this.findViewById(R.id.regis_viewpage);
		backbtn=(ImageView)this.findViewById(R.id.regis_back_log);
		tri1=(ImageView)this.findViewById(R.id.regis_tri1);
		tri2=(ImageView)this.findViewById(R.id.regis_tri2);
		tri3=(ImageView)this.findViewById(R.id.regis_tri3);
		regis_pager=(ViewPager)this.findViewById(R.id.regis_viewpage);
		nextbtn=(TextView)this.findViewById(R.id.regis_newxtbtn);
		
		
	
		
	}
	@SuppressWarnings("unchecked")
	private void initViewPager()
	{
		  LayoutInflater mInflater = LayoutInflater.from(this);  
		  student=mInflater.inflate(R.layout.regis_student_xml, null);
		  teacher=mInflater.inflate(R.layout.regis_teacher_xml, null);
		  parent=mInflater.inflate(R.layout.regis_parent_xml, null);
		 
		  
		  
			st_studyid=(EditText)student.findViewById(R.id.regis_studyid);
			st_id=(EditText)student.findViewById(R.id.regis_st_idcard);
			st_pass=(EditText)student.findViewById(R.id.regis_st_passcode);
			st_pass1=(EditText)student.findViewById(R.id.regis_st_passcode1);
			
			pt_id=(EditText)parent.findViewById(R.id.regis_pt_idcard);
			pt_pass=(EditText)parent.findViewById(R.id.regis_pt_passcode);
			pt_pass1=(EditText)parent.findViewById(R.id.regis_pt_passcode1);
			
			tc_id=(EditText)teacher.findViewById(R.id.regis_tc_idcard);
			tc_pass=(EditText)parent.findViewById(R.id.regis_tc_passcode);
			tc_pass1=(EditText)parent.findViewById(R.id.regis_tc_passcode1);
		
		
			
		  
		  ViewList = new ArrayList<View>();
		  ViewList.add(student);
		  ViewList.add(parent);
		  ViewList.add(teacher);
		  
		  regis_viewgaer_Adapter adapter=new regis_viewgaer_Adapter(ViewList);
		  regis_pager.setAdapter(adapter);	
		  regis_pager.setOnPageChangeListener(new reOnPageChangeListener());
	}
	
	
	  public class reOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO 自动生成的方法存根
			
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onPageSelected(int arg0) {
			
			if(arg0==0)
			{
				student_regis.setTextColor(getResources().getColor(R.color.color_theme));
				parent_regis.setTextColor(Color.parseColor("#ffffff"));
				teacher_regis.setTextColor(Color.parseColor("#ffffff"));
				
				student_regis.setBackgroundColor(Color.parseColor("#fee8ef"));
				parent_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				teacher_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
			
				
					
				tri1.setVisibility(View.VISIBLE);
				tri2.setVisibility(View.INVISIBLE);
				tri3.setVisibility(View.INVISIBLE);
				
			}
			else if(arg0==1)
			{
				parent_regis.setBackgroundColor(Color.parseColor("#fee8ef"));
				student_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				teacher_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
				student_regis.setTextColor(Color.parseColor("#ffffff"));
				parent_regis.setTextColor(getResources().getColor(R.color.color_theme));
				teacher_regis.setTextColor(Color.parseColor("#ffffff"));
				
				tri1.setVisibility(View.INVISIBLE);
				tri2.setVisibility(View.VISIBLE);
				tri3.setVisibility(View.INVISIBLE);
			}
			else if(arg0==2)
			{
				
				teacher_regis.setBackgroundColor(Color.parseColor("#fee8ef"));
				parent_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				student_regis.setBackgroundColor(Color.parseColor("#fc8ead"));
				
				student_regis.setTextColor(Color.parseColor("#ffffff"));
				parent_regis.setTextColor(Color.parseColor("#ffffff"));
				teacher_regis.setTextColor(getResources().getColor(R.color.color_theme));
				
				tri1.setVisibility(View.INVISIBLE);
				tri2.setVisibility(View.INVISIBLE);
				tri3.setVisibility(View.VISIBLE);
				
			}
			
			
			
			
			
			
			// TODO 自动生成的方法存根
			
		}    
	    }

	
	//viewpager adapter
	 public class regis_viewgaer_Adapter extends PagerAdapter {
	        public List<View> mListViews;

	        public regis_viewgaer_Adapter(List<View> mListViews) {
	            this.mListViews = mListViews;
	        }

	        @Override
	        public void destroyItem(View arg0, int arg1, Object arg2) {
	            ((ViewPager) arg0).removeView(mListViews.get(arg1));
	        }

	        @Override
	        public void finishUpdate(View arg0) {
	        }

	        @Override
	        public int getCount() {
	            return mListViews.size();
	        }

	        @Override
	        public Object instantiateItem(View arg0, int arg1) {
	            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
	            return mListViews.get(arg1);
	        }

	        @Override
	        public boolean isViewFromObject(View arg0, Object arg1) {
	            return arg0 == (arg1);
	        }

	        @Override
	        public void restoreState(Parcelable arg0, ClassLoader arg1) {
	        }

	        @Override
	        public Parcelable saveState() {
	            return null;
	        }

	        @Override
	        public void startUpdate(View arg0) {
	        }
	    }
	 
	 
	 
	 
	 public class regisTask extends AsyncTask<Object,Integer,String>
	 {
		
		     int type; //0 st 1pt 2tc
		  	 String studyid;
			 String idcard;
			 String phone;
			 String passcode;

		public regisTask(int type ,Map<String,String>data)
		 {
			this.type=type;
			this.idcard=data.get("idcard");
			this.phone=data.get("phone");
			this.passcode=data.get("passcode");
			if(type==0)
			{
				this.studyid=data.get("studyid");
			} 
			else this.studyid="";
		 }
		 
		 
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/action.php";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			
			param.add(new BasicNameValuePair("action","logup"));
			param.add(new BasicNameValuePair("type", Integer.toString(this.type)));
			param.add(new BasicNameValuePair("idcard",this.idcard));
			param.add(new BasicNameValuePair("password",this.passcode));
			param.add(new BasicNameValuePair("phone",this.phone));
			param.add(new BasicNameValuePair("studyid",this.studyid));
			
			
			
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("regisresult:" + result);             
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
					Toast.makeText(getApplicationContext(), "注册成功", 1000).show();				
					Intent intent=new Intent();
					intent.putExtra("phone", myintent.getStringExtra("phone"));
					intent.setClass(regis_acitivity.this, login_activity.class);
					finish();
					startActivity(intent);
					
				}
				else if(data.getString("info").equals("User already exists."))
				{
					Toast.makeText(getApplicationContext(), "用户已存在", 1000).show();
					
				}
				else if(data.getString("info").equals("Phone already exists."))
				{
					Toast.makeText(getApplicationContext(), "号码已经被注册", 500).show();
					
				}
				progressBar.dismiss();
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(regis_acitivity.this);

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
 
	




