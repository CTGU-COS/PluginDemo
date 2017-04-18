package com.example.androiddemo;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import dalvik.system.DexClassLoader;

public class ProxyActivity extends Activity {

	private DexClassLoader loader;
	private Activity activity;
	private Class<?> clazz = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initloader(savedInstanceState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Method onStart = null;
		try {
			onStart = clazz.getDeclaredMethod("onStart");
			onStart.setAccessible(true);
			onStart.invoke(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 调用插件activity的onDestroy方法  
        Method onDestroy = null;  
        try {  
            onDestroy = clazz.getDeclaredMethod("onDestroy");  
            onDestroy.setAccessible(true);  
            onDestroy.invoke(activity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

	private void initloader(Bundle savedInstanceState) {
		Intent intent = new Intent("com.example.plugin");
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resolveinfos = pm.queryIntentActivities(intent, 0);
		if (resolveinfos.size() == 0)
			return;

		ActivityInfo actInfo = resolveinfos.get(0).activityInfo;
		String packageName = actInfo.packageName;
		String apkPath = actInfo.applicationInfo.sourceDir;
		String dexOutputDir = getApplicationInfo().dataDir;
		String libPath = actInfo.applicationInfo.nativeLibraryDir;

		loader = new DexClassLoader(apkPath, dexOutputDir, libPath, getClass().getClassLoader());
		try {
			clazz = loader.loadClass(actInfo.name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Method setProxy = clazz.getDeclaredMethod("setProxy", Activity.class);
			setProxy.setAccessible(true);

			activity = (Activity) clazz.newInstance();
			setProxy.invoke(activity, this);

			Method onCreate = clazz.getDeclaredMethod("onCreate", Bundle.class);
			onCreate.setAccessible(true);
			onCreate.invoke(activity, savedInstanceState);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
