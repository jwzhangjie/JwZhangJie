package com.jwzhangjie.videoplayer.dialog;

import com.jwzhangjie.videoplayer.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

public class wifi_not_connect {
	public wifi_not_connect() {
		
	}
	public static AlertDialog.Builder createwificonnectDialog(Context context){
		final AlertDialog.Builder connectDialogwifi = new AlertDialog.Builder(new ContextThemeWrapper(context, 
				R.layout.share_dialog));
		connectDialogwifi.setTitle(R.string.share_net_error_title);
		connectDialogwifi.setMessage(R.string.share_net_error_content);
		connectDialogwifi .setCancelable(false);
		connectDialogwifi .setPositiveButton(R.string.share_net_error_done,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return connectDialogwifi;
	}

}