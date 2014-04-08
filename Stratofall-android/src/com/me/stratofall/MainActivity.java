package com.me.stratofall;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.stackmob.android.sdk.common.StackMobAndroid;

public class MainActivity extends AndroidApplication
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);

		//StackMobAndroid.init(getApplicationContext(), 0, "<Development Key Goes Here>"); //Development key need to change to production key when released
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		cfg.useCompass = false;
		cfg.useAccelerometer = true;
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
		initialize(new Stratofall(), cfg);
	}
}