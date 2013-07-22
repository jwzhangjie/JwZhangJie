package com.jwzhangjie.videoplayer.component;

import com.jwzhangjie.videoplayer.util.AppLog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class AppMediaPlayerFunction extends Activity implements OnCompletionListener, OnPreparedListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appMediaPlayerFunctionLayoutParams = new AppMediaPlayerFunctionLayoutParams(this);
		appMediaPlayerFunctionLayoutParams.initParams();
		initLayoutAndCompont();
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnPreparedListener(this);
		if(!(getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_VIEW ))){
			 mVideoFileName = getIntent().getExtras().getString("file_name");
			 AppLog.e("mVideoFileName", mVideoFileName);
			 mVideoFilePosition = getIntent().getExtras().getInt("file_position");
		}
		if (mVideoFileName == null)
		{
			mVideoFileName = getRealPath(getIntent().getData());
		}
		mVideoView.setVideoPath(mVideoFileName);
		mVideoView.setMediaController(mController);
	}
	AppMediaPlayerFunctionLayoutParams appMediaPlayerFunctionLayoutParams;
	
	public void onPrepared(MediaPlayer mp) {
		mVideoView.start();
	}

	public void onCompletion(MediaPlayer mp) {
		mVideoView.stopPlayback();
		finish();
	}
	//得到文件的物理位置
	private String getRealPath(Uri fileUrl){
		String fileName = null;
		Uri filePathUri = fileUrl;
		if(fileUrl!= null){
		if (fileUrl.getScheme().toString().compareTo("content")==0)
		{ 
			Cursor cursor = getApplicationContext().getContentResolver().query(fileUrl, null, null, null, null);
		if (cursor != null && cursor.moveToFirst())
		{
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			fileName = cursor.getString(column_index); 
			cursor.close();
		}
		}else if (fileUrl.getScheme().compareTo("file")==0)
		{
			fileName = filePathUri.toString();
			fileName = filePathUri.toString().replace("file://", "");
			}
		}
		//AppAppLog.e("videoPlayerActivty", "the realPath:" + fileName);
			return fileName;
	}
	/*
	 * 设置布局
	 */
	public void setLayout(){
		Parent.addView(mVideoView, appMediaPlayerFunctionLayoutParams.videoViewParams);
		setContentView(Parent);
	}
	/*
	 * 初始化布局,组建
	 */
	public void initLayoutAndCompont(){
		Parent = new RelativeLayout(this);
		mVideoView = new VideoView(this);
		mController = new MediaController(this);
		Parent.addView(mVideoView, appMediaPlayerFunctionLayoutParams.videoViewParams);
		setContentView(Parent);
	}
	/*
	 * 声明布局,组建变量
	 */
	public RelativeLayout Parent;
	private VideoView mVideoView = null;
	private MediaController mController = null;
	public String mVideoFileName = null;
	public int mVideoFilePosition;
}
