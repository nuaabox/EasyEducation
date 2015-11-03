package com.hwd.cw.test;

import cn.smssdk.SMSSDK;
import io.rong.imkit.RongIM;
import android.app.Application;

public class MyApp extends Application{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
	try
	{
		RongIM.init(this);
		SMSSDK.initSDK(this, "ae76b1166d70", "8f9bad769ce69be6b0cc2205dc452fda");
	}
	catch(Exception x)
	{
		x.printStackTrace();
	}
	
	
	  
	}	

}
