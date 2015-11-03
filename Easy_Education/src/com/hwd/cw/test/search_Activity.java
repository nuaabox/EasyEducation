package com.hwd.cw.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

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

import com.hwd.cw.test.collection_Activity.MyOnPageChangeListener;
import com.hwd.cw.test.collection_Activity.MyPagerAdapter;
import com.hwd.cw.test.collection_Activity.MycoAdspter;
import com.hwd.cw.test.collection_Activity.MyuniAdspter;
import com.hwd.cw.test.collection_Activity.get_pic_Task;
import com.hwd.cw.test.collection_Activity.MycoAdspter.Zujian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class search_Activity  extends Activity{
	
	

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1_univercity, t2_user ;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    
    
    private View search_univercityView;
    private View search_userView;
    private ListView search_univercity_list;
    private ListView search_user_list;
    
    private JSONObject udata;
    public static Map<String,Drawable>search_pic_list =new HashMap<String,Drawable>();;
	ProgressDialog progressBar;
	private AutoCompleteTextView search_text;
	private ImageView search_btn;
	private ImageView back_btn;
	public static Map<String,String>search_username_list;//
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.setContentView(R.layout.search_xml);
	        ExitApplication.getInstance().addActivity(this);
	        search_username_list=new HashMap<String,String>();
	       
	        
	        
	        search_text=(AutoCompleteTextView) this.findViewById(R.id.auto_seach_edit);
	        search_btn=(ImageView) this.findViewById(R.id.searchpage_search_btn);
	        back_btn=(ImageView)this.findViewById(R.id.search_back_main);
	        
	        LayoutInflater mInflater = getLayoutInflater();
	        search_univercityView=mInflater.inflate(R.layout.co_univercity, null);
	        search_userView=mInflater.inflate(R.layout.co_user, null);
	        search_univercity_list=(ListView) search_univercityView.findViewById(R.id.co_univercity_list);
	        search_user_list=(ListView) search_userView.findViewById(R.id.co_user_list);
	        
	        
	        
	        
	        cursor=(ImageView) this.findViewById(R.id.search_move_bar);
	        t1_univercity=(TextView)this.findViewById(R.id.search_univercity_text);
	        t2_user=(TextView)this.findViewById(R.id.search_user_text);
	        
	        InitViewPager();
	        InitImageView();
	        
	        
	        search_user_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根
					Intent intent =new Intent();
					intent.putExtra("origin", "search_Activity");
					TextView view=(TextView)arg1.findViewById(R.id.co_list_text);
					String	nickname=view.getText().toString();
					intent.putExtra("nickname",nickname);
					Bitmap bitmap=(Bitmap) arg1.findViewById(R.id.co_list_img).getTag();
					intent.putExtra("headimg", bitmap);
					intent.setClass(search_Activity.this, user_page_Activity.class);
					startActivity(intent);
					
				}	
	        });
	        
	        
	       search_univercity_list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO 自动生成的方法存根
					Intent intent =new Intent();
					TextView view=(TextView)arg1.findViewById(R.id.co_list_text);
					String	name=view.getText().toString();
					intent.putExtra("unid", (String)view.getTag());
					System.out.println("unid1="+(String)view.getTag());
					intent.putExtra("name",name);
					intent.putExtra("orgin", "search_Activity");
					intent.setClass(search_Activity.this, univercity_page_Activity.class);
					startActivity(intent);
					
				}
	        	
	        	
	        	
	        }); 
	       
	        
	       search_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				
				searchTask task=new searchTask();
				task.executeOnExecutor(Executors.newCachedThreadPool(),"");
			} 
	       });
	        
	       back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				finish();
			}
	    	   
	       });
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	 }
	 
	 
	 
	 
	 
	 
	 private void InitViewPager() {
         mPager = (ViewPager) findViewById(R.id.search_pager);
          listViews = new ArrayList<View>();
         LayoutInflater mInflater = getLayoutInflater();    
         listViews.add(search_userView);		        
         listViews.add(search_univercityView);
         mPager.setAdapter(new MyPagerAdapter(listViews));
         mPager.setCurrentItem(0);
         mPager.setOnPageChangeListener(new MyOnPageChangeListener());
     }
/**
 * ViewPager适配器
*/
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
    bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.co_move_bar)
            .getWidth();// 获取图片宽度
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int screenW = dm.widthPixels;// 获取分辨率宽度
    offset = (screenW / 3 - bmpW)/2 ;// 计算偏移量
    Matrix matrix = new Matrix();
    matrix.postTranslate(offset, 0);
    cursor.setImageMatrix(matrix);// 设置动画初始位置
}
/**
 * 页卡切换监听
*/
public class MyOnPageChangeListener implements OnPageChangeListener {

    int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
    int two = one * 2;// 页卡1 -> 页卡3 偏移量

    @Override
    public void onPageSelected(int arg0) {
        Animation animation = null;
        switch (arg0) {
        case 0:
            if (currIndex == 1) {
                animation = new TranslateAnimation(one-3*offset, one, 0, 0);
            } else if (currIndex == 2) {
                animation = new TranslateAnimation(two, 0, 0, 0);
            }
            break;
        case 1:
            if (currIndex == 0) {
                animation = new TranslateAnimation(one, one-3*offset, 0, 0);
            } else if (currIndex == 2) {
                animation = new TranslateAnimation(two, one, 0, 0);
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
	public class MycoAdspter extends BaseAdapter {  
    
    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    public MycoAdspter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    /** 
     * 组件集合，对应list.xml中的控件 
     * @author Administrator 
     */  
    public final class Zujian{  
        public ImageView co_list_img;  
        public TextView co_list_text;
        
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
            convertView=layoutInflater.inflate(R.layout.co_list, null);  
            zujian.co_list_img=(ImageView)convertView.findViewById(R.id.co_list_img);  
            zujian.co_list_text=(TextView)convertView.findViewById(R.id.co_list_text); 	              
            convertView.setTag(zujian);  
        }else{  
            zujian=(Zujian)convertView.getTag();  
        }  
        //绑定数据  	            
        zujian.co_list_text.setText((String)data.get(position).get("co_list_text"));
        Bitmap bitmap=(Bitmap)data.get(position).get("headimg");
        if(bitmap!=null)
        {
        zujian.co_list_img.setImageBitmap((Bitmap)data.get(position).get("headimg"));
        zujian.co_list_img.setTag((Bitmap)data.get(position).get("headimg"));
        }
        else zujian.co_list_img.setBackgroundResource(R.drawable.lst_usrimg1);	           
       
        return convertView;  
    }  
  
}  
			public class MyuniAdspter extends BaseAdapter {  
        
        private List<Map<String, Object>> data;  
        private LayoutInflater layoutInflater;  
        private Context context;  
        public MyuniAdspter(Context context,List<Map<String, Object>> data){  
            this.context=context;  
            this.data=data;  
            this.layoutInflater=LayoutInflater.from(context);  
        }  
        /** 
         * 组件集合，对应list.xml中的控件 
         * @author Administrator 
         */  
        public final class Zujian{  
            public ImageView co_list_img;  
            public TextView co_list_text;
            
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
                convertView=layoutInflater.inflate(R.layout.co_list, null);  
                zujian.co_list_img=(ImageView)convertView.findViewById(R.id.co_list_img);  
                zujian.co_list_text=(TextView)convertView.findViewById(R.id.co_list_text); 	              
                convertView.setTag(zujian);  
            }else{  
                zujian=(Zujian)convertView.getTag();  
            }  
            //绑定数据  
            zujian.co_list_img.setBackground((Drawable) ((data.get(position).get("co_list_img"))));  
            zujian.co_list_text.setText((String)data.get(position).get("co_list_text"));   
            zujian.co_list_text.setTag(data.get(position).get("id"));
            return convertView;  
        }  
      
    }  
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public class searchTask extends AsyncTask<Object,Integer,String>
	 {
		@Override
		protected String doInBackground(Object... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String url="http://easyeducation.sinaapp.com/";
			HttpPost httpPost=new HttpPost(url);
			List<NameValuePair>param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("action","search"));
			param.add(new BasicNameValuePair("keyString",search_text.getText().toString()));
			HttpResponse httpResponse=null;
			String result=new String("");
		    try { 
	           
	            httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8)); 
	            httpResponse = new DefaultHttpClient().execute(httpPost); 
	           
	            if (httpResponse.getStatusLine().getStatusCode() == 200) { 
	                
	                 result= EntityUtils.toString(httpResponse.getEntity()); 
	                System.out.println("search_result:" + result);             
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
				JSONObject mydata = new JSONObject(result);	
				progressBar.dismiss();
				if(mydata.getString("state").equals("1"))
				{
					
					
					JSONObject array=mydata.getJSONObject("plist");	
    				List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
    				List<Map<String, Object>> ulist=new ArrayList<Map<String,Object>>();  
    				
    				int n=Integer.parseInt(array.getString("n"));
    				for(int i=1;i<=n;i++)
    				{
    					 String item=""+i;
						 
    					 Map<String, Object> map=new HashMap<String, Object>();  
						 
						 
						 String name=array.getJSONObject((item)).getString("nickname");
						 map.put("co_list_text",array.getJSONObject((item)).getString("nickname"));
						 String imgString=array.getJSONObject((item)).getString("headsrc");
						 if(!imgString.equals(""))
						 {
						 byte imgbyte[]= Base64.decode(imgString, Base64.DEFAULT);
					 	 Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
					 	 map.put("headimg", bitmap);
						 }
					 	 
    					if(!name.equals("null"))
    					{	
    						list.add(map);
    						search_username_list.put(array.getJSONObject(item).getString("nickname"),
								  array.getJSONObject(item).getString("username"));
    					}
    					
    				}
    				 search_user_list.setAdapter(new MycoAdspter(search_Activity.this,list));
    				
    				 
    				 udata=mydata.getJSONObject("ulist");
    				 
    				 int len=Integer.parseInt(udata.getString("n"));
						for(int i=1;i<=len;i++)
						{
							 String item=""+i;
							 Map<String, Object> map=new HashMap<String, Object>();  
							 get_pic_Task getpic=new get_pic_Task(udata.getJSONObject(item).getString("name"),
									 udata.getJSONObject(item).getString("id"));
							 getpic.execute("");							
							 map.put("co_list_img",search_pic_list.get(udata.getJSONObject(item).getString("name")));
							 map.put("co_list_text",udata.getJSONObject((item)).getString("name"));
							 map.put("id", udata.getJSONObject(item).getString("id"));
							 ulist.add(map);	
						}
						search_univercity_list.setAdapter(new MyuniAdspter(search_Activity.this,ulist));
    				 
    				 
    				 
    				

				}
				else if(mydata.getString("info").equals("User already exists."))
				{
					Toast.makeText(getApplicationContext(), "无相关内容", 500).show();
					
				}
				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}  
		 }
		 @Override  
		    protected void onPreExecute() {  
				progressBar = new ProgressDialog(search_Activity.this);

				progressBar.setCancelable(true);

				progressBar.setMessage("搜索中 ...");

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
	 public class get_pic_Task extends AsyncTask<String,Integer,Drawable>
	 {
		 String name;
		 String id;
		 public get_pic_Task(String name,String id)
		 {
			 this.name=name;	
			 this.id=id;
		 }
		@Override
		protected Drawable doInBackground(String... map) {
			// TODO 锟皆讹拷锟斤拷锟缴的凤拷锟斤拷锟斤拷锟�
			String myurl="http://easyeducation.sinaapp.com/pic/";
			System.out.println("picid="+id);
			myurl=myurl+"u"+id+".jpg";
			Drawable drawable = null ;
		
			System.out.println("url="+myurl);
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
			 System.out.println("istream="+result);//获取输入
			// Bitmap bitmap=null;
			// BitmapFactory.Options options=new BitmapFactory.Options(); options.inSampleSize = 5; 	
			 //bitmap=BitmapFactory.decodeStream(result,null, options);    			
			// pic_list.put(name, bitmap);
			
			 if(result!=null)
			 {
			 	search_pic_list.put(name, result); 	
			 }
			 else 
			 search_pic_list.put(name, null);
			 
			 	List<Map<String, Object>> ulist=new ArrayList<Map<String,Object>>();  
				try {
					
					int n=Integer.parseInt(udata.getString("n"));
					for(int i=1;i<=n;i++)
					{
						 String item=""+i;
						 Map<String, Object> map=new HashMap<String, Object>();  											
						 map.put("co_list_img",search_pic_list.get(udata.getJSONObject(item).getString("name")));
						 map.put("co_list_text",udata.getJSONObject((item)).getString("name"));
						 map.put("id", udata.getJSONObject(item).getString("id"));
						 ulist.add(map);	
					}
					search_univercity_list.setAdapter(new MyuniAdspter(search_Activity.this,ulist));
					
				} catch (NumberFormatException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			 
			// System.out.println(pic_list);
		 }
	 }
	

			
	
}
