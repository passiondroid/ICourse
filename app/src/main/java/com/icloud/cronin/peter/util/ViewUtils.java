package com.icloud.cronin.peter.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.icloud.cronin.peter.R;

public class ViewUtils {

	public static Dialog showSyncDialog(Context context) {
		final Dialog dialog = new Dialog(context);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		//Button btn = (Button) dialog.findViewById(R.id.btn);

		//TextView textView = (TextView) dialog.findViewById(R.id.txt01);
		//textView.setText(message);
		/*if(dialogBtnListener == null) {
			btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		}*/
		//dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}

}
