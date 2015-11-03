package com.easyeducation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.easyeducation.fragment.HomeFragment;
import com.hwd.cw.test.R;

public class MainActivity extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.content);
		HomeFragment homeFragment = new HomeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, homeFragment, "Home").commit();
	}
}
