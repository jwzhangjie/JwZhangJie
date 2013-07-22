package com.seuic.share.layoutparams;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ShareParams {
	Activity activity;
	
	public ShareParams(Activity activity){
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
  		density = dm.density;
  		double bb = Math.sqrt(Math.pow(Screen_width, 2)+ Math.pow(Screen_height, 2));
  		screenSize = bb / (160 * dm.density);
	}
		 
	 //初始化屏幕参数
	 public void initLandLayoutParams(){
	 	textlLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		textlLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		textLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		gridLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		grid1laLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		grid1laLayoutParams.addRule(RelativeLayout.BELOW,1);
		grid1laLayoutParams.addRule(RelativeLayout.ABOVE, 2);
		
		gridvideoLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		gridvideo2LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		gridvideo2LayoutParams.addRule(RelativeLayout.BELOW,1);
		gridvideo2LayoutParams.addRule(RelativeLayout.ABOVE, 2);
		
	    buttonLayoutParams = new LinearLayout.LayoutParams(dip2px(3*button_width),dip2px(button_width));
		int jiange = (Screen_width - dip2px(3*button_width * button_sum))/(button_sum+1);
		buttonLayoutParams.leftMargin = jiange;
		bottLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		bottLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    parentLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	 }
	 //声明变量
	 public float scale;
	 public double screenSize;
	 public int button_width = 60;
	 public int Screen_width;
	 public int Screen_height;
	 public float density;
	 public int button_sum = 2;
	 //声明布局参数
	 public RelativeLayout.LayoutParams textlLayoutParams;
	 public RelativeLayout.LayoutParams textLayoutParams;
	 public RelativeLayout.LayoutParams gridLayoutParams,gridvideoLayoutParams;
	 public RelativeLayout.LayoutParams grid1laLayoutParams,gridvideo2LayoutParams;
	 public LinearLayout.LayoutParams buttonLayoutParams;
	 public RelativeLayout.LayoutParams bottLayoutParams;
	 public RelativeLayout.LayoutParams parentLayoutParams;
}
