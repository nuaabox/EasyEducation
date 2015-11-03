package com.hwd.cw.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class conect_usActivity extends Activity {
	
	private ImageView back_btn;
	private TextView sent_btn;
	private EditText sent_text;
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.conect_us_xml);
	        ExitApplication.getInstance().addActivity(this);
	        back_btn=(ImageView)this.findViewById(R.id.conect_back_main);
	        sent_btn=(TextView)this.findViewById(R.id.contact_us);
	        sent_text=(EditText)this.findViewById(R.id.conect_text);
	        
	        back_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
					conect_usActivity.this.finish();
				}
	        	
	        });
	 		}
}
