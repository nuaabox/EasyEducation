package com.easyeducation.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BasePage {
   private View view;
   public Context ct;
/**
    * 1 。画界面
    * 2    初始化数�?
    */
	public BasePage(Context ct) {
		this.ct = ct;
		LayoutInflater inflater = (LayoutInflater)ct.getSystemService(
			      Context.LAYOUT_INFLATER_SERVICE);
		view = initView(inflater);
		
	}
	public  View getRootView(){
    	return view;
    }
	public abstract View initView(LayoutInflater inflater);
    
	public abstract void initData();
}
