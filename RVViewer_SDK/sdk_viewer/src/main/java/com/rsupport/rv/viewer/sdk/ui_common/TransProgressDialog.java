package com.rsupport.rv.viewer.sdk.ui_common;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsupport.rv.viewer.sdk.ui.ScreenCanvasMobileActivity;
import com.rsupport.rv.viewer.sdk_viewer.R;

import java.util.Objects;


/**<pre>*******************************************************************************
 *       ______   _____    __    __ _____   _____   _____    ______  _______
 *      / ___  | / ____|  / /   / // __  | / ___ | / __  |  / ___  ||___  __|
 *     / /__/ / | |____  / /   / // /  | |/ /  | |/ /  | | / /__/ /    / /
 *    / ___  |  |____  |/ /   / // /__/ // /__/ / | |  | |/ ___  |    / /
 *   / /   | |   ____| || |__/ //  ____//  ____/  | |_/ // /   | |   / /
 *  /_/    |_|  |_____/ |_____//__/    /__/       |____//_/    |_|  /_/
 *
 ********************************************************************************</pre>
 *
 * <b>Copyright (c) 2012 RSUPPORT Co., Ltd. All Rights Reserved.</b><p>
 *
 * <b>NOTICE</b> :  All information contained herein is, and remains the property
 * of RSUPPORT Company Limited and its suppliers, if any. The intellectual
 * and technical concepts contained herein are proprietary to RSUPPORT
 * Company Limited and its suppliers and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction
 * of this material is strictly forbidden unless prior written permission
 * is obtained from RSUPPORT Company Limited.<p>
 *
 * FileName: TransProgressDialog.java<br>
 * Author  : khkim<br>
 * Date    : 2014. 3. 5<br>
 * Purpose : 반투명 프로그래스 효과를 위한 Costum Dialog <p>
 *
 * [History]<p>
 */

public class TransProgressDialog extends Dialog {

	public static TransProgressDialog show(Context context, CharSequence title, String message) {
		return show(context, title, message, false);
	}

	public static TransProgressDialog show(Context context, CharSequence title, String message, boolean indeterminate) {
		return show(context, title, message, indeterminate, false);
	}

	public static TransProgressDialog show(Context context, CharSequence title, String message, boolean indeterminate, boolean cancelable) {
		return show(context, title, message, indeterminate, cancelable);
	}

	public static TransProgressDialog show(Context context, CharSequence title, String message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
		TransProgressDialog dialog = null;
		if ((context instanceof ScreenCanvasMobileActivity) && message == null) {
			dialog = new TransProgressDialog(context, R.style.CanvasProgressDialog);
			dialog.setContentView(R.layout.canvas_progress_dialog);
			
			LinearLayout tipLayout = (LinearLayout) dialog.findViewById(R.id.process_tip);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (context instanceof ScreenCanvasMobileActivity)
				tipLayout.setVisibility(View.INVISIBLE);
			
			View view = null;
//			int random = (int)(Math.random() * 3 + 1);
//			
//			switch (random) {
//			case 1:
//				((TextView) dialog.findViewById(R.id.progress_tip_title)).setText(R.string.remoteview_process_tip01_title);
//				((ImageView) dialog.findViewById(R.id.progress_tip_image)).setBackgroundResource(R.drawable.img_tip01);
//				((TextView) dialog.findViewById(R.id.progress_tip_message)).setText(R.string.remoteview_process_tip01_message);
//				break;
//				// HX 개발 일정 연기로 숨김 처리.
//			case 2:
//				((TextView) dialog.findViewById(R.id.progress_tip_title)).setText(R.string.remoteview_process_tip02_title);
//				((ImageView) dialog.findViewById(R.id.progress_tip_image)).setBackgroundResource(R.drawable.img_tip02);
//				((TextView) dialog.findViewById(R.id.progress_tip_message)).setText(R.string.remoteview_process_tip02_message);
//				break;	
//			default:
//				((TextView) dialog.findViewById(R.id.progress_tip_title)).setText(R.string.remoteview_process_tip03_title);
//				((ImageView) dialog.findViewById(R.id.progress_tip_image)).setBackgroundResource(R.drawable.img_tip03);
//				((TextView) dialog.findViewById(R.id.progress_tip_message)).setText(R.string.remoteview_process_tip03_message);
//			}
			
			((TextView) dialog.findViewById(R.id.progress_tip_title)).setText(R.string.remoteview_process_tip01_title);
			((ImageView) dialog.findViewById(R.id.progress_tip_image)).setBackgroundResource(R.drawable.img_tip01);
			((TextView) dialog.findViewById(R.id.progress_tip_message)).setText(R.string.remoteview_process_tip01_message);
			
			dialog.setCancelable(cancelable);
			dialog.setOnCancelListener(cancelListener);
			
		} else {
			dialog = new TransProgressDialog(context);
			dialog.setContentView(R.layout.progress_dialog);

			TextView progressText = (TextView) dialog.findViewById(R.id.progress_text);
			if(progressText != null) {
				progressText.setText(message);
				progressText.setTextColor(context.getResources().getColor(R.color.color04));
			}
			
			dialog.setCancelable(cancelable);
			dialog.setOnCancelListener(cancelListener);
		}

		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		// dialog.setTitle(title);
		
		// rotation animation
		ImageView progressImage = (ImageView) dialog.findViewById(R.id.progress_image);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.progress_rotate);
		progressImage.startAnimation(animation);
		
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

		dialog.show();
		return dialog;
	}

	public static TransProgressDialog simple(Context context) {
		TransProgressDialog dialog = new TransProgressDialog(context);

		dialog.setContentView(R.layout.progress_dialog);

		Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		ImageView progressImage = dialog.findViewById(R.id.progress_image);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.progress_rotate);

		progressImage.startAnimation(animation);

		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

		return dialog;
	}

	public TransProgressDialog(Context context) {
		super(context, R.style.ProgressDialog);
	}
	
	public TransProgressDialog(Context context, int style) {
		super(context, style);
	}
}
