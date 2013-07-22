package com.seuic.share.layoutparams;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class VideoParams {
	public float scale;
	Activity activity;
	public double screenSize;
	public int button_width = 60;
	public int Screen_width;
	public int Screen_height;
	public int title_height = 0,status_height = 0;
	public VideoParams(Activity activity){
		this.activity = activity;
	}
	public void initVar(){
		if (screenSize > 5.8){
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
		 myGallerylLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 myGallerylLayoutParams.topMargin = dip2px(button_width);
		 myGallerylLayoutParams.bottomMargin = dip2px(5);
		 GallerylLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		 parentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	 } 
	 
	 public RelativeLayout.LayoutParams myGallerylLayoutParams;
	 public RelativeLayout.LayoutParams GallerylLayoutParams;
	 public RelativeLayout.LayoutParams parentLayoutParams;
}
