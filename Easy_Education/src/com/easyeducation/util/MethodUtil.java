package com.easyeducation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class MethodUtil {

	private static final String TAG = "MethodUtil";
	public static ImageFileCache fileCache;
	private static Context context;
	private static FileService fileService;

	public static String postToHttp(String baseUrl,
			ArrayList<BasicNameValuePair> params) {
		String filename = "";
		String result = "";

		// TODO 鏍规嵁璇锋眰url锛屽垽鏂渶瑕佷繚�?�樿鍙栫殑瀵瑰簲鏂囦欢
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost postMethod = new HttpPost(baseUrl);
			postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 灏嗗弬鏁板～鍏OST
			// Log.d(TAG, "-------baseUrl = " + baseUrl); // Entity涓�
			// Log.d(TAG, "-------params = " + params); // Entity涓�

			HttpResponse response = httpClient.execute(postMethod); // 鎵цPOST鏂规�?
			// Log.d(TAG, "resCode = "
			// + response.getStatusLine().getStatusCode()); // 鑾峰彇鍝嶅簲鐮�
			result = EntityUtils.toString(response.getEntity(), "utf-8");
			// Log.d(TAG, "-------result = " + result); // 鑾峰彇鍝嶅簲鍐呭�?
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 鎻愬彇json瀛椾覆涓殑imgUrl锛屽苟閫氳繃璇rl浠庢枃浠剁紦瀛樻垨缃戠粶涓幏鍙栧浘鐗�
	 * 
	 * @param actionUrl
	 *            鍚岃繃璇rl锛屾兂鏈嶅姟鍣ㄥ彂閫佽姹傦紝鑾峰彇鍝嶅簲鐨刯son瀛楃涓�闇�閫氳繃绾跨▼鍔犺�?
	 * @return 杩斿洖Bitmap瀵硅薄鏁扮粍
	 */
	public static Bitmap[] getBitmaps(String actionUrl,
			ArrayList<BasicNameValuePair> params, Context context) {
		MethodUtil methodUtil = new MethodUtil();
		fileCache = new ImageFileCache();
		fileService = new FileService(context);
		String jsonText = "";
		Bitmap[] bitmaps = null;
		JSONArray jsonArray = null;
		String filename = "";
		Log.i(TAG, "--------------------" + actionUrl
				+ "----------------------");
			// 椤堕儴鍥剧墖
			filename = "topImage";
		// TODO 鍒ゆ柇缃戠粶杩炴�?
		if (!methodUtil.isNetworkAvailable(context)) {
			try {
				jsonText = fileService.read(filename);
				Log.i(TAG, "=================fileService.read(" + filename
						+ ")==================");
			} catch (Exception e) {
			//	Toast.makeText(context, "璇锋煡鐪嬬綉缁滆繛鎺�?, Toast.LENGTH_SHORT)
				//.show();
				e.printStackTrace();
			}
		} else {

			try {
				jsonText = fileService.read(filename);
				Log.i(TAG, "=================fileService.read(" + filename
						+ ")==================");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(TextUtils.isEmpty(jsonText)){
				try {
					jsonText = fileService.read(filename);
					Log.i(TAG, "=================fileService.read(" + filename
							+ ")==================");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (TextUtils.isEmpty(jsonText)) {
				jsonText = MethodUtil.postToHttp(actionUrl, params);
				if (TextUtils.isEmpty(jsonText)) {
					jsonText = MethodUtil.postToHttp(actionUrl, params);
				}
				if (!TextUtils.isEmpty(jsonText)) {
					try {
						Log.i(TAG, "-----------------" + jsonText
								+ "--------------");
						Log.i(TAG, "-----------------" + filename
								+ "--------------");
						Log.i(TAG,
								"=================fileService.MODE_PRIVATE_Sava("
										+ filename + ")==================");
						fileService.MODE_PRIVATE_Sava(filename, jsonText);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(context, "鏁版嵁鍔犺浇澶辫触锛岃閲嶅惎搴旂敤",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		}
		try {
			jsonArray = new JSONArray(jsonText.equals("") ? "[]" : jsonText);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		int length = jsonArray.length();
		Log.d(TAG, "length is" + length);
		if (length > 0) {
			bitmaps = new Bitmap[length];
			for (int i = 0; i < length; i++) {
				JSONObject jsonItem;
				try {
					jsonItem = jsonArray.getJSONObject(i);
					bitmaps[i] = getBitmap(jsonItem.getString("imgUrl"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmaps;
	}

	/**
	 * 浠庣綉缁滄垨鏂囦欢缂撳瓨涓幏鍙栧浘鐗囬泦鍙婂搴旂殑url
	 * 
	 * @param actionUrl
	 *            鍚戞湇鍔″櫒璇锋眰json瀛楃涓茬殑url
	 * @return
	 */
	public static List<Map<String, Object>> getImages(String actionUrl,
			ArrayList<BasicNameValuePair> params, Context context) {
		MethodUtil methodUtil = new MethodUtil();
		fileCache = new ImageFileCache();
		// TODO 淇濆瓨鏁版嵁
		fileService = new FileService(context);
		String filename = "";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		String jsonText = "";
		JSONArray jsonArray = null;

			filename = "bottomImage";
		// TODO 鍒ゆ柇缃戠粶杩炴�?
		if (!methodUtil.isNetworkAvailable(context)) {
			try {
				jsonText = fileService.read(filename);
				Log.i(TAG, "=================fileService.read(" + filename
						+ ")==================");
				
			} catch (Exception e) {
				//Toast.makeText(context, "璇锋煡鐪嬬綉缁滆繛鎺�?, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else {

			try {
				jsonText = fileService.read(filename);
				Log.i(TAG, "=================fileService.read(" + filename
						+ ")==================");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(TextUtils.isEmpty(jsonText)){
				try {
					jsonText = fileService.read(filename);
					Log.i(TAG, "=================fileService.read(" + filename
							+ ")==================");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (TextUtils.isEmpty(jsonText)) {
				jsonText = MethodUtil.postToHttp(actionUrl, params);
				if (TextUtils.isEmpty(jsonText)) {
					jsonText = MethodUtil.postToHttp(actionUrl, params);
				}
				if (!TextUtils.isEmpty(jsonText)) {
					try {
						fileService.MODE_PRIVATE_Sava(filename, jsonText);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.i(TAG, "-----------------" + e.toString()
								+ "--------------");
					}
				}
			}
		}
		try {
			jsonArray = new JSONArray(jsonText.equals("") ? "[]" : jsonText);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		int length = jsonArray.length();
		Log.d(TAG, length + "");
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				map = new HashMap<String, Object>();
				JSONObject jsonItem;
				try {
					jsonItem = jsonArray.getJSONObject(i);
					map.put("bitmap", getBitmap(jsonItem.getString("imgUrl")));
					map.put("url", jsonItem.getString("url"));
					list.add(map);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 浠庣綉缁滃拰鏂囦欢缂撳瓨涓幏鍙栧浘鐗囩殑�?�炵幇鏂规硶
	 * 
	 * @param url
	 *            鍥剧墖鐨剈rl
	 * @return 杩斿洖Bitmap 瀵硅�?
	 */
	public static Bitmap getBitmap(String url) {
		Bitmap result = null;
		if (!url.equals("")) {
			// 鏂囦欢缂撳瓨涓幏鍙�?
			result = fileCache.getImage(url);
			System.out.println("fileCache.getImage(url)---111---33333");
			if (result == null) {
				//Log.d(TAG, "鏂囦欢缂撳瓨閲屾病鏈�?);
				result = ImageGetFromHttp.downloadBitmap(url);
				if (result == null) {
					result = ImageGetFromHttp.downloadBitmap(url);
					if (result == null) {
						result = ImageGetFromHttp.downloadBitmap(url);
					}
				}
				System.out
						.println("ImageGetFromHttp.downloadBitmap(url)--111--4444");
				if (result != null) {
					// 灏嗙綉缁滃浘鐗囨坊鍔犲埌鏂囦欢缂撳瓨涓�
					fileCache.saveBitmap(result, url);
					System.out
							.println("fileCache.saveBitmap(result, url);--111--5555");
				}
			}
		}
		return result;
	}

	/**
	 * 鍒ゆ柇缃戠粶鐘舵�?
	 * 
	 * @param context
	 *            涓婁笅鏂�?
	 * @return true 琛ㄧず鏈夌綉缁�false 琛ㄧず娌℃湁缃戠�?
	 */
	public static boolean isNetworkAvailable(Context context) {
		// 鑾峰緱缃戠粶鐘舵�绠＄悊鍣�
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			if (info != null) {
				for (NetworkInfo network : info) {
					if (network.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
