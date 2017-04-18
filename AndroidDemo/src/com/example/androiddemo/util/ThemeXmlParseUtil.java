package com.example.androiddemo.util;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.example.androiddemo.entry.LauncherThemeConfig;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

public class ThemeXmlParseUtil {

	public static ArrayList<LauncherThemeConfig> parseXMLWithPull(Context context, String fileName) {
		ArrayList<LauncherThemeConfig> mThemeConfigs = null;
		LauncherThemeConfig mLauncherThemeConfig = null;
		
		XmlPullParser xmlPullParser = Xml.newPullParser();
        AssetManager assetManager = context.getAssets();
        
        try {
			InputStream inputStream = assetManager.open(fileName);
			xmlPullParser.setInput(inputStream, "utf-8");
			
			int type = xmlPullParser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {
				 String currentTagName = xmlPullParser.getName();
				 
				 switch (type) {
				case XmlPullParser.START_TAG:
					if (currentTagName.equals("themeSeries")) {
						mThemeConfigs = new ArrayList<LauncherThemeConfig>();
					} else if (currentTagName.equals("species")) {
						mLauncherThemeConfig = new LauncherThemeConfig();
						mLauncherThemeConfig.model = xmlPullParser.getAttributeValue(null, "model");
					} else if (currentTagName.equals("pkg")) {
						mLauncherThemeConfig.pkgs.add(xmlPullParser.nextText());
					}
					break;

				case XmlPullParser.END_TAG:
					if (currentTagName.equals("species")) {
						mThemeConfigs.add(mLauncherThemeConfig);
					}
					
				default:
					break;
				}
				type = xmlPullParser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return mThemeConfigs;
	}
}
