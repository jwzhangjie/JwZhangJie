package com.jwzhangjie.videoplayer.share;

import java.util.ArrayList;

import com.jwzhangjie.videoplayer.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapterVideo extends BaseAdapter {

	private Context mContext;
	private ArrayList<Bitmap> videos = new ArrayList<Bitmap>();
	LayoutInflater inflater;
	/**
	 * @param context
	 */
	public ImageAdapterVideo(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public void addPhoto(Bitmap photo) {
		videos.add(photo);
	}
	
	public void removePhoto(int position){
		if (position < getCount()) {
			videos.remove(position);
			notifyDataSetChanged();
		}
	}
	
	public int getCount() {
		return videos.size();
	}

	public Object getItem(int position) {
		return videos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.video_view_item, null);
			holderView  = new HolderView();
			holderView.imgv = (ImageView)convertView.findViewById(R.id.imageView_video_view);
			convertView.setTag(holderView);
		}else {
			holderView = (HolderView)convertView.getTag();
		}
		holderView.imgv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		holderView.imgv.setImageBitmap(videos.get(position));
		return convertView;
	}
	class HolderView{
		ImageView imgv;
	}
}
