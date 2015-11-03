package com.easyeducation.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


import com.easyeducation.base.BasePage;
import com.easyeducation.view.LazyViewPager;

import com.hwd.cw.test.R;
public class third_page extends BasePage {
	public third_page(Context ct) {
		super(ct);
		initData();
		// TODO 鑷姩鐢熸垚鐨勬瀯閫犲嚱鏁板瓨鏍�?
	}


	private View download_view;

	

	public View initView(LayoutInflater inflater) {
		download_view = inflater.inflate(R.layout.firstpage, null);
		
		return download_view;
	}



	@Override
	public void initData() {
		// TODO 自动生成的方法存�?
		
	}
	

	

}
