package com.hwd.cw.test;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class news_detail_Activity extends Activity{

	private ImageView news_back_btn;
	private TextView news_title;
	private TextView news_date;
	private TextView news_author;
	private ImageView news_img;
	private TextView news_text;
	
	 @SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.news_detail_xml);
	        ExitApplication.getInstance().addActivity(this);
	        news_back_btn=(ImageView)this.findViewById(R.id.news_back_main);
	        
	        
	        news_title=(TextView)this.findViewById(R.id.news_title);
	        news_date=(TextView)this.findViewById(R.id.news_date);
	        news_author=(TextView)this.findViewById(R.id.news_author);
	        news_text=(TextView)this.findViewById(R.id.news_text);
	        news_img=(ImageView)this.findViewById(R.id.news_img);
	        
	        Intent intent=this.getIntent();
	        int n=intent.getExtras().getInt("position");
	        news_img.setBackground(MainActivity.news_pic_list.get(n));
	        Map map=MainActivity.news_list.get(n);
	        
	        news_title.setText((String) map.get("headline"));
	        news_date.setText((String) map.get("time"));
	        news_author.setText((String)map.get("author"));
	        news_text.setText((String)map.get("content"));
	        
	        
	        
	        
	        
	        
	        
	        news_back_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					news_detail_Activity.this.finish();
					
				}
	        	
	        	
	        });
	 }
}
