package com.example.pldemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class PLActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Toast.makeText(this, "PL onStart", Toast.LENGTH_SHORT).show();
	}

}
