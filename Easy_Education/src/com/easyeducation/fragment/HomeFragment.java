package com.easyeducation.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.easyeducation.base.BaseFragment;
import com.easyeducation.base.BasePage;
import com.easyeducation.home.Second_page;
import com.easyeducation.home.first_page;
import com.easyeducation.home.third_page;
import com.easyeducation.view.LazyViewPager;
import com.easyeducation.view.LazyViewPager.OnPageChangeListener;



import com.hwd.cw.test.R;

public class HomeFragment extends BaseFragment {
	
	private View view;
	private LazyViewPager viewpager;
	private RadioGroup main_radio;
	private int checkedId = R.id.se_first_page;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home2, null);
		viewpager = (LazyViewPager) view.findViewById(R.id.viewpager);
		main_radio = (RadioGroup) view.findViewById(R.id. myradio);
		return view;
	}

	List<BasePage> list = new ArrayList<BasePage>();;

	@Override
	public void initData(Bundle savedInstanceState) {
		
		
		list.add(new first_page(ct));
		list.add(new Second_page(ct));
		list.add(new third_page(ct));
		HomePageAdapter adapter = new HomePageAdapter(ct, list);
		viewpager.setAdapter(adapter);
		

		main_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.se_first_page:
					viewpager.setCurrentItem(0, false);
					checkedId = 0;
					break;

				case R.id.se_second_page:
					viewpager.setCurrentItem(1, false);
					checkedId = 1;
					break;
				case R.id.se_third_page:
					viewpager.setCurrentItem(2, false);
					checkedId = 2;
					break;
			
				}

			}
		});
		main_radio.check(checkedId);
	}

	public class HomePageAdapter extends PagerAdapter {
		private Context ct;
		private List<BasePage> list;

		public HomePageAdapter(Context ct, List<BasePage> list) {
			this.ct = ct;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
			((LazyViewPager) container).removeView(list.get(position)
					.getRootView());

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((LazyViewPager) container).addView(list.get(position)
					.getRootView(), 0);
			return list.get(position).getRootView();
		}

	}
}
