package com.example.androiddemo.entry;

import java.util.ArrayList;

public class LauncherThemeConfig {

	public String model;
	
	public ArrayList<String> pkgs = new ArrayList<String>();
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public ArrayList<String> getPkgs() {
		return pkgs;
	}
	
	public void setPkgs(ArrayList<String> pkgs) {
		this.pkgs = pkgs;
	}
}
