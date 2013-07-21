package com.jwzhangjie.videoplayer;

import io.vov.vitamio.LibsChecker;

import java.util.List;

import com.jwzhangjie.videoplayer.adapter.JieVideoListViewAdapter;
import com.jwzhangjie.videoplayer.component.LoadedImage;
import com.jwzhangjie.videoplayer.videofile.AbstructProvider;
import com.jwzhangjie.videoplayer.videofile.Video;
import com.jwzhangjie.videoplayer.videofile.VideoProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class JieVideo extends Activity{

	public JieVideo instance = null;
	ListView mJieVideoListView;
	JieVideoListViewAdapter mJieVideoListViewAdapter;
	List<Video> listVideos;
	private TextView first_letter_overlay;
	private ImageView alphabet_scroller; //字母滚动查询表
	int videoSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.jie_video);
		instance = this;
		AbstructProvider provider = new VideoProvider(instance);
        listVideos = provider.getList();
        videoSize = listVideos.size();
        mJieVideoListViewAdapter = new JieVideoListViewAdapter(this, listVideos);
		mJieVideoListView = (ListView)findViewById(R.id.jievideolistfile);
		mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
		mJieVideoListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent();
				intent.setClass(JieVideo.this, JieVideoPlayer.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("video", listVideos.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		loadImages();
	    first_letter_overlay = (TextView)findViewById(R.id.first_letter_overlay);
        alphabet_scroller = (ImageView)findViewById(R.id.alphabet_scroller);
        alphabet_scroller.setClickable(true);
        alphabet_scroller.setOnTouchListener(asOnTouch);
	}
	/**
	 * Load images.
	 */
	private void loadImages() {
		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}
	}
	private void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			mJieVideoListViewAdapter.addPhoto(image);
			mJieVideoListViewAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
		final ListView grid = mJieVideoListView;
		final int count = grid.getChildCount();
		final LoadedImage[] list = new LoadedImage[count];

		for (int i = 0; i < count; i++) {
			final ImageView v = (ImageView) grid.getChildAt(i);
			list[i] = new LoadedImage(
					((BitmapDrawable) v.getDrawable()).getBitmap());
		}

		return list;
	}
	/** 
	    * 获取视频缩略图 
	    * @param videoPath 
	    * @param width 
	    * @param height 
	    * @param kind 
	    * @return 
	    */  
	   private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){  
	    Bitmap bitmap = null;  
	    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	    return bitmap;  
	   }  

	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			for (int i = 0; i < videoSize; i++) {
				bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
				if (bitmap != null) {
					publishProgress(new LoadedImage(bitmap));
				}
			}
			return null;
		}
		
		@Override
		public void onProgressUpdate(LoadedImage... value) {
			addImage(value);
		}
	}
	/**
	 * A-Z
	 */
	private OnTouchListener asOnTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// 0
				alphabet_scroller.setPressed(true);
				first_letter_overlay.setVisibility(View.VISIBLE);
				mathScrollerPosition(event.getY());
				break;
			case MotionEvent.ACTION_UP:// 1
				alphabet_scroller.setPressed(false);
				first_letter_overlay.setVisibility(View.GONE);
				break;
			case MotionEvent.ACTION_MOVE:
				mathScrollerPosition(event.getY());
				break;
			}
			return false;
		}
	};

	/**
	 * 显示字符
	 * 
	 * @param y
	 */
	private void mathScrollerPosition(float y) {
		int height = alphabet_scroller.getHeight();
		float charHeight = height / 28.0f;
		char c = 'A';
		if (y < 0)
			y = 0;
		else if (y > height)
			y = height;

		int index = (int) (y / charHeight) - 1;
		if (index < 0)
			index = 0;
		else if (index > 25)
			index = 25;

		String key = String.valueOf((char) (c + index));
		first_letter_overlay.setText(key);

		int position = 0;
		if (index == 0)
			mJieVideoListView.setSelection(0);
		else if (index == 25)
			mJieVideoListView.setSelection(mJieVideoListViewAdapter.getCount() - 1);
		else {
			int size = listVideos.size();
			for (int i = 0; i < size; i++) {
				if (listVideos.get(i).getTitle_key().startsWith(key)) {
					mJieVideoListView.setSelection(position);
					break;
				}
				position++;
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
