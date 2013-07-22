package com.jwzhangjie.videoplayer.component;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class AppMediaPlayerFunctionLayoutParams {
	
	Activity activity;
	public AppMediaPlayerFunctionLayoutParams(Activity activity){
		this.activity = activity;
	}
	
	public int dip2px(float dpValue) {  
        return (int)(dpValue * scale + 0.5f);
    }
	public int px2dip(float pxValue) {  
        return (int) (pxValue / scale + 0.5f);
    }  
	//获取屏幕的宽度，高度和密度以及dp / px
	 public void getDisplayMetrics() {
  		DisplayMetrics dm = new DisplayMetrics();
  		dm = activity.getApplicationContext().getResources().getDisplayMetrics();
  		Screen_width = dm.widthPixels;
  		Screen_height = dm.heightPixels;
  		scale = activity.getResources().getDisplayMetrics().density;
  		density = dm.density;
  		double bb = Math.sqrt(Math.pow(Screen_width, 2)+ Math.pow(Screen_height, 2));
  		screenSize = bb / (160 * dm.density);
	}
	 
		public int Screen_width;
		public int Screen_height;
		public float scale;//dp -- px
		public double screenSize;
		public float density;
		
		/*
		 * 初始化布局参数
		 */
		public void initParams(){
			parentParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			videoViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			videoViewParams.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
		}
		
		/*
		 * 布局参数声明
		 */
		public RelativeLayout.LayoutParams parentParams;
		public RelativeLayout.LayoutParams videoViewParams;
		public RelativeLayout.LayoutParams controllerParams;
}
