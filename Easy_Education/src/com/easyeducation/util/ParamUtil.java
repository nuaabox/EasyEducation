package com.easyeducation.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ParamUtil{
	public static final String appId = "1005";
	
	private static LruCache<String, Bitmap> mLruCache = null;
}
