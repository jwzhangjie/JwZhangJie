package com.jwzhangjie.videoplayer.component;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class AppActivityClose extends Application
{
	private List<Activity> mList = new LinkedList<Activity>();
	private static AppActivityClose instance;
	public static boolean isExit = false;
	private AppActivityClose(){
		
	}
	public synchronized static AppActivityClose getInstance(){
		if (instance == null)
		{
			instance = new AppActivityClose();
		}
		return instance;
	}
	
	//add activity
	public void addActivity(Activity activity){
		mList.add(activity);
	}
	
	public void exitAll(){
		try
		{
			 isExit = true;
			for (Activity activity : mList)
			{
				if (activity != null)
				{
					activity.finish();
					activity = null;
				}
			}
		} catch (Exception e)
		{
			
			e.printStackTrace();
		}finally{
			instance = null;
			System.gc();
			System.exit(0);
		}
	}
	public void exit(){
		try
		{
			isExit = true;
			for (Activity activity : mList)
			{
				if (activity != null)
				{
					activity.finish();
					activity = null;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			instance = null;
			System.gc();
		}
	}
	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
		System.gc();
	}

}
