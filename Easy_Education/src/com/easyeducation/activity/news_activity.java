package com.easyeducation.activity;

import com.hwd.cw.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class news_activity  extends Activity{
	
	
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.news_detail_xml);
	}

}
