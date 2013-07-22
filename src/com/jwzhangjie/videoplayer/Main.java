package com.jwzhangjie.videoplayer;

import com.jwzhangjie.videoplayer.circlemenu.view.CircleImageView;
import com.jwzhangjie.videoplayer.circlemenu.view.CircleLayout;
import com.jwzhangjie.videoplayer.circlemenu.view.CircleLayout.OnItemClickListener;
import com.jwzhangjie.videoplayer.circlemenu.view.CircleLayout.OnItemSelectedListener;
import com.jwzhangjie.videoplayer.online.WebVideo;
import com.jwzhangjie.videoplayer.util.AppLog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnItemSelectedListener, OnItemClickListener{
	TextView selectedTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppLog.enableLogging(true);
		CircleLayout circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView)circleMenu.getSelectedItem()).getName());
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {	
		selectedTextView.setText(name);
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_app) + " " + name, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		switch(position){
		case 0:
			intent.setClass(Main.this, JieVideo.class);
			startActivity(intent);
			break;
		case 2:
			intent.setClass(Main.this, WebVideo.class);
			startActivity(intent);
			break;
		}
	}

}