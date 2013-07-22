package com.seuic.share;

import com.jwzhangjie.videoplayer.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeleteDialog extends Dialog{
	private int i;
	public DeleteDialog(Context context) {
		super(context);
	}
	public DeleteDialog(Context context, int theme ,int inter){
        super(context, theme);
        this.i = inter;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_dialog);
		
		Button positiveBtn = (Button) findViewById(R.id.positiveButton);
		Button cancelBtn = (Button) findViewById(R.id.cancelButton);
		TextView text = (TextView) findViewById(R.id.message);
		if(i == 1){
			text.setText(R.string.share_delete_photo);
		}else if(i == 2){
			text.setText(R.string.share_delete_video);
		}
		
		positiveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
				if(i == 1){
					ImageGalleryActivity.getInstance().Delete_photo();
				}else if(i == 2){
					VideoGalleryActivity.getInstance().Delete_video();
				}
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss(); 
			}
		});
	}
	
}