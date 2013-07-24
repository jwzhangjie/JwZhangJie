package com.jwzhangjie.videoplayer.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;

import com.jwzhangjie.videoplayer.R;
import com.jwzhangjie.videoplayer.component.AppActivityClose;
import com.jwzhangjie.videoplayer.component.AppMediaPlayerFunction;
import com.jwzhangjie.videoplayer.dialog.wifi_not_connect;
import com.jwzhangjie.videoplayer.share.DeleteDialog;
import com.jwzhangjie.videoplayer.share.layoutparams.VideoParams;

import android.content.ContentResolver;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SdCardPath")
public class VideoGalleryActivity extends Activity {
	private static VideoGalleryActivity mContext = null;
	public final static int DELETE_ENBALE = 5000;
	public final static int SHARE_ENABLE = 5001;
	public VideoAdapter imageAdapterV;
	public static XmlPullParser parser;
	private PopupWindow mPopupWindow;
	private MyGallery myGallery;
	public LinearLayout Parent;
	private String videoPath;
	RelativeLayout myGallerylLayout;
	private int positionV;
	public List<String> video_path;
	public VideoParams videoParams;
	private TextView photos_count;	
	private Button deleButton;
	private Button shareButton;
	Timer deletetDelayTimer;
	Timer sharetDelayTimer;
	private boolean isShowing = false;
	private boolean connectWifi = false;
	private File file;
	private Dialog dlg;
	Handler handler;
	public boolean isExit = false;
	public boolean isShared = false;
	public boolean isBackgroud = false;
	public static VideoGalleryActivity getInstance() {
		return mContext;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (isShared && !isExit) {
			isShared = false;
		}else if (isBackgroud) {
			isBackgroud = false;
		}else if (!isShared && !isExit && !AppActivityClose.isExit) {
			isExit = true;
			AppActivityClose.getInstance().exitAll();
		}
	}
	
	@Override
	protected void onDestroy() {
		for (int i = 0; i < 2; i++) {
			if (bitmap[i] != null && !bitmap[i].isRecycled()) {
				bitmap[i].recycle();
				bitmap[i] = null;
			}
		}
		
		if (sharetDelayTimer != null) {
			sharetDelayTimer.cancel();
			sharetDelayTimer = null;
		}
		if (deletetDelayTimer != null) {
			deletetDelayTimer.cancel();
			deletetDelayTimer = null;
		}
		dismiss();
		super.onDestroy();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		if (parser == null) {
			parser = getResources().getXml(R.layout.my_gallery);
		}
		AttributeSet attributes = Xml.asAttributeSet(parser);
		videoParams = new VideoParams(this);
		Parent = new LinearLayout(this);
		myGallerylLayout = new RelativeLayout(this);
		myGallerylLayout.setId(3);
		myGallery = new MyGallery(mContext, attributes);
		myGallery.setSpacing(16);
		
		videoParams.getDisplayMetrics();
		videoParams.initVar();
		videoParams.initLandLayoutParams();
		
		myGallerylLayout.addView(myGallery,videoParams.myGallerylLayoutParams);
		Parent.addView(myGallerylLayout,videoParams.GallerylLayoutParams);
		setContentView(Parent,videoParams.parentLayoutParams);		
		AppActivityClose.getInstance().addActivity(this); //tianjia
	
		connectWifi = note_Intent(mContext);
		video_path  = getInSDPhotoVideo();
		
		imageAdapterV = new VideoAdapter(getApplicationContext());
		Intent intent = getIntent();
		videoPath = intent.getStringExtra("videoPath");
		int currenPosition = intent.getIntExtra("position", 0);
		
		myGallery.setAdapter(imageAdapterV);
    	myGallery.setSelection(currenPosition);
		myGallery.setOnItemSelectedListener(listenerVideo);

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DELETE_ENBALE:
					deleButton.setEnabled(true);
                    shareButton.setEnabled(true);
					break;
				case SHARE_ENABLE:
					shareButton.setEnabled(true);
                    deleButton.setEnabled(true);
					break;
				}
			}
		};
	}
	/*
	 * 获取指定目录下的视频文件后缀为.mp4
	 */
	public static List<String> getInSDPhotoVideo() {
		List<String> it_p = new ArrayList<String>();
		String path = "/mnt/sdcard/";//AppInforToCustom.getAppInforToCustomInstance().getCameraShootingPath();
		File f = new File(path); 
		if (f.exists()) {	//首先要判断文件夹是否存在
			File[] files = f.listFiles();
			for(File file : files){
				if (file.isFile()) {  //如果是文件的话
					String fileName = file.getName();
					if (fileName.endsWith(".mp4")) {
						it_p.add(file.getPath());
					}
				}
			}
		}else {	//如果文件夹不存在则返回的list的大小为0，同时创建一个新的文件夹
			f.mkdirs();
		}
		return it_p;
	}
	/*
	 * 判断网络是否连接
	 */
	public boolean note_Intent(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		else{
			return true;
		}
	}
	/*
	 * 当界面消失时，将PopupWindow取消
	 */
	 public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
	 }
	
	 class deleteDelayTask extends TimerTask{
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = DELETE_ENBALE;
			handler.sendMessage(msg);
			deletetDelayTimer.cancel();
			deletetDelayTimer = null;
		}
	 }
	 class shareDelayTask extends TimerTask{
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = SHARE_ENABLE;
			handler.sendMessage(msg);
			sharetDelayTimer.cancel();
			sharetDelayTimer = null;
		}
	 }
	@SuppressWarnings("deprecation")
	private void showPopWindow(){
	    dismiss();
	    isShowing = true;
        View foot_popunwindwow = null;
        LayoutInflater LayoutInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        foot_popunwindwow = LayoutInflater.inflate(R.layout.photo_count, null);
        mPopupWindow = new PopupWindow(foot_popunwindwow,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(findViewById(3),
                Gravity.TOP , 0, 5);
        mPopupWindow.update();
        photos_count = (TextView) foot_popunwindwow.findViewById(R.id.photo_counts);
        deleButton = (Button) foot_popunwindwow.findViewById(R.id.delete_button);
        deleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleButton.setEnabled(false);
                shareButton.setEnabled(false);
				if(deletetDelayTimer == null){
					deletetDelayTimer = new Timer();
					deletetDelayTimer.schedule(new deleteDelayTask(), 1500);
				}
				dlg = new DeleteDialog(mContext,R.style.DeleteDialog,2);
				 
				 WindowManager m = getWindowManager(); 
			 	 Display d = m.getDefaultDisplay();
			 	 Window w=dlg.getWindow(); 
				 WindowManager.LayoutParams lp =w.getAttributes(); 
				 
				 w.setGravity(Gravity.RIGHT | Gravity.TOP);
				 lp.x=10; 
				 lp.y=70;
				 lp.height = (int) (d.getHeight() * 0.3);
				 w.setAttributes(lp);
				 dlg.show();
			}
		});
        shareButton = (Button) foot_popunwindwow.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(connectWifi){
					shareButton.setEnabled(false);
                    deleButton.setEnabled(false);
					if(sharetDelayTimer ==null){
						sharetDelayTimer = new Timer(true);
						sharetDelayTimer.schedule(new shareDelayTask(), 1500);
					}
					Intent shareIntent =new Intent();
					shareIntent.setAction("android.intent.action.SEND");
					shareIntent.setType("video/*"); 
					file = new File(videoPath);
					
					ContentValues content = new ContentValues(5);
					content.put(Video.VideoColumns.TITLE, "Share");
					content.put(MediaStore.Video.VideoColumns.SIZE, file.length());
					content.put(Video.VideoColumns.DATE_ADDED,System.currentTimeMillis() / 1000); 
					content.put(Video.Media.MIME_TYPE, "video/mp4");
					content.put(MediaStore.Video.Media.DATA, videoPath);
					ContentResolver contentResolver = getContentResolver();
					Uri base = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					Uri newUri = contentResolver.insert(base, content);
			       
					if(newUri == null){
						shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); 
					}else{
						shareIntent.putExtra(Intent.EXTRA_STREAM, newUri); 
					}
					 
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(Intent.createChooser(shareIntent, "Share"));
					isShared = true;
			}else{
        		wifi_not_connect.createwificonnectDialog(mContext).show();
        	}
			}
		});
	}
	
	public OnItemSelectedListener listenerVideo = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> adapter, View view, int position,
				long id) {
			positionV = position;
			videoPath = video_path.get(positionV).toString();
			if(!isShowing){
				showPopWindow();
			}
			photos_count.setText(positionV + 1 + " of " + video_path.size());
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	public void Delete_video() {
		file = new File(videoPath); 
		if(file.exists()){
			 file.delete();
			 file = null;
		 }
		VideoImage.imageAdapterV.removePhoto(positionV);//这里要判断视频的缩略图是否小于100张同时视频的个数大于100，如果符合则要添加视频的缩略图到VideoImage中。
		if (positionV < VideoImage.instance.video_path.size()) {
			VideoImage.instance.video_path.remove(positionV);
		}
		if (video_path.size() -1 == 0)
		{
			dismiss();
			isExit = true;
			mContext.finish();
		}else{
			video_path.remove(positionV);
			if(positionV == video_path.size()){ //如果是删除最后一个，返回到第一个
				 positionV = 0;
				 imageAdapterV = new VideoAdapter(getApplicationContext());
				 myGallery.setAdapter(imageAdapterV);
				 myGallery.setSelection(positionV);
			}else {
				imageAdapterV.notifyDataSetChanged();
			}
			dlg.dismiss();
			videoPath = video_path.get(positionV).toString();
			photos_count.setText(positionV + 1 + " of " + video_path.size());
		}
	}
	public Bitmap[] bitmap;
	class VideoAdapter extends BaseAdapter{
		private Context mContext;
		LayoutInflater inflater1;
		public VideoAdapter(Context context) {
			mContext = context;
			inflater1 = LayoutInflater.from(mContext);
			bitmap = new Bitmap[2];
		}

		public VideoAdapter(VideoGalleryActivity mContext2) {
			mContext = mContext2;
			inflater1 = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return video_path.size();
		}

		public void removeVideo(int position){
			video_path.remove(position);
		}
		
		public Object getItem(int position) {
			return video_path.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holderView;
			if (convertView == null) {
				convertView = inflater1.inflate(R.layout.video_play_item, null);
				holderView = new HolderView();
				holderView.imgv = (ImageView)convertView.findViewById(R.id.imageView_video_play);
				holderView.playBtn = (ImageView)convertView.findViewById(R.id.video_play_button);
				convertView.setTag(holderView);
			}else {
				holderView = (HolderView)convertView.getTag();
			}
			holderView.playBtn.setOnClickListener(videoPlayListent);
			holderView.imgv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			if (bitmap[0] == null) {
				bitmap[1] = getVideoThumbnail(bitmap[0], video_path.get(position).toString(),100,100,MediaStore.Images.Thumbnails.MICRO_KIND);
				holderView.imgv.setImageBitmap(bitmap[1]);
				if (bitmap[0] != null && !bitmap[0].isRecycled()) {
					bitmap[0].recycle();
					bitmap[0] = null;
				}
			}
			return convertView;
		}
		
		class HolderView{
			ImageView playBtn;
			ImageView imgv;
		}
		
	}
	/*
	 * 获取视频的缩图
	 * 先通过ThumbnailUtils来创建一个视频的图，然后再利用ThumbnailUtils来生成指定大小的图
	 * MICRO_KIND
	 */
	private Bitmap getVideoThumbnail(Bitmap bitmap, String videoPath, int width , int height, int kind){
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	public OnClickListener videoPlayListent = new OnClickListener() {
		public void onClick(View v) {
			//这里要判断这个视频文件是否存在，如果存在则播放，如果不存在，则刷新视频数据
			File file = new File(videoPath);
			if(file.exists()){
				Intent intent = new Intent(VideoGalleryActivity.this, AppMediaPlayerFunction.class);
				intent.putExtra("file_name", videoPath);
				intent.putExtra("file_position", positionV);
				startActivity(intent);
				isBackgroud = true;
			}else {//如果这个文件不存在，则刷新数据，提示文件被非法删除
				video_path = getInSDPhotoVideo();
				int size = video_path.size();
				if (size > 0) { //如果用户在后台将所有的视频都删除，则关闭这个界面
					if (positionV > size - 1) {
						positionV = 0;
					}
					videoPath = video_path.get(positionV).toString();
				}else{
					isExit = true;
					mContext.finish();
				}
			}
		}
	};
	
	@Override
	public void onBackPressed()
	{
		isExit = true;
		mContext.finish();
	}
}
