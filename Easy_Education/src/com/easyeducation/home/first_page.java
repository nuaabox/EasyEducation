package com.easyeducation.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


import com.easyeducation.MainActivity;
import com.easyeducation.activity.news_activity;
import com.easyeducation.activity.userpage_activity;
import com.easyeducation.base.BasePage;
import com.easyeducation.util.RoundImageView;
import com.easyeducation.view.LazyViewPager;


import com.hwd.cw.test.R;
import com.hwd.cw.test.collection_Activity.MyOnPageChangeListener;
import com.hwd.cw.test.collection_Activity.MyPagerAdapter;
import com.hwd.cw.test.collection_Activity.MycoAdspter.Zujian;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
public class first_page extends BasePage {
	public first_page(Context ct) {
		super(ct);
		initData();
		// TODO 鑷姩鐢熸垚鐨勬瀯閫犲嚱鏁板瓨鏍�?
	}

	
	private View first_view;
	//news viewpager相关数据
	private ViewPager advPager;
	private ImageView []imageViews;
	private ImageView imageView;
	private boolean isContinue=true;
	private AtomicInteger what = new AtomicInteger(0);  
	//news viewpager相关数据
	//推送viewpager相关数据
	private ViewPager recomdViewPager;
	private TextView todaynews;
	private TextView todayfocus;
	private View focus_view;
	private View todaynews_view;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageView cursor;
	List listViews;
	
	
	//
	//listView相关数据
	List<Map<String, Object>> newslist=new ArrayList<Map<String,Object>>();  
	List<Map<String, Object>> focuslist=new ArrayList<Map<String,Object>>();  
	ListView newlistview;
	ListView focuslitview;
	//listView相关数据
	public View initView(LayoutInflater inflater) {
	 first_view= inflater.inflate(R.layout.firstpage, null);
	 cursor=(ImageView)first_view.findViewById(R.id.firstpage_move_bar);
	 initnewsViewPager();
	 initRecommondViewPager();
	 return first_view;
	
	}



	@Override
	public void initData() {
		// TODO 自动生成的方法存�?
		set_list_data();
		
		
		
	}
	
	//viewpager相关方法{
		//初始化viewpager
	 private void initnewsViewPager() {  
         advPager = (ViewPager)first_view.findViewById(R.id.firstpage_news_pager);  
         
         
         ViewGroup group = (ViewGroup)first_view.findViewById(R.id.firstpage_news_pager);  
         // 这里存放的是四张广告背景  
         
         List<View> advPics = new ArrayList<View>();  
   
         ImageView img1 = new ImageView(ct);  
         img1.setBackgroundResource(R.drawable.bigimg1);  
         advPics.add(img1);  
         
         ImageView img2 = new ImageView(ct);  
         img2.setBackgroundResource(R.drawable.bigimg2);  
         advPics.add(img2);  
         
         ImageView img3 = new ImageView(ct);  
         img3.setBackgroundResource(R.drawable.bigimg3);  
         advPics.add(img3);  
          
         ImageView img4 = new ImageView(ct);  
         img4.setBackgroundResource(R.drawable.bigimg4);  
         advPics.add(img4);  
         
         ImageView img5 = new ImageView(ct);  
         img5.setBackgroundResource(R.drawable.bigimg5);  
         advPics.add(img5); 
         
         AdvAdapter advpter=new AdvAdapter(advPics);
 	     advPager.setAdapter(advpter);
         
         // 对imageviews脚标进行填充  
         imageViews = new ImageView[5];  
         // 初始化小图标，自动滚动图片右下角的星星  
         // 如果需要修改星星的图片修改drawable文件即可  
         for (int i = 0; i < 5; i++) {  
             imageView = new ImageView(ct);  
             imageView.setLayoutParams(new LayoutParams(20, 20));  
             imageView.setPadding(5, 5, 5, 5);  
             imageViews[i] = imageView;  
             if (i == 0) {  
                 imageViews[i]  
                         .setBackgroundResource(android.R.drawable.btn_star_big_on);  
             } else {  
                 imageViews[i]  
                         .setBackgroundResource(android.R.drawable.btn_star_big_off);  
             }  
             group.addView(imageViews[i]);  
         }      
         
         advPager.setOnPageChangeListener(new GuidePageChangeListener());  
         advPager.setOnTouchListener(new OnTouchListener() {  
   
             public boolean onTouch(View v, MotionEvent event) {  
                 switch (event.getAction()) { 
                 case MotionEvent.ACTION_DOWN:              	 
                 	/*
                 	if(clickcount==0)
                 	{   clickcount++;
                 		firClick = System.currentTimeMillis();	        					
     				} else if (clickcount == 1){
     					secClick = System.currentTimeMillis();
     					if(secClick - firClick < 500){		
     						//双击事件
     						
     						Intent intent=new Intent();
     						intent.setClass(MainActivity.this, news_detail_Activity.class);
     						startActivity(intent);
     					}
     					clickcount=0;
     					firClick=0;
     					secClick=0;
     				}
     					*/	            	   	
                 case MotionEvent.ACTION_MOVE:  
                     isContinue = false;  
                     break;  
                 case MotionEvent.ACTION_UP:  
                     isContinue = true; 
                    
                     break;  
                 default:  
                     isContinue = true;  
                     break;  
                 }  
                 return false;  
             }  
         });  
         /*
         new Thread(new Runnable() {  	      
             @Override  
             public void run() {  
                 while (true) {  
                     if (isContinue) {  
                         viewHandler.sendEmptyMessage(what.get());  
                         whatOption();  
                     }  
                 }  
             }  
   
         }).start();  
         */
     } 

 	private void whatOption() {  
         what.incrementAndGet();  
         if (what.get() > imageViews.length - 1) {  
             what.getAndSet(0);  
         }  
         try {  
             Thread.sleep(3000);  
         } catch (InterruptedException e) {  
   
         }  
     }  
 	
 	
 	 private final Handler viewHandler = new Handler() {  
 		  
 	        @Override  
 	        public void handleMessage(Message msg) {  
 	            advPager.setCurrentItem(msg.what);  
 	            super.handleMessage(msg);  
 	        }  
 	  
 	    };  
 	  
 	    private final class GuidePageChangeListener implements OnPageChangeListener {  
 	  
 	        public void onPageScrollStateChanged(int arg0) {  
 	        	
 	        	
 	        	
 	  
 	        }  
 	  
 	       
 	        public void onPageScrolled(int arg0, float arg1, int arg2) {  
 	        	
 	  
 	        }  
 	  
 	        @Override  
 	        public void onPageSelected(int arg0) { 
 	        	
 	        	
 	        	switch (arg0)
 	        	{
 	        		
 	        	}
 	        	
 	        	
 	        	
 	        	
 	        	
 	            what.getAndSet(arg0%5);  
 	            for (int i = 0; i < imageViews.length; i++) {  
 	                imageViews[arg0]  
 	                        .setBackgroundResource(android.R.drawable.btn_star_big_on);  
 	                if (arg0 != i) {  
 	                    imageViews[i]  
 	                            .setBackgroundResource(android.R.drawable.btn_star_big_off);  
 	                }  
 	            }  
 	  
 	        }  
 	  
 	    }  
 	  
	
	 private final class AdvAdapter extends PagerAdapter {  
	        private List<View> views = null;  
	  
	        public AdvAdapter(List<View> views) {  
	            this.views = views;  
	        }  
	  
	        
	        public void destroyItem(View arg0, int arg1, Object arg2) {  
	            ((ViewPager) arg0).removeView(views.get(arg1%5));  
	        }  
	  
	        public void finishUpdate(View arg0) {  
	  
	        }  
	  
	        public int getCount() {  
	            return views.size();  
	        }  
	  
	        @Override  
	        public Object instantiateItem(View arg0,  int arg1) {  
	        
	        	View view1=views.get(arg1);
	            ((ViewPager) arg0).addView(view1, 0);  
	            final int positon=arg1;
	            view1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO 自动生成的方法存根								
					}            	
	            });
	            return view1 ;
	           
	        }  
	  
	        @Override  
	        public boolean isViewFromObject(View arg0, Object arg1) {  
	            return arg0 == arg1;  
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
	
	 //viewpager相关方法 与类 }
	 
	 @SuppressWarnings("unchecked")
	private void initRecommondViewPager()
	 {
		 recomdViewPager=(ViewPager)first_view.findViewById(R.id.firstpage_recommd_pager);
		 todaynews=(TextView)first_view.findViewById(R.id.today_news);
		 todayfocus=(TextView)first_view.findViewById(R.id.my_focus);
		
		 todaynews.setOnClickListener(new OnClickListener(){

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				recomdViewPager.setCurrentItem(0);
				todaynews.setTextColor(Color.parseColor("#fb7299"));
				todayfocus.setTextColor(Color.BLACK);
			} 
		 });
		 todayfocus.setOnClickListener(new OnClickListener(){

				@SuppressLint("ResourceAsColor")
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					recomdViewPager.setCurrentItem(1);
					todayfocus.setTextColor(Color.parseColor("#fb7299"));
					todaynews.setTextColor(Color.BLACK);
				} 
			 });
		 
		 
		 
		  LayoutInflater mInflater = LayoutInflater.from(ct);  
		  
		  focus_view=mInflater.inflate(R.layout.firstpage_focus_list, null);
		  todaynews_view=mInflater.inflate(R.layout.firstpage_todaynews_list, null);
		  
		  newlistview=(ListView) todaynews_view.findViewById(R.id.first_todaynews_list);
		  focuslitview=(ListView)focus_view.findViewById(R.id.first_foucus_list);
		  
		
		  
		  
		listViews = new ArrayList<View>();
     
	    listViews.add(todaynews_view);
	    listViews.add(focus_view);		        
	    recomdViewPager.setAdapter(new MyPagerAdapter(listViews));
	    recomdViewPager.setCurrentItem(0);
	    todayfocus.setTextColor(Color.BLACK);
	    InitImageView();
	    recomdViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		 
	 }
	 public class MyPagerAdapter extends PagerAdapter {
	        public List<View> mListViews;

	        public MyPagerAdapter(List<View> mListViews) {
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
	    /**
	     * 初始化动画
	*/
	    private void InitImageView() {	   
	    	
	        bmpW = BitmapFactory.decodeResource(ct.getResources(), R.drawable.co_move_bar)
	                .getWidth();// 获取图片宽度
	        DisplayMetrics dm = new DisplayMetrics();	        
	        WindowManager mWm = (WindowManager)ct.getSystemService(Context.WINDOW_SERVICE);	
	        
	        mWm.getDefaultDisplay().getMetrics(dm);
	        int screenW = dm.widthPixels;// 获取分辨率宽度
	        offset = screenW/2 -bmpW;///(screenW / 3 - bmpW)/2 ;// 计算偏移量
	        Matrix matrix = new Matrix();
	        if(recomdViewPager.getCurrentItem()==0)
	        {
	        matrix.postTranslate(offset, 0);
	        }
	        else 
	        {
	        	matrix.postTranslate(bmpW-offset, 0);  	
	        }
	        
	        
	        
	        cursor.setImageMatrix(matrix);// 设置动画初始位置
	    }
	    /**
	     * 页卡切换监听
	*/
	    public class MyOnPageChangeListener implements OnPageChangeListener {

	        int one = offset  + bmpW;// 页卡1 -> 页卡2 偏移量
	        int two = one * 2;// 页卡1 -> 页卡3 偏移量

	        @Override
	        public void onPageSelected(int arg0) {
	            Animation animation = null;
	           if(arg0==0)
	           {
	        	   todaynews.setTextColor(Color.parseColor("#fb7299"));
				   todayfocus.setTextColor(Color.BLACK); 
	        	   
	           }
	           else if(arg0==1)
	           {
	        	   todayfocus.setTextColor(Color.parseColor("#fb7299"));
					todaynews.setTextColor(Color.BLACK);
	        	   
	           }
	            
	            
	           switch (arg0) {
	            case 0:
	                if (currIndex == 1) {
	                    animation = new TranslateAnimation(one-2*offset, offset, 0, 0);
	                } 
	                break;
	            case 1:
	                if (currIndex == 0) {
	                    animation = new TranslateAnimation(offset, one-2*offset, 0, 0);
	                } 
	                break;
	           
	            }
	            currIndex = arg0;
	            animation.setFillAfter(true);// True:图片停在动画结束位置
	            animation.setDuration(300);
	            cursor.startAnimation(animation);
	        }

	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2) {
	        }

	        @Override
	        public void onPageScrollStateChanged(int arg0) {
	        }
	    }
	    	
	 
	 //listview 相关方法
	    public class MylistAdspter extends BaseAdapter {  
	        
	        private List<Map<String, Object>> data;  
	        private LayoutInflater layoutInflater;  
	        private Context context;  
	        public MylistAdspter(Context context,List<Map<String, Object>> data){  
	            this.context=context;  
	            this.data=data;  
	            this.layoutInflater=LayoutInflater.from(context);  
	        }  
	        /** 
	         * 组件集合，对应list.xml中的控件 
	         * @author Administrator 
	         */  
	        public final class Zujian{  
	            public ImageView headimg;  
	            public TextView  username;
	            public TextView texttitle;
	            public TextView nowtime;
	            
	        }  
	        @Override  
	        public int getCount() {  
	            return data.size();  
	        }  
	        /** 
	         * 获得某一位置的数据 
	         */  
	        @Override  
	        public Object getItem(int position) {  
	            return data.get(position);  
	        }  
	        /** 
	         * 获得唯一标识 
	         */  
	        @Override  
	        public long getItemId(int position) {  
	            return position;  
	        }  
	      
	        @Override  
	        public View getView(int position, View convertView, ViewGroup parent) {  
	            Zujian zujian=null;  
	            if(convertView==null){  
	                zujian=new Zujian();  
	                //获得组件，实例化组件  
	                convertView=layoutInflater.inflate(R.layout.firstpage_list, null);  
	                zujian.headimg=(ImageView)convertView.findViewById(R.id.first_list_headimg);
	                zujian.username=(TextView)convertView.findViewById(R.id.first_list_username);
	                zujian.texttitle=(TextView)convertView.findViewById(R.id.first_list_newstitle);
	                zujian.nowtime=(TextView)convertView.findViewById(R.id.first_list_newstime);
	                convertView.setTag(zujian);  
	            }else{  
	                zujian=(Zujian)convertView.getTag();  
	            }  
	            //绑定数据  	            
	            zujian.username.setText((String)data.get(position).get("username"));
	            
	            zujian.texttitle.setText((String)data.get(position).get("texttitle"));
	            
	            zujian.nowtime.setText((String)data.get(position).get("nowtime"));
	            
	            Bitmap bitmap=(Bitmap)data.get(position).get("headimg");
	            if(bitmap!=null)
	            {
	            zujian.headimg.setImageBitmap((Bitmap)data.get(position).get("headimg"));
	            zujian.headimg.setTag((Bitmap)data.get(position).get("headimg"));
	            }
	            else zujian.headimg.setBackgroundResource(R.drawable.lst_usrimg1);	      
	            zujian.username.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO 自动生成的方法存根
						Intent intent=new Intent();
						intent.setClass(ct, userpage_activity.class);
						ct.startActivity(intent);
						
					}
	            });
	            zujian.texttitle.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO 自动生成的方法存根
						Intent intent=new Intent();
						intent.setClass(ct, news_activity.class);
						ct.startActivity(intent);
						
					}
	            });
	            
	            
	            
	            return convertView;  
	        }  
  
	    }  
	  
	 public void set_list_data()
	 {
		 for(int i=0;i<10;i++)
		 {
			 Map<String, Object> map=new HashMap<String, Object>();  
			 map.put("username", "易教APP团队");
			 map.put("texttitle", "理综试卷的一百个小技巧");
			 map.put("nowtime","今天下午3.10");
			 newslist.add(map);		 
		 }
		 MylistAdspter newsAdapter=new MylistAdspter(ct,newslist);
		 newlistview.setAdapter(newsAdapter);
		 
		 for(int i=0;i<10;i++)
		 {
			 Map<String, Object> map=new HashMap<String, Object>();  
			 map.put("username", "南京航空航天大学");
			 map.put("texttitle", "C语言程序设计技巧");
			 map.put("nowtime","今天下午4.10");
			 focuslist.add(map);		 
		 }
		 MylistAdspter focussAdapter=new MylistAdspter(ct,focuslist);
		 focuslitview.setAdapter(focussAdapter);
		 
		 
		 
		 
	 }
	 
	 
	 
	 
	 
	 
}
