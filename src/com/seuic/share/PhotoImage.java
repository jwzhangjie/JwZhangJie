package com.seuic.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jwzhangjie.videoplayer.R;
import com.jwzhangjie.videoplayer.component.AppActivityClose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoImage extends Activity{
	public List<String> photo;
	public String path;
	Bitmap bitmapP = null;
	public static PhotoImage instance;
	public static ImageAdapterPhoto imageAdapterP;
	private GridView photo_gridview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photoimage);
		instance = this;
		AppActivityClose.getInstance().addActivity(this);
		path = "/mnt/sdcard";//AppInforToCustom.getAppInforToCustomInstance().getCameraPicturePath();
		photo = getInSDPhoto();
		imageAdapterP = new ImageAdapterPhoto(getApplicationContext());
		photo_gridview = (GridView)findViewById(R.id.photoGallery);
		photo_gridview.setAdapter(imageAdapterP);
		photo_gridview.setOnItemClickListener(new OnItemClickListener() {
 			public void onItemClick(AdapterView<?> parent, View view,
 					int position, long id) {
 				try
				{
 					File file = new File(photo.get(position).toString());
     				if (file.exists())
					{
     					ShareActivity.isBackground = true;
						Intent i = new Intent();
						i.setClass(PhotoImage.this, ImageGalleryActivity.class);
						i.putExtra("ImagePath", photo.get(position).toString());
						i.putExtra("position", position);
						startActivity(i);
					}else {
						//如果不存在则重新进行加载图像
						photo = getInSDPhoto();
//						photo_gridview.
//						getAsyncTaskPhoto();
						showMessage("文件已经删除");
					}
     				file = null;
				} catch (IndexOutOfBoundsException e)
				{
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
 			}
 		});
		getAsyncTaskPhoto();
	}
	/*
	 * 获取SD卡指定目录的图片列表
	 */
	public List<String> getInSDPhoto() {
		List<String> it_p = new ArrayList<String>();
		File f = new File(path);
		if (!f.exists()){
			f.mkdirs();
		}else {
			File[] files = f.listFiles();
			for(File file : files){
				if (file.isFile()) {
					String fileName = file.getName(); 
						if (fileName.endsWith(".jpg")) {
							it_p.add(file.getPath());
						}
						if (it_p.size() >= 200)
						{
							showMessage("图片数量大于200张以后的不显示");
							break;
						}
					}else {
						file.delete();
					}
			}
		}
		return it_p;
	}
	/**
	 * 异步加载图片缩略图
	 */
    private void getAsyncTaskPhoto() {
		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		if (data == null)
		{
			new AsyncTaskLoadPhoto(photo).execute();
		} else {
			final Bitmap[] photos = (Bitmap[]) data;
			if (photos.length == 0) {
				new AsyncTaskLoadPhoto(photo).execute();
			}
			for (Bitmap photo : photos) {
				imageAdapterP.addPhoto(photo);
				imageAdapterP.notifyDataSetChanged();
			}
		}
	}
    /**
     * 图片异步类
     * @author zhangjie
     */
    class AsyncTaskLoadPhoto extends AsyncTask<Object, Bitmap, Object> {
		private List<String> photo_lis;
		public AsyncTaskLoadPhoto(List<String> path) {
			photo_lis = path;
		}
		@Override
		protected Object doInBackground(Object... params) {
			int size = photo.size();
			for(int i = 0; i < size; i++){
				if (AppActivityClose.isExit)break;
				bitmapP =  getImageThumbnail(bitmapP, photo_lis.get(i).toString(), 190, 92);
				if (bitmapP != null) {
					publishProgress(bitmapP);
				}else{
					Bitmap bitmapP2 = BitmapFactory.decodeFile( photo_lis.get(i).toString()); 
		            if(bitmapP2 !=null){
		            	bitmapP = Bitmap.createScaledBitmap(bitmapP2,190,92,true);
		            	publishProgress(bitmapP);
		            	bitmapP2.recycle();
		            }
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
		}

		@Override
		protected void onProgressUpdate(Bitmap... values) {
			for (Bitmap bitmap : values) {
				imageAdapterP.addPhoto(bitmap);
				imageAdapterP.notifyDataSetChanged();
			}
		}
    }
    /** 
     * 根据指定的图像路径和大小来获取缩略图 
     * 此方法有两点好处： 
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度， 
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 
     *        用这个工具生成的图像不会被拉伸。 
     * @param imagePath 图像的路径 
     * @param width 指定输出图像的宽度 
     * @param height 指定输出图像的高度 
     * @return 生成的缩略图 
     */  
    private Bitmap getImageThumbnail(Bitmap bitmap, String imagePath, int width, int height) {  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高，注意此处的bitmap为null  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        options.inJustDecodeBounds = false; // 设为 false  
        // 计算缩放比  
        int h = options.outHeight;  
        int w = options.outWidth;  
        int beWidth = w / width;  
        int beHeight = h / height;  
        int be = 1;  
        if (beWidth < beHeight) {  
            be = beWidth;  
        } else {  
            be = beHeight;  
        }  
        if (be <= 0) {  
            be = 1;  
        }  
        options.inSampleSize = be;  
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false  
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象  
       bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
	public void showMessage(String msg){
		Toast toast = Toast.makeText(instance, msg, Toast.LENGTH_LONG);
		toast.show();
	}
	
}
