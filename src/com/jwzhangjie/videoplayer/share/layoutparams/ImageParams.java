package com.jwzhangjie.videoplayer.share.layoutparams;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class ImageParams {
	Activity activity;

	public ImageParams(Activity activity){
		this.activity = activity;
	}
	public void initVar(){
		if (screenSize > 5.8)
		{
			button_width = 60;
		}else {
			button_width = 40;
		}
	}
	public int dip2px(float dpValue) {  
        return (int)(dpValue * scale + 0.5f);
    }
	//获取屏幕的宽度，高度和密度以及dp / px
	public void getDisplayMetrics() {
  		DisplayMetrics dm = new DisplayMetrics();
  		dm = activity.getApplicationContext().getResources().getDisplayMetrics();
  		Screen_width = dm.widthPixels;
  		Screen_height = dm.heightPixels;
  		scale = activity.getResources().getDisplayMetrics().density;
  		double bb = Math.sqrt(Math.pow(Screen_width, 2)+ Math.pow(Screen_height, 2));
  		screenSize = bb / (160 * dm.density);
	 }
		 
	 public void initLandLayoutParams(){
		 shareLayoutParams = new RelativeLayout.LayoutParams(dip2px(button_width), dip2px(button_width));
		 shareLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		 shareLayoutParams.rightMargin = dip2px(100);
		 
		 flipInParentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		 flipInParentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 flipInParentLayoutParams.bottomMargin = dip2px(5);
		 
		 parentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	 }
	//声明变量
	public float scale;
	public double screenSize;
	public int button_width = 60;
	public int Screen_width;
	public int Screen_height;
	public int title_height = 0,status_height = 0;
	//声明布局参数
	public RelativeLayout.LayoutParams flipInParentLayoutParams;
	public RelativeLayout.LayoutParams parentLayoutParams;
	
	public RelativeLayout.LayoutParams shareLayoutParams;
	
}

