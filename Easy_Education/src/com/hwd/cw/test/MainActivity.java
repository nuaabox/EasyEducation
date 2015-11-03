package com.hwd.cw.test;



import io.rong.imkit.RCloudContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.UserInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hwd.cw.test.collection_Activity.MyuniAdspter;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final long TIME_INTERVAL = 5000;  
    // viewPager右下角的脚标图片  
    private ImageView[] imageViews = null;  
    private ImageView imageView = null;  
    private ViewPager advPager = null;  
    private AtomicInteger what = new AtomicInteger(0);  
    private boolean isContinue = true; 
    
    private ImageView show_menu;//显示菜单按钮
    private ImageView search_btn;//顶部搜索按钮
    private TextView user_name;//点击用户名进入个人信息页面
    private TextView collection;//点击我的收藏进入收藏页面
    private TextView conect;//点击进入联系我们页面
    private TextView myimopen;
    private ImageView headimg;
    
    private int clickcount=0;
  //  private SlidingMenu menu;
    double firClick=0;
    double secClick=0;
    ListView recommond_list;
    SharedPreferences userinfo;
    public static int islogin=0;//是否为在线状态
    private String recommond_result;
    private ProgressDialog progressBar;
    List<Map<String, Object>> recommond_list_data;
    public static Map<String,String>username_list;
    public static  List<View> myadvPics = new ArrayList<View>();
    public static List<Map<String,String>> news_list;
    public static List<Drawable> news_pic_list;
    UserInfo rong_userinfo;       
    private  MyAdspter list_adapter;
    private static int opentime=0;
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        userinfo=this.getSharedPreferences("user", MODE_PRIVATE);
	        ExitApplication.getInstance().addActivity(this);
	        news_list=new ArrayList<Map<String,String>>();
	        news_pic_list=new ArrayList<Drawable>();
	        
	        list_adapter=new MyAdspter(this, recommond_list_data);
	        if(islogin==0)
	        {
	        	Intent intent=new Intent();
	        	intent.setClass(MainActivity.this, login_Activity.class);
	        	this.startActivity(intent);
	        	
	        }
	        setContentView(R.layout.activity_main);
	        //侧滑菜单	       
	        initmenu();
	        //广告轮播
	        initViewPager();
	        //list
	        
	        RongIM.setGetUserInfoProvider(new RongIM.GetUserInfoProvider()  {

			    @Override
			    public UserInfo getUserInfo(final String userId) {		    
			      
			    	
			       try {
			            final   getronginfoTask task = new   getronginfoTask(userId);
			            task.setOnDataFinishedListener(new OnDataFinishedListener() {
			                 
			                @Override
			                public void onDataSuccessfully(Object data) {
			                    try {
			                    	Map map=(Map)data;
			                    	rong_userinfo=new UserInfo(userId,(String)map.get("nickname"),"data:image/png;base64,"+(String)map.get("headsrc"));				                    	
			                    	RCloudContext.getInstance().getUserInfoCache().put(userId, rong_userinfo);
			                    	
			                    } catch (Exception e) {
			                        e.printStackTrace();
			                    }
			                }
			                @Override
			                public void onDataFailed() {
			                  
			                }
			            });
			            task.executeOnExecutor(Executors.newCachedThreadPool(),"");
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			       
			       return rong_userinfo;
			    }

			}, true);
	        
	        
	        getnewsTask newsTask=new getnewsTask();
		    newsTask.executeOnExecutor(Executors.newCachedThreadPool(),"");
			  
	        recommond_list=(ListView)findViewById(R.id.recommond_list);  
	        username_list=new HashMap<String,String>();
	      
	        recommond_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根
					String nickname=((TextView)arg1.findViewById(R.id.recommond_name)).getText().toString();
					Bitmap bitmap=(Bitmap) ((ImageView)arg1.findViewById(R.id.recommond_head)).getTag();
					Intent intent=new Intent();
					intent.putExtra("nickname",nickname);
					intent.putExtra("headimg",bitmap);
					intent.putExtra("origin", "MainActivity");
					intent.setClass(MainActivity.this, user_page_Activity.class);
					startActivity(intent);
					
				}});
	        //
	        //搜索按钮点击事件
	        search_btn=(ImageView)findViewById(R.id.search_btn);
	        search_btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent=new Intent();
					intent.setClass(arg0.getContext(), search_Activity.class);
					arg0.getContext().startActivity(intent);
				}
	        	
	        	
	        	
	        });
	        //菜单按钮点击事件
	        show_menu=(ImageView)findViewById(R.id.open_menu);
	        show_menu.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					
					//menu.showMenu();
				}
	        	
	        });
	        user_name=(TextView)findViewById(R.id.user_name);
	        
	        user_name.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent=new Intent();
					intent.setClass(arg0.getContext(), UserInfoActivity.class);
					 
					
					startActivity(intent);
				}
	        	
	        });
	        collection=(TextView)findViewById(R.id.collection);
	        collection.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					Intent intent=new Intent();
					intent.setClass(arg0.getContext(), collection_Activity.class);
					startActivity(intent);
				}        	
	        });      
	       
	        conect=(TextView)findViewById(R.id.contact_us);
	        conect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					//Intent intent=new Intent();
					//intent.setClass(arg0.getContext(), conect_usActivity.class);
					//startActivity(intent);
					RongIM.getInstance().startConversationList(MainActivity.this);
				}
	        	
	        });
	        this.findViewById(R.id.my_im).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					
				}
	        });
	        this.findViewById(R.id.log_off).setOnClickListener(new OnClickListener()
	        {

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					 logoutTask logoutTask=new logoutTask();
				      logoutTask.executeOnExecutor(Executors.newCachedThreadPool(),"");				
				} 	
	        });
	        this.findViewById(R.id.change_user_password).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO 自动生成的方法存根
					
					Intent intent=new Intent();
					intent.setClass(MainActivity.this, change_userpassword_Activity.class);
					startActivity(intent);
				}
	        	
	        	
	        	
	        });
	      
	        

	  }
	  @Override
	   protected void onResume() {
	       
		  super.onResume();
		  userinfo=getSharedPreferences("user", Activity.MODE_PRIVATE);
	     // user_name.setText(userinfo.getString("nickname", "未登陆"));  
	      requestlistTask listTask=new requestlistTask();
	      listTask.executeOnExecutor(Executors.newCachedThreadPool(),"");
	  }
	  
	  @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event)  
	    {  
	        if (keyCode == KeyEvent.KEYCODE_BACK )  
	        {  
	            // 创建退出对话框  
	            AlertDialog isExit = new AlertDialog.Builder(this).create();  
	            // 设置对话框标题  
	            isExit.setTitle("退出？");  
	            // 设置对话框消息  
	            isExit.setMessage("确定要退出吗");  
	            // 添加选择按钮并注册监听  
	            isExit.setButton("确定", listener);  
	            isExit.setButton2("取消", listener);  
	            // 显示对话框  
	            isExit.show();  
	  
	        }  
	          
	        return false;  
	          
	    }  
	    /**监听对话框里面的button点击事件*/  
	    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
	    {  
	        public void onClick(DialogInterface dialog, int which)  
	        {  
	            switch (which)  
	            {  
	            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
	            	ExitApplication.getInstance().exit(MainActivity.this);
	                break;  
	            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
	                break;  
	            default:  
	                break;  
	            }  
	        }  
	    };    
	  
	  private void initViewPager() {  
          advPager = (ViewPager)findViewById(R.id.adv_pager);  
          
          
          ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);  
          // 这里存放的是四张广告背景  
          /*
          List<View> advPics = new ArrayList<View>();  
    
          ImageView img1 = new ImageView(this);  
          img1.setBackgroundResource(R.drawable.bigimg1);  
          advPics.add(img1);  
          
          ImageView img2 = new ImageView(this);  
          img2.setBackgroundResource(R.drawable.bigimg2);  
          advPics.add(img2);  
          
          ImageView img3 = new ImageView(this);  
          img3.setBackgroundResource(R.drawable.bigimg3);  
          advPics.add(img3);  
           
          ImageView img4 = new ImageView(this);  
          img4.setBackgroundResource(R.drawable.bigimg4);  
          advPics.add(img4);  
          
          ImageView img5 = new ImageView(this);  
          img5.setBackgroundResource(R.drawable.bigimg5);  
          advPics.add(img5); 
          */
          // 对imageviews脚标进行填充  
          imageViews = new ImageView[5];  
          // 初始化小图标，自动滚动图片右下角的星星  
          // 如果需要修改星星的图片修改drawable文件即可  
          for (int i = 0; i < 5; i++) {  
              imageView = new ImageView(this);  
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
      } 

  	private void whatOption() {  
          what.incrementAndGet();  
          if (what.get() > imageViews.length - 1) {  
              what.getAndSet(0);  
          }  
          try {  
              Thread.sleep(TIME_INTERVAL);  
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
  	        		case 0 :	//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);break;
  	        		case 1 :	//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);break;
  	        		case 2 :   // menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);break;
  	        		case 3 :   // menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);break;
  	        		case 4 :   // menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);break;	
  	        		
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
						/*
						ImageView view=(ImageView)arg0;
						view.setDrawingCacheEnabled(true);
						Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
						intent.putExtra("img", bitmap);
						view.setDrawingCacheEnabled(false);
						
						*/
						Intent intent=new Intent();
						intent.setClass(MainActivity.this, news_detail_Activity.class);	
						intent.putExtra("position",positon);
					
						
					
						
						startActivity(intent);
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
	    	    
	  //侧滑菜单
	  
	private void initmenu() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm) ;
		int width = dm.widthPixels;//
		int height = dm.heightPixels ;//
		// 实例化滑动菜单对象
		//menu = new SlidingMenu(this);
		// 设置为左滑菜单 
		//menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置滑动阴影的宽度
		//menu.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动阴影的图像资源
		//menu.setShadowDrawable(R.drawable.shadow);
		// 设置滑动菜单划出时主页面显示的剩余宽度
		//menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		//menu.setFadeDegree(0.35f);
		// 附加在Activity上
		//menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		// 设置滑动菜单的布局
		//menu.setMenu(R.layout.menu);
		

	}
	 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
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
	         * 组件集合，对应list.xml中的控件 
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
	      
	        @SuppressLint("NewApi")
			@Override  
	        public View getView(int position, View convertView, ViewGroup parent) {  
	            Zujian zujian=null;  
	            if(convertView==null){  
	                zujian=new Zujian();  
	                //获得组件，实例化组件  
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
	            //绑定数据   
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
	  
	    public class requestlistTask extends AsyncTask<Object,Integer,String>
		 {
			@Override
			protected String doInBackground(Object... map) {
				// TODO 自动生成的方法存根
				String url="http://easyeducation.sinaapp.com/";
				HttpPost httpPost=new HttpPost(url);
				List<NameValuePair>param=new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("action","reconmend_list"));
				HttpResponse httpResponse=null;
				String result=new String("");
			    try { 
		            // 设置httpPost请求参数 
		            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
		            httpResponse = new DefaultHttpClient().execute(httpPost); 
		            //System.out.println(httpResponse.getStatusLine().getStatusCode()); 
		            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
		                // 第三步，使用getEntity方法活得返回结果 
		                 result= EntityUtils.toString(httpResponse.getEntity()); 
		               // System.out.println("listresult:" + result);             
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
					int n=Integer.parseInt(data.getString("n"));
					JSONObject array=new JSONObject(data.getString("list"));
					if(opentime==0)
					progressBar.dismiss();
					opentime++;
					recommond_list_data=new ArrayList<Map<String,Object>>();  
			        for (int i = 1; i < n+1; i++) {  
			            Map<String, Object> map=new HashMap<String, Object>();  
			            String item=""+i;
			            map.put("headsrc", R.drawable.lst_usrimg1);  
			            map.put("name", array.getJSONObject(item).getString("nickname"));  		            
			            map.put("job", array.getJSONObject(item).getString("job"));
			            
			            if(!array.getJSONObject(item).getString("headsrc").equals(""))
			            {
			            	byte imgbyte[]= Base64.decode(array.getJSONObject(item).getString("headsrc"), Base64.DEFAULT);
			            	Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
			            	if(bitmap!=null)
			            	{
				            map.put("headimg", bitmap);
			            	}
			            }
			            username_list.put(array.getJSONObject(item).getString("nickname"),
			            		array.getJSONObject(item).getString("username"));
			            String []lables;		         
			            lables=array.getJSONObject(item).getString("tag").split("\\|");
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
			            recommond_list_data.add(map);  
			        }  
			        recommond_list.setAdapter(new MyAdspter(MainActivity.this, recommond_list_data));
			        
			        byte imgbyte1[]= Base64.decode(userinfo.getString("headsrc",""), Base64.DEFAULT);
			 		 Bitmap bitmap2 = BitmapFactory.decodeByteArray(imgbyte1, 0, imgbyte1.length);
			        headimg=(ImageView)findViewById(R.id.user_headimg);
			        headimg.setImageBitmap(bitmap2);
		            
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}  
			 }
			 
			 @Override  
			    protected void onPreExecute() {  
				 if(opentime==0)
				 {
					progressBar = new ProgressDialog(MainActivity.this);

					progressBar.setCancelable(true);

					progressBar.setMessage("加载中 ...");

					progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

					progressBar.setProgress(0);

					progressBar.setMax(100);

					progressBar.show(); 
			    }  
			 }
			 @Override  
			    protected void onProgressUpdate(Integer... values) {  
			        int vlaue = values[0];  
			        progressBar.setProgress(vlaue);  
			    }  
			
			 
		 }
	    public class logoutTask extends AsyncTask<Object,Integer,String>
		 {
			@Override
			protected String doInBackground(Object... map) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				String url="http://easyeducation.sinaapp.com/";
				HttpPost httpPost=new HttpPost(url);
				List<NameValuePair>param=new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("action","logout"));
				param.add(new BasicNameValuePair("id",userinfo.getString("id", "")));		
				HttpResponse httpResponse=null;
				String result=new String("");
			    try { 
		           
		            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
		            httpResponse = new DefaultHttpClient().execute(httpPost); 
		           
		            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
		                
		                 result= EntityUtils.toString(httpResponse.getEntity()); 
		                System.out.println("logoutresult:" + result);             
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
					if(data.getString("state").equals("1"))
					{
						
						Toast.makeText(getApplicationContext(), "退出登陆", 500).show();
						Intent intent=new Intent();
			        	intent.setClass(MainActivity.this, login_Activity.class);
			        	startActivity(intent);
						
						
						
					}
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}  
			 }
		 }
	    public class getnewsTask extends AsyncTask<Object,Integer,String>
		 {
			@Override
			protected String doInBackground(Object... map) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				String url="http://easyeducation.sinaapp.com/";
				HttpPost httpPost=new HttpPost(url);
				List<NameValuePair>param=new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("action","reconmend_news"));	
				HttpResponse httpResponse=null;
				String result=new String("");
			    try { 
		           
		            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
		            httpResponse = new DefaultHttpClient().execute(httpPost); 
		           
		            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
		                
		                 result= EntityUtils.toString(httpResponse.getEntity()); 
		               // System.out.println("newsresult:" + result);             
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
				
				 try {
					JSONObject data = new JSONObject(result);	
					if(data.getString("state").equals("1"))
					{
						int n=Integer.parseInt(data.getString("n"));
						JSONObject array=new JSONObject(data.getString("list"));
				        for (int i = 1; i < n+1; i++) {
				        	
				        	String item=""+i;
				        	String id=array.getJSONObject(item).getString("graph");
				        	
				        	get_newspic_Task getpicTask=new get_newspic_Task(id);
				        	getpicTask.executeOnExecutor(Executors.newCachedThreadPool(),"");
							Map map=new HashMap<String,String>();
				        	map.put("author",array.getJSONObject(item).getString("author")); 
				        	map.put("headline",array.getJSONObject(item).getString("headline")); 
				        	map.put("time",array.getJSONObject(item).getString("time")); 
				        	map.put("content",array.getJSONObject(item).getString("content"));
				        	news_list.add(map);
				        }
						
						
						
						
						
						
					}
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}  
			 }
		 }
	    public class get_newspic_Task extends AsyncTask<String,Integer,Drawable>
   	 {
 
   		 String id;
   		 public get_newspic_Task(String id)
   		 {
   			 this.id=id;
   		 }
   		@Override
   		protected Drawable doInBackground(String... map) {
   			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
   			String myurl="http://easyeducation.sinaapp.com/pic/";
   			myurl=myurl+id;
   			System.out.println("newpic_url="+myurl);
   			Drawable drawable = null ;
   		
   	
   			try {						
			    	drawable = Drawable.createFromStream(
			    		    new URL(myurl).openStream(),"image.jpg");
				} catch (MalformedURLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
   			return drawable;
							
   		} 
   		 @SuppressLint({ "ShowToast", "NewApi" })
   		@Override  
   	        protected void onPostExecute(Drawable result) {  	
   			 
   			 		news_pic_list.add(result);
   			 		ImageView img1 = new ImageView(MainActivity.this);  
   			 		img1.setBackground(result);
   			 		myadvPics.add(img1);
   			 	    if(myadvPics.size()==5) 
   			 	    {
   			 	    	System.out.println("size="+myadvPics.size()+"设置adapter");
   			 	    	AdvAdapter advpter=new AdvAdapter(myadvPics);
   			 	    	advpter.notifyDataSetChanged();
   			 	    	
   			 	    	advPager.setAdapter(advpter);
   			 	    }
   			 		
   		 }
   	 }
	    
	    public class getronginfoTask extends AsyncTask<Object,Integer,String>
		 {
	    	
	    	String id;
	    	OnDataFinishedListener onDataFinishedListener;
	    	public getronginfoTask(String id)
	    	{
	    		
	    		this.id=id;
	    	}
	    	   public void setOnDataFinishedListener(
	                   OnDataFinishedListener onDataFinishedListener) {
	               this.onDataFinishedListener = onDataFinishedListener;
	           }
	    
			@Override
			protected String doInBackground(Object... map) {
				// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
				String url="http://easyeducation.sinaapp.com/";
				HttpPost httpPost=new HttpPost(url);
				List<NameValuePair>param=new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("action","getronginfo"));	
				param.add(new BasicNameValuePair("username",id));
				
				HttpResponse httpResponse=null;
				String result=new String("");
			    try { 
		           
		            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
		            httpResponse = new DefaultHttpClient().execute(httpPost); 
		           
		            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
		                
		                 result= EntityUtils.toString(httpResponse.getEntity()); 
		                 System.out.println("ronginforesult:" + result);             
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
				
				 try {
					JSONObject data = new JSONObject(result);	
					if(data.getString("state").equals("1"))
					{
						
				       Map map=new HashMap<String,Object>();
					   map.put("nickname",data.get("nickname"));
					   map.put("headsrc", data.get("headsrc"));
					   onDataFinishedListener.onDataSuccessfully(map);
						
						
						
					}
					else onDataFinishedListener.onDataFailed();
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}  
			 }
		 }
	    public interface OnDataFinishedListener {
	        
	        public void onDataSuccessfully(Object data);
	         
	        public void onDataFailed();
	         
	    }
	    
	 
}
