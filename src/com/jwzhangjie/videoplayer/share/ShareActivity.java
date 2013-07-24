package com.jwzhangjie.videoplayer.share;

import java.util.ArrayList;
import java.util.List;

import com.jwzhangjie.videoplayer.R;
import com.jwzhangjie.videoplayer.component.AppActivityClose;


import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

@SuppressWarnings("deprecation")
public class ShareActivity extends Activity {

	public ShareActivity instance = null;
	LocalActivityManager manager = null;
	ViewPager pager = null;
	TabHost tabHost = null;
	TextView photoText,videoText;
	public boolean isExit = false;
	public static boolean isBackground = false;

	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ImageView cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.share_main);
		instance = this;
		AppActivityClose.getInstance().addActivity(instance); //tianjia
		manager = new LocalActivityManager(this , true);
		manager.dispatchCreate(savedInstanceState);

		InitImageView();
		initTextView();
		initPagerViewer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (isBackground) {
			isBackground = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!AppActivityClose.isExit && !isExit && !isBackground) {
			AppActivityClose.getInstance().exitAll();
		}
	}

	@Override
	protected void onDestroy() {
		if (!AppActivityClose.isExit) {
			AppActivityClose.getInstance().exitAll();
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		isExit = true;
		instance.finish();
	}

	private void initTextView() {
		photoText = (TextView) findViewById(R.id.phototext);
		videoText = (TextView) findViewById(R.id.videotext);

		photoText.setOnClickListener(new MyOnClickListener(0));
		videoText.setOnClickListener(new MyOnClickListener(1));
	}
	
	private void initPagerViewer() {
		pager = (ViewPager) findViewById(R.id.viewpage);
		final ArrayList<View> list = new ArrayList<View>();
		Intent photoIntent = new Intent(ShareActivity.this, PhotoImage.class);
		list.add(getView("PhotoImage", photoIntent));
		Intent videoIntent = new Intent(ShareActivity.this, VideoImage.class);
		list.add(getView("VideoImage", videoIntent));

		pager.setAdapter(new ImagePagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller).getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 2 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	/**
	 * @param id
	 * @param intent
	 * @return
	 */
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * Pager
	 */
	public class ImagePagerAdapter extends PagerAdapter{
		List<View> list =  new ArrayList<View>();
		public ImagePagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(View container, int position,
				Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
			
		}
	}
	
	public class MyOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + bmpW;

		public void onPageSelected(int opt) {
			Animation animation = null;
			switch (opt) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} 
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} 
				break;
			}
			currIndex = opt;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		public void onPageScrollStateChanged(int arg0) {
			
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;
		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	};
}
