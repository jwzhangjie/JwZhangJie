package com.jwzhangjie.videoplayer.util;

import java.util.prefs.Preferences;

public class MyPreferences {
	private Preferences prefs;
	/**
	 * 用于个别用户的偏好
	 * @return
	 */
	public Preferences UserNewInstance(){
		if(prefs == null){
			prefs = Preferences.userNodeForPackage(getClass());
		}
		return prefs;
	}
	/**
	 * 用于通用的安装配置
	 * @return
	 */
	public Preferences SystemNewInstace(){
		if(prefs == null){
			prefs = Preferences.systemNodeForPackage(getClass());
		}
		return prefs;
	}
	
	public void addString(String key, String value){
		prefs.put(key, value);
	}
	
	public void addInt(String key, int value){
		prefs.putInt(key, value);
	}
	
	public void addBoolean(String key, boolean value){
		prefs.putBoolean(key, value);
	}
	
	public void addLong(String key, long value){
		prefs.putLong(key, value);
	}
	
	public void addDouble(String key, double value){
		prefs.putDouble(key, value);
	}
	
	public void addFloat(String key, float value){
		prefs.putFloat(key, value);
	}
	
	public void addByteArray(String key, byte[] value){
		prefs.putByteArray(key, value);
	}
}
