package com.jwzhangjie.videoplayer.share;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapterPhoto extends BaseAdapter {
	private Context mContext;
	private ArrayList<Bitmap> photos = new ArrayList<Bitmap>();

	/**
	 * @param context
	 */
	public ImageAdapterPhoto(Context context) {
		mContext = context;
	}

	public void addPhoto(Bitmap photo) {
		photos.add(photo);
	}
	/**
	 * 当删除图片时，局部更新数据
	 * @param postion
	 */
	public void removePhoto(int postion){
		if (postion < getCount()) {
			photos.remove(postion);
			this.notifyDataSetChanged();
		}
	}
	
	public int getCount() {
		return photos.size();
	}

	public Object getItem(int position) {
		return photos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		imageView = new ImageView(mContext);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageBitmap(photos.get(position));

		return imageView;
	}
}
