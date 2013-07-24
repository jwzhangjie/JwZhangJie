package com.jwzhangjie.videoplayer.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.jwzhangjie.videoplayer.R;
import com.jwzhangjie.videoplayer.component.AppActivityClose;
import com.jwzhangjie.videoplayer.dialog.wifi_not_connect;
import com.jwzhangjie.videoplayer.flip.FlipViewController;
import com.jwzhangjie.videoplayer.flip.FlipViewController.ViewFlipListener;
import com.jwzhangjie.videoplayer.share.DeleteDialog;
import com.jwzhangjie.videoplayer.share.layoutparams.ImageParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Gallery.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageGalleryActivity extends Activity {
	public static final String tag = "ImageGalleryActivity";
	private static ImageGalleryActivity mContext;
	protected static final int SHOW_PROGRESS = 0;
	public final static int DELETE_ENBALE = 5000;
	public final static int SHARE_ENABLE = 5001;
	public final static int ShOW_POP = 5002;
	private PopupWindow mPopupWindow;
	private String photoPath;
	private int position = 0;
	private List<String> photo_path1;
	private String path;
	public RelativeLayout Parent;
	FlipViewController flipView;
	RelativeLayout topInParentLayout;
	private TextView photos_count;	
	private Button deleButton;
	private Button shareButton;
	public ImageParams imageParams;
	private boolean isShowing = false;
	private boolean connectWifi = false;
	public boolean isExit = false;
	public boolean isShared = false;
	private File file;
	private Dialog dlg;
	Timer deletetDelayTimer;
	Timer sharetDelayTimer;
	Timer showPopTimer;
	ImageAdapter imageAdapter;
	Handler handler;
	public static ImageGalleryActivity getInstance() {
		return mContext;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		imageParams = new ImageParams(this);
		imageParams.getDisplayMetrics();
		imageParams.initVar();
		imageParams.initLandLayoutParams();
		Parent = new RelativeLayout(this);
		flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		topInParentLayout = new RelativeLayout(this);
		topInParentLayout.setId(135);
		Parent.addView(flipView, imageParams.flipInParentLayoutParams);
//		Parent.setBackgroundResource(R.drawable.info_back);
		setContentView(Parent,imageParams.parentLayoutParams);
		
		AppActivityClose.getInstance().addActivity(this);
		connectWifi = note_Intent(mContext);
		path = "/mnt/sdcard";//AppInforToCustom.getAppInforToCustomInstance().getCameraPicturePath();
		photo_path1 = getInSDPhoto();
		Intent intent = getIntent();
		photoPath = intent.getStringExtra("ImagePath");
		position = intent.getIntExtra("position", 0);
		imageAdapter = new ImageAdapter(getApplicationContext());
		flipView.setAdapter(imageAdapter, position);
		flipView.setOnViewFlipListener(new ViewFlipListener() {
			public void onViewFlipped(View view, int position_t) {
				if (photos_count != null) {
					position = position_t;
					photos_count.setText((position + 1) + " of " + photo_path1.size());
				}
			}
		});
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
				case ShOW_POP:
					showPopWindow();
					break;
				}
			}
		};
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (!isShowing) {
			if (showPopTimer == null) {
				showPopTimer = new Timer();
				showPopTimer.schedule(new showPopTask(), 500);
			}
		}
	}

	@Override
	protected void onStop()
	{
		if (isShared && !isExit) {
			isShared = false;
		}else if (!isShared && !isExit && !AppActivityClose.isExit) {
			isExit = true;
			AppActivityClose.getInstance().exitAll();
		}
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		dismiss();
		if (sharetDelayTimer != null) {
			sharetDelayTimer.cancel();
			sharetDelayTimer = null;
		}
		if (deletetDelayTimer != null) {
			deletetDelayTimer.cancel();
			deletetDelayTimer = null;
		}
		if (showPopTimer != null) {
			showPopTimer.cancel();
			showPopTimer = null;
		}
		super.onDestroy();
	}
	/*
	 * 获取照片路径下的图片
	 */
	public List<String> getInSDPhoto() {
			List<String> it_p = new ArrayList<String>();
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}else {
				File[] files = f.listFiles();
				for(File file : files){
					if (file.isFile()) {
						String fileName = file.getName(); 
						if (fileName.endsWith(".jpg")) { 						
							it_p.add(file.getPath());
						}
					}
				}
			}
			return it_p;
	}
	/** 
	 * 判断当前是否连接网络
	 * @param context
	 * @return
	 */
	public boolean note_Intent(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = con.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable() || !networkinfo.isConnected()) {
			return false;
		}else{
			return true;
		}
		
	}
	/**
	 * 关闭popwindow
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
			handler.sendEmptyMessage(DELETE_ENBALE);
			deletetDelayTimer.cancel();
			deletetDelayTimer = null;
		}
	 }
	 class shareDelayTask extends TimerTask{
		@Override
		public void run() {
			handler.sendEmptyMessage(SHARE_ENABLE);
			sharetDelayTimer.cancel();
			sharetDelayTimer = null;
		}
	 }
	 class showPopTask extends TimerTask{
		@Override
		public void run() {
			handler.sendEmptyMessage(ShOW_POP);
			showPopTimer.cancel();
			showPopTimer = null;
		}
		 
	 }
	private void showPopWindow(){
	    dismiss();
	    isShowing = true;
        View foot_popunwindwow = null;

        LayoutInflater LayoutInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        foot_popunwindwow = LayoutInflater
                .inflate(R.layout.photo_count, null);

        mPopupWindow = new PopupWindow(foot_popunwindwow,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
       
        mPopupWindow.showAtLocation(Parent, Gravity.TOP , 0, 5);
        mPopupWindow.update();

        photos_count = (TextView) foot_popunwindwow.findViewById(R.id.photo_counts);
        photos_count.setText(position + 1 + " of " + photo_path1.size());
        deleButton = (Button) foot_popunwindwow.findViewById(R.id.delete_button);
        deleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				deleButton.setEnabled(false);
                shareButton.setEnabled(false);
				if(deletetDelayTimer == null){
					deletetDelayTimer = new Timer();
					deletetDelayTimer.schedule(new deleteDelayTask(), 1500);
				}
				dlg = new DeleteDialog(mContext,R.style.DeleteDialog,1);
				 
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
						sharetDelayTimer = new Timer();
						sharetDelayTimer.schedule(new shareDelayTask(), 1500);
					}
					Intent shareIntent =new Intent();
					shareIntent.setAction(Intent.ACTION_SEND);
					shareIntent.setType("image/jpeg"); 
					shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
					
					 file = new File(photoPath); 
					 
					 ContentValues content = new ContentValues(5);
					 content.put(MediaStore.Images.ImageColumns.TITLE, "Share");
					 content.put(MediaStore.Images.ImageColumns.SIZE, file.length());
					 content.put(MediaStore.Images.ImageColumns.DATE_ADDED,System.currentTimeMillis() / 1000); 
					 content.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
					 content.put(MediaStore.Images.Media.DATA, photoPath);
					 ContentResolver resolver = mContext.getContentResolver();
					 Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content);
					 
					 if(uri == null){
						 shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); 
					 }else{
						 shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
					 }
					 
					startActivity(Intent.createChooser(shareIntent, "Share"));
					isShared = true;			
			}else{
        		wifi_not_connect.createwificonnectDialog(mContext).show();
        	}
			}
		});
	}
	/**
	 * 删除图片，然后局部更新PhotoImage中的缩略图
	 */
	public void Delete_photo(){
		photoPath = photo_path1.get(position).toString();//由于浏览大图时，没有更新photoPath的数据，所以删除的时候，要更新
		file = new File(photoPath);
		if(file.exists()){
			 file.delete();
		 }
		PhotoImage.imageAdapterP.removePhoto(position); //删除图片的所路图// 这里要考虑200章图片
		if (position < PhotoImage.instance.photo.size()) {
			PhotoImage.instance.photo.remove(position);//删除PhotoImage
		}
		if (photo_path1.size() - 1 == 0)
		{
			dismiss();
			isExit = true;
			mContext.finish();
		}else {
			photo_path1.remove(position);
			if(position == photo_path1.size()){//如果删除最后一页，则返回第一页
				position = 0;
				imageAdapter = new ImageAdapter(getApplicationContext());
				flipView.setAdapter(imageAdapter, position);
			}else {
				imageAdapter.notifyDataSetChanged();
			}
			dlg.dismiss();
			photoPath = photo_path1.get(position).toString();
			photos_count.setText(position + 1 + " of " + photo_path1.size());
		}
	}
	class ImageAdapter extends BaseAdapter{
		private Context mContext; 
		LayoutInflater inflater1;
		BitmapFactory.Options options;
		Bitmap bitmap;
		public ImageAdapter(Context applicationContext) {
			mContext = applicationContext;
			inflater1 = LayoutInflater.from(mContext);
			options = new BitmapFactory.Options();
			options.inSampleSize = 2;
		}

		public int getCount() {
			return photo_path1.size();
		}

		public Object getItem(int potion) {
			return potion;
		}

		public long getItemId(int potion) {
			return potion;
		}

		public View getView(int potion, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater1.inflate(R.layout.photo_view, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView_view);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			bitmap = BitmapFactory.decodeFile(photo_path1.get(potion).toString(), options);
			viewHolder.imageView.setImageBitmap(bitmap);
			return convertView;
		}
	}
	
	class ViewHolder {
		ImageView imageView;
	}
	@Override
	public void onBackPressed()
	{
		isExit = true;
		ImageGalleryActivity.this.finish();
	}
}
