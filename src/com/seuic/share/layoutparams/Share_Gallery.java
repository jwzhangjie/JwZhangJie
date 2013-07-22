package com.seuic.share.layoutparams;

import android.widget.LinearLayout;

public class Share_Gallery
{
	/*
	 * 初始化布局参数
	 */
	public void initParams(){
		parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		galleryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//		galleryParams.topMargin = ToolsUnility.getToolsUnilityInstance(AppConnect.getInstance().context).dip2px(43);
//		galleryParams.bottomMargin = ToolsUnility.getToolsUnilityInstance(AppConnect.getInstance().context).dip2px(5);
	}
	/*
	 * 布局参数声明
	 */
	public LinearLayout.LayoutParams parentParams;
	public LinearLayout.LayoutParams galleryParams;
}
