package com.example.pldemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Activity proxyActivity;
	public void setProxy(Activity proxyActivity) {
		this.proxyActivity = proxyActivity;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TextView tv = new TextView(proxyActivity);
		tv.setText("Plugin");
		proxyActivity.setContentView(tv, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	@Override
	protected void onStart() {
		Toast.makeText(proxyActivity, "onStart", Toast.LENGTH_SHORT).show();
	}
}
