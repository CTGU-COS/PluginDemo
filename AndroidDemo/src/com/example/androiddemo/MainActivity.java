package com.example.androiddemo;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.androiddemo.entry.LauncherThemeConfig;
import com.example.androiddemo.util.ThemeXmlParseUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends Activity {

	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PLDemo.apk";

	Button mButton, mProxyButton;
	TextView mTextView;

	DexClassLoader loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mButton = (Button) findViewById(R.id.button);
		mProxyButton = (Button) findViewById(R.id.button1);
		mTextView = (TextView) findViewById(R.id.text);

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<LauncherThemeConfig> configs = ThemeXmlParseUtil.parseXMLWithPull(MainActivity.this,
						"themeConfig.xml");
				mTextView.setText(configs.get(0).model + configs.get(1).getPkgs().toString());
				useDexClassLoader();
			}
		});

		mProxyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity.this.startActivity(new Intent(MainActivity.this, ProxyActivity.class));
			}
		});
	}

	/**
	 * 这种方式用于从已安装的apk中读取，必须要有一个activity，且需要配置ACTION
	 */
	private void useDexClassLoader() {
		Intent intent = new Intent("com.example.plugin");
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resolveinfos = pm.queryIntentActivities(intent, 0);
		if (resolveinfos.size() == 0) {
			return;
		}
		ActivityInfo actInfo = resolveinfos.get(0).activityInfo;

		String packageName = actInfo.packageName;
		String apkPath = actInfo.applicationInfo.sourceDir;
		String dexOutputDir = getApplicationInfo().dataDir;
		String libPath = actInfo.applicationInfo.nativeLibraryDir;
		DexClassLoader classLoader = new DexClassLoader(apkPath, dexOutputDir, libPath,
				this.getClass().getClassLoader());
		replaceClassLoader(classLoader);
		Class activity = null;
		try {
			activity = classLoader.loadClass("com.example.pldemo.PLActivity");
		} catch (ClassNotFoundException e) {
			Log.i("MainActivity", "ClassNotFoundException");
		}
		Intent intent1 = new Intent(MainActivity.this, activity);
		MainActivity.this.startActivity(intent1);
		try {
			Class<?> clazz = classLoader.loadClass(packageName + ".PluginClass");

			Object obj = clazz.newInstance();
			Class[] param = new Class[2];
			param[0] = Integer.TYPE;
			param[1] = Integer.TYPE;

			Method method = clazz.getMethod("function", param);

			Integer ret = (Integer) method.invoke(obj, 12, 34);

			Log.d("JG", "返回的调用结果为：" + ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void useDexDirClassLoader() {

	}

	private void replaceClassLoader(DexClassLoader loader) {
		try {
			Class clazz_Ath = Class.forName("android.app.ActivityThread");
			Class clazz_LApk = Class.forName("android.app.LoadedApk");
			Object currentActivityThread = clazz_Ath.getMethod("currentActivityThread").invoke(null);
			Field field1 = clazz_Ath.getDeclaredField("mPackages");
			field1.setAccessible(true);
			Map mPackages = (Map) field1.get(currentActivityThread);
			String packageName = MainActivity.this.getPackageName();
			WeakReference ref = (WeakReference) mPackages.get(packageName);
			Field field2 = clazz_LApk.getDeclaredField("mClassLoader");
			field2.setAccessible(true);
			field2.set(ref.get(), loader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
