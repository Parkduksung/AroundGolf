package com.rsupport.rv.viewer.sdk.ui;

import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_ALERT_CANCEL;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_ALERT_CLOSE;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_ALERT_CONNCLOSE;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_CONNECT_ERROR;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_FORCE_CONNECT_NO;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_FORCE_CONNECT_YES;
import static com.rsupport.rv.viewer.sdk.agent.IEventIds.EVENT_ID_RESOLUTION_RESTRICTION;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rsupport.rv.viewer.sdk.common.CanvasProxy;
import com.rsupport.rv.viewer.sdk.common.ComConstant;
import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;
import com.rsupport.rv.viewer.sdk.setting.PreferenceConstant;
import com.rsupport.rv.viewer.sdk.ui.remote.RemoteMobileViewModel;
import com.rsupport.rv.viewer.sdk.ui.remote.RemoteViewModel;
import com.rsupport.rv.viewer.sdk.ui_common.TransProgressDialog;
import com.rsupport.rv.viewer.sdk.util.AgentInfo;
import com.rsupport.rv.viewer.sdk.util.DisplayUtil;
import com.rsupport.rv.viewer.sdk.view.ScreenCanvasGLSurfaceViewHX;
import com.rsupport.rv.viewer.sdk_viewer.R;

import java.util.Timer;
import java.util.TimerTask;

public class ScreenCanvasMobileActivity extends AppCompatActivity implements OnGestureListener, OnDoubleTapListener {
	private final int MARGIN_UP = 0;
	private final int RIGHT_MARGIN_DOWN = 54;
	private final int TOP_MARGIN_DOWN = 54;
	private final int RIGHT_MARGIN_DOWN_TABLET = 80;
	private final int TOP_MARGIN_DOWN_TABLET = 80;

	private Handler uiHandler = new Handler();

	private int toolPressY;
	private int toolTransY;

	private long lastTouchTime;

	private boolean isForceClosing;
	private boolean isClientDrawMode;
	private boolean isToolMenuUp = false;
	private boolean isCloseConnection = false;
	private boolean isNotification = true;
	private boolean isToolMoved;
	private Boolean isTwoFinger = false;

	private AgentInfo currentAgent;
	private Timer controlCheckTimer;
	private GestureDetector gestureScanner;
	private ScreenCanvasGLSurfaceViewHX canvasSurfaceHX;
	private TransProgressDialog connectionDialog;

	private final Rect windowRect = new Rect();
	private View controlView;
	private ImageView controlButton;
	private ImageView controlMenu;
	private ImageView controlHome;
	private ImageView controlBack;
	private LinearLayout controlBox;
	private View toolsView;
	private ImageView toolsCloseButton;
	private ImageView pointerButton;
	private ImageView drawButton;
	private ImageView notifyButton;
	private ImageView drawEraserButton;
	private ImageView exitButton;
	private RelativeLayout toolsBox;
	private RelativeLayout canvasBox;
	private TranslateAnimation upAnimation;
	private TranslateAnimation downAnimation;

	private RemoteMobileViewModel remoteViewModel;
	private RemoteViewModel.RemoteStatus.ForceConnectAsking forceConnectAsking;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RLog.d("ScreenCanvasMobileActivity : onCreate");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


		setContentView(R.layout.canvas_mobile);
//		hideTitle();
		Global.GetInstance().setCurrentActivity(this);

		remoteViewModel = new ViewModelProvider(this).get(RemoteMobileViewModel.class);
		getLifecycle().addObserver(remoteViewModel);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		gestureScanner = new GestureDetector(this);

		setProgressBarVisibility(true);

		canvasSurfaceHX = (ScreenCanvasGLSurfaceViewHX) findViewById(R.id.canvas_surface_hx);
		currentAgent = RuntimeData.getInstance().getLastAgentInfo();
		canvasSurfaceHX.setVisibility(View.VISIBLE);

		Global.GetInstance().setScreenController(canvasSurfaceHX);
		Global.GetInstance().setMobileViewer(this);

		canvasBox = (RelativeLayout) findViewById(R.id.canvasBox);

//		initControlView();
//		initToolsView();

		remoteViewModel.getCanvasStatusLiveData().observe(this, this::onCanvasStatusChanged);
		remoteViewModel.getRemoteStatusLiveData().observe(this, this::onRemoteSessionStatusChanged);

		if(savedInstanceState == null) {
			remoteViewModel.setConnectable();
			showConnectionDialog();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		initLockTime();
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		RLog.i("onDestroy");

		canvasSurfaceHX.release();
		canvasSurfaceHX.stopDecoder();

		hideConnectionDialog();
		super.onDestroy();
	}

	public Rect getWindowRect() {
		return windowRect;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		lastTouchTime = System.currentTimeMillis();

		if(e.getAction() == MotionEvent.ACTION_DOWN) {
			getWindow().getDecorView().getWindowVisibleDisplayFrame(windowRect);
		}

		if(e.getPointerCount() >= 2) {
			isTwoFinger = true;
		}

		if(isClientDrawMode == false) {
			canvasSurfaceHX.onCustomTouchEventEx(e, false, false, true, 0, 0, windowRect.left);
			return gestureScanner.onTouchEvent(e);
		}

		if(!isTwoFinger){
			CanvasProxy.getInstance().onDrawTouchEvent(e);
		}

		if(e.getAction() == KeyEvent.ACTION_UP){
			isTwoFinger = false;
		}

		return true;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			showAlertDialogOkCancel(GlobalStatic.ALERT_CLOSE);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void eventDelivery(int event){

		switch(event) {
			case EVENT_ID_CONNECT_ERROR :
//				closeConnectionThread();
				GlobalStatic.g_errNumber = 0;
				GlobalStatic.CMD_PROC_ERR = 0;
				break;
			case EVENT_ID_FORCE_CONNECT_NO:
				if(forceConnectAsking != null){
					forceConnectAsking.getDenied().invoke();
					forceConnectAsking = null;
				}
				break;
			case EVENT_ID_FORCE_CONNECT_YES:
				if(forceConnectAsking != null){
					forceConnectAsking.getAccept().invoke();
					forceConnectAsking = null;
				}
				break;
			case EVENT_ID_ALERT_CONNCLOSE :
//				closeConnectionThread();
				break;
			case EVENT_ID_ALERT_CLOSE :
				remoteViewModel.disconnect();
				break;
			case EVENT_ID_RESOLUTION_RESTRICTION :
			case EVENT_ID_ALERT_CANCEL :
			default :
		}
	}

	private void onCanvasStatusChanged(RemoteViewModel.CanvasStatus canvasStatus) {
		final int bitCount = 16;
		final int bitmapWidth = GlobalStatic.x264VideoHeader.getWidth();
		final int bitmapHeight = GlobalStatic.x264VideoHeader.getHeight();
		canvasSurfaceHX.resetScreen(bitmapWidth, bitmapHeight, bitCount);
		canvasSurfaceHX.setInitScreen(bitmapWidth, bitmapHeight, bitCount);
		rotationCanvasUI(Configuration.ORIENTATION_PORTRAIT);
	}

	private void onRemoteSessionStatusChanged(RemoteViewModel.RemoteStatus remoteStatus) {
		if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Connecting){
			showConnectionDialog();
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Connected){
			onRemoteSessionConnected();
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Recovering){
//			showProgressHandler(getString(R.string.remotepc_loading_waiting_message));
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Recovered){
//			hideProgressHandler();
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Disconnecting){
//			showProgressHandler(getString(R.string.remotepc_loading_waiting_message));
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Disconnected){
			exceptionForceCloseConnectionThread(false);
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.ForceConnectAsking){
			showConnectionDialog();
			forceConnectAsking = (RemoteViewModel.RemoteStatus.ForceConnectAsking)remoteStatus;
			showMessageForceConnectAgent(getString(R.string.cmderr_already_remote_control));
		}
		else if(remoteStatus instanceof RemoteViewModel.RemoteStatus.Error){
			onConnectError((RemoteViewModel.RemoteStatus.Error)remoteStatus);
		}
	}

	private void onRemoteSessionConnected() {
		hideConnectionDialog();
		rotationCanvasUI(Configuration.ORIENTATION_PORTRAIT);
	}

	private void onConnectError(RemoteViewModel.RemoteStatus.Error remoteErrorStatus) {
		RLog.e(remoteErrorStatus.getException());
		final int errorCode = remoteErrorStatus.getException().getErrorCode();
		if (errorCode == ComConstant.CMD_ERR_NOT_ALLOWED_IP_ADDRESS) {
			showMessageInfoOnConnectAgent(getString(R.string.cmderr_not_allowed_ip_address));
		} else if (errorCode == ComConstant.WEB_ERR_AGENT_NOT_LOGIN) {
			GlobalStatic.g_errNumber = ComConstant.WEB_ERR_AGENT_NOT_LOGIN;
			currentAgent.status = ComConstant.AGENT_STATUS_NOLOGIN;
			showErrDialogHandler();
		} else if (errorCode == ComConstant.CMD_ERR_SESSION_SOCKET_FAIL) {
			GlobalStatic.g_errNumber = ComConstant.CMD_ERR_SESSION_SOCKET_FAIL;
			currentAgent.status = ComConstant.AGENT_STATUS_NOLOGIN;
			showErrDialogHandler();
		} else if (GlobalStatic.CMD_PROC_ERR == ComConstant.CMD_ERR_NOAGENT) {
			// No Agent 뷰 보여주기
			GlobalStatic.g_errNumber = ComConstant.CMD_ERR_NOAGENT;
			showErrDialogHandler();
		} else {
			showErrDialogHandler();
			GlobalStatic.CMD_PROC_ERR = 0;
		}
	}

	private void showConnectionDialog() {
		RLog.v("showConnectionDialog");
		if(connectionDialog == null || !connectionDialog.isShowing()){
			connectionDialog = TransProgressDialog.show(this, null, null, true, false, null);
		}
	}

	private void hideConnectionDialog() {
		RLog.v("hideConnectionDialog");
		if(connectionDialog != null){
			connectionDialog.dismiss();
			connectionDialog = null;
		}
	}

//	public void closeConnectionThread() {
//		showProgressHandler(getString(R.string.remotepc_loading_waiting_message));
//		Thread th = new Thread() {
//			public void run() {
//				closeConnection();
//				hideProgressHandler();
//			}
//		};
//		th.start();
//	}

	public void exceptionForceCloseConnectionThread(final boolean isTimeout) {
		Thread th = new Thread() {
			public void run() {

				if (!isCloseConnection) {
					if (isTimeout){
//						showToast(getString(R.string.msg_timeover_net_status_check));
					} else {
						if(RuntimeData.getInstance().appProperty.isWebserver5x()) {
//							showToast(getString(R.string.msg_disconnect));
						}
					}
				}

				isCloseConnection = true;

				try {
					sleep(2000);
				} catch (Exception e) {}
				forceCloseConnection();
			}
		};
		th.start();
	}

	private void initToolsView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		toolsView = inflater.inflate(R.layout.hyoja_toolsview, null);
//
//		toolsBox = (RelativeLayout) toolsView.findViewById(R.id.tools_box);
//		toolsCloseButton = (ImageView) toolsView.findViewById(R.id.tools_close);
//		pointerButton = (ImageView) toolsView.findViewById(R.id.tools_pointer);
//		drawButton = (ImageView) toolsView.findViewById(R.id.tools_draw);
//		drawEraserButton = (ImageView) toolsView.findViewById(R.id.tools_draw_eraser);
//		notifyButton = (ImageView) toolsView.findViewById(R.id.tools_notify_bar);
//		exitButton = (ImageView) toolsView.findViewById(R.id.tools_exit);

		toolsCloseButton.setOnTouchListener(toolsButtonTouchListener);
		pointerButton.setOnTouchListener(toolsButtonTouchListener);
		drawButton.setOnTouchListener(toolsButtonTouchListener);
		notifyButton.setOnTouchListener(toolsButtonTouchListener);
		drawEraserButton.setOnTouchListener(toolsButtonTouchListener);
		exitButton.setOnTouchListener(toolsButtonTouchListener);
	}

	private void setToolsEvent(int toolsBt) {
		if (pointerButton.getId() == toolsBt && isToolMenuUp) {
			pointerButtonEvent();
			pointerButton.setSelected(GlobalStatic.isPointerVisible);
		} else if (drawButton.getId() == toolsBt && isToolMenuUp) {
			drawModeButtonEvent();
			drawButton.setSelected(isClientDrawMode);
			drawEraserButton.setSelected(GlobalStatic.isDrawEraser);
		} else if (notifyButton.getId() == toolsBt && isToolMenuUp) {
			notifyButtonEvent();
		} else if (drawEraserButton.getId() == toolsBt && isToolMenuUp) {
			if (isClientDrawMode) {
				drawEraserButtonEvent();
				drawEraserButton.setSelected(GlobalStatic.isDrawEraser);
			} else {
//				REToast.show(this, "Draw Mode On Need!!");
			}
		} else if (exitButton.getId() == toolsBt && isToolMenuUp) {
			exitButtonEvent();
		} else if (toolsCloseButton.getId() == toolsBt) {
			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolsBox.getLayoutParams();
			boolean isUp = false;
			isUp = layoutParams.rightMargin == DisplayUtil.Dip2Pixel(this, MARGIN_UP);
			toolsBox.startAnimation(isUp ? downAnimation : upAnimation);
			isToolMenuUp = !isUp;
		}
	}

	private void startControlViewTimer() {
		lastTouchTime = System.currentTimeMillis();
		controlCheckTimer = new Timer();
		controlCheckTimer.schedule(new TimerTask() {
			public void run() {
				long waitTime = System.currentTimeMillis() - lastTouchTime;

				if (waitTime > 5000) {
					dealdyHideControlViewHandler.sendEmptyMessage(0);
					stopControlViewTimer();
				}
			}
		}, 0, 1000);
	}

	private void stopControlViewTimer() {
		if (controlCheckTimer != null) {
			controlCheckTimer.cancel();
			controlCheckTimer = null;
		}
	}

	private void pointerButtonEvent() {
		RLog.i("pointerButtonEvent");

		if (GlobalStatic.isPointerVisible == false) {
			Global.GetInstance().getDataChannel().reqPointerStart();
			GlobalStatic.isPointerVisible = true;
			pointerButton.setImageResource(R.drawable.btn_pointer_icon_selected);

			isClientDrawMode = false;
			Global.GetInstance().getDataChannel().reqDrawStop();
			drawButton.setSelected(false);
			drawButton.setImageResource(R.drawable.btn_draw_icon_normal);

			GlobalStatic.isDrawEraser = false;
			drawEraserButton.setImageResource(R.drawable.btn_eraser_icon_normal);
			drawEraserButton.setSelected(false);
		} else {
			Global.GetInstance().getDataChannel().reqPointerStop();
			GlobalStatic.isPointerVisible = false;
			pointerButton.setImageResource(R.drawable.btn_pointer_icon_normal);
		}
	}

	private void drawModeButtonEvent() {
		RLog.i("drawButtonEvent");
		if (Global.GetInstance().getDataChannel() == null) return;

		GlobalStatic.isPointerVisible = false;
		pointerButton.setImageResource(R.drawable.btn_pointer_icon_normal);
		pointerButton.setSelected(GlobalStatic.isPointerVisible);

		if (isClientDrawMode) {

			if (GlobalStatic.isDrawEraser) {
				GlobalStatic.isDrawEraser = false;
				drawEraserButton.setImageResource(R.drawable.btn_eraser_icon_normal);
				drawButton.setImageResource(R.drawable.btn_draw_icon_selected);
			} else {
				isClientDrawMode = false;
				Global.GetInstance().getDataChannel().reqDrawStop();
				drawButton.setImageResource(R.drawable.btn_draw_icon_normal);
			}

		} else {
			isClientDrawMode = true;
			Global.GetInstance().getDataChannel().reqDrawStart();
			drawButton.setImageResource(R.drawable.btn_draw_icon_selected);
		}

		drawEraserButton.setImageResource(R.drawable.btn_eraser_icon_normal);
	}

	private void notifyButtonEvent() {
		if (isNotification) {
			downNotification();
		} else {
			upNotification();
		}
	}

	private void drawEraserButtonEvent() {
		RLog.i("drawEraserButtonEvent");

		if (GlobalStatic.isDrawEraser) {
			isClientDrawMode = false;
			GlobalStatic.isDrawEraser = false;
			drawEraserButton.setImageResource(R.drawable.btn_eraser_icon_normal);
			if (Global.GetInstance().getDataChannel() == null) return;
			Global.GetInstance().getDataChannel().reqDrawStop();
		} else {
			drawButton.setSelected(false);
			drawButton.setImageResource(R.drawable.btn_draw_icon_normal);
			GlobalStatic.isDrawEraser = true;
			drawEraserButton.setImageResource(R.drawable.btn_eraser_icon_selected);
		}
	}

	private void downNotification() {
		Global.GetInstance().getDataChannel().reqNotificationDown();
		isNotification = false;
	}

	private void upNotification() {
		Global.GetInstance().getDataChannel().reqNotificationUp();
		isNotification = true;
	}

	private void exitButtonEvent() {
		RLog.e("exitButtonEvent");
		showAlertDialogOkCancel(GlobalStatic.ALERT_CLOSE);
	}

	private void initControlView() {
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		controlView = inflater.inflate(R.layout.hyoja_controlview, null);
//
//		controlBox = (LinearLayout) controlView.findViewById(R.id.control_box);
//		controlButton = (ImageView) controlView.findViewById(R.id.control_btn);
//		controlMenu = (ImageView) controlView.findViewById(R.id.control_menu);
//		controlHome = (ImageView) controlView.findViewById(R.id.control_home);
//		controlBack = (ImageView) controlView.findViewById(R.id.control_back);
//
//		controlButton.setOnTouchListener(controlTouchListener);
//		controlMenu.setOnTouchListener(controlTouchListener);
//		controlHome.setOnTouchListener(controlTouchListener);
//		controlBack.setOnTouchListener(controlTouchListener);
	}

	private void initLockTime() {
		remoteViewModel.updateUserActionTime();
	}

	private void rotationCanvasUI(int orientation) {
		canvasBox.removeView(controlView);
		canvasBox.removeView(toolsView);

		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			int rightMarginDown = -RIGHT_MARGIN_DOWN;


			upAnimation = makeAnimationX(rightMarginDown, MARGIN_UP);
			downAnimation = makeAnimationX(MARGIN_UP, rightMarginDown);

			RelativeLayout.LayoutParams viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			viewparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			viewparam.addRule(RelativeLayout.CENTER_HORIZONTAL);

			controlBox.setOrientation(LinearLayout.HORIZONTAL);
			controlBox.setBackgroundResource(R.drawable.bg_hardware_vertical);

//			controlView.findViewById(R.id.line_vertical1).setVisibility(View.VISIBLE);
//			controlView.findViewById(R.id.line_vertical2).setVisibility(View.VISIBLE);
//			controlView.findViewById(R.id.line_horizontal1).setVisibility(View.GONE);
//			controlView.findViewById(R.id.line_horizontal2).setVisibility(View.GONE);

			canvasBox.addView(controlView, viewparam);

			viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			viewparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			toolsBox.setLayoutParams(viewparam);

//			LinearLayout toolsMenus = (LinearLayout) toolsView.findViewById(R.id.tools_menus);
//			toolsMenus.setBackgroundResource(R.drawable.bg_toolbar_horizontal);

			setRightMargin(toolsBox, rightMarginDown);
//			toolsMenus.setOrientation(LinearLayout.VERTICAL);

			RelativeLayout.LayoutParams toolsBoxParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
//			toolsBoxParam.addRule(RelativeLayout.ALIGN_LEFT, toolsMenus.getId());
			toolsBoxParam.addRule(RelativeLayout.CENTER_VERTICAL);
			toolsCloseButton.setBackgroundResource(R.drawable.toolbar_flag_horizontal_transparency);
			toolsCloseButton.setLayoutParams(toolsBoxParam);

			viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			viewparam.addRule(RelativeLayout.CENTER_VERTICAL);

			canvasBox.addView(toolsView, viewparam);

			rotateToolbarLine(true);
		} else {
			int topMarginDown = -TOP_MARGIN_DOWN;

			upAnimation = makeAnimationY(topMarginDown, MARGIN_UP);
			downAnimation = makeAnimationY(MARGIN_UP, topMarginDown);

			RelativeLayout.LayoutParams viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			viewparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			viewparam.addRule(RelativeLayout.CENTER_VERTICAL);

			controlBox.setOrientation(LinearLayout.VERTICAL);
			controlBox.setBackgroundResource(R.drawable.bg_hardware_horizontal);

//			controlView.findViewById(R.id.line_vertical1).setVisibility(View.GONE);
//			controlView.findViewById(R.id.line_vertical2).setVisibility(View.GONE);
//			controlView.findViewById(R.id.line_horizontal1).setVisibility(View.VISIBLE);
//			controlView.findViewById(R.id.line_horizontal2).setVisibility(View.VISIBLE);

			canvasBox.addView(controlView, viewparam);

			viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			viewparam.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			toolsBox.setLayoutParams(viewparam);

//			LinearLayout toolsMenus = (LinearLayout) toolsView.findViewById(R.id.tools_menus);
//			toolsMenus.setBackgroundResource(R.drawable.bg_toolbar_vertical);

			setTopMargin(toolsBox, topMarginDown);
//			toolsMenus.setOrientation(LinearLayout.HORIZONTAL);

			RelativeLayout.LayoutParams toolsBoxParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
//			toolsBoxParam.addRule(RelativeLayout.ALIGN_BOTTOM, toolsMenus.getId());
			toolsBoxParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
			toolsCloseButton.setBackgroundResource(R.drawable.toolbar_flag_vertical_transparency);
			toolsCloseButton.setLayoutParams(toolsBoxParam);

			viewparam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			viewparam.addRule(RelativeLayout.CENTER_HORIZONTAL);

			canvasBox.addView(toolsView, viewparam);

			rotateToolbarLine(false);
		}
	}

	private void rotateToolbarLine(boolean isVertical) {
		int resId = 0;
		LinearLayout.LayoutParams params = null;

		if (isVertical) {
			resId = R.drawable.line_horizontal;
			params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					getResources().getDimensionPixelSize(R.dimen.line_1));
		} else {
			resId = R.drawable.line_vertical;
			params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.line_1),
					ViewGroup.LayoutParams.MATCH_PARENT);
		}
//
//		toolsView.findViewById(R.id.toolbar_line1).setBackgroundResource(resId);
//		toolsView.findViewById(R.id.toolbar_line2).setBackgroundResource(resId);
//		toolsView.findViewById(R.id.toolbar_line3).setBackgroundResource(resId);
//		toolsView.findViewById(R.id.toolbar_line4).setBackgroundResource(resId);
//		toolsView.findViewById(R.id.toolbar_line1).setLayoutParams(params);
//		toolsView.findViewById(R.id.toolbar_line2).setLayoutParams(params);
//		toolsView.findViewById(R.id.toolbar_line3).setLayoutParams(params);
//		toolsView.findViewById(R.id.toolbar_line4).setLayoutParams(params);
	}

	private void setControlEvent(int toolsBt) {
		if (controlMenu.getId() == toolsBt) {
			canvasSurfaceHX.sendMonkeyKeycode(KeyEvent.KEYCODE_APP_SWITCH);

		} else if (controlHome.getId() == toolsBt) {
			canvasSurfaceHX.sendMonkeyKeycode(KeyEvent.KEYCODE_HOME);

		} else if (controlBack.getId() == toolsBt) {
			canvasSurfaceHX.sendMonkeyKeycode(KeyEvent.KEYCODE_BACK);

		} else if (controlButton.getId() == toolsBt) {
			controlButtonEvent(false);
		}
	}

	private void controlButtonEvent(boolean isOverTouch) {
		if (controlBox.getVisibility() == View.VISIBLE || isOverTouch) {
			controlButton.setVisibility(View.VISIBLE);
			controlButton.setAnimation(DisplayUtil.getAnimation(this, R.anim.toast_enter));
			controlBox.setVisibility(View.GONE);
			stopControlViewTimer();
		} else {
			controlButton.setVisibility(View.GONE);
			controlBox.setVisibility(View.VISIBLE);
			controlBox.setAnimation(DisplayUtil.getAnimation(this, R.anim.toast_enter));
			startControlViewTimer();
		}
	}

	private TranslateAnimation makeAnimationX(final int fromMargin, final int toMargin) {
		TranslateAnimation animation =
				new TranslateAnimation(0, DisplayUtil.Dip2Pixel(this, fromMargin - toMargin), 0, 0);
		animation.setDuration(250);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				// Cancel the animation to stop the menu from popping back.
				toolsBox.clearAnimation();

				// Set the new bottom margin.
				setRightMargin(toolsBox, toMargin);
			}

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
		return animation;
	}

	private TranslateAnimation makeAnimationY(final int fromMargin, final int toMargin) {
		TranslateAnimation animation =
				new TranslateAnimation(0, 0, 0, DisplayUtil.Dip2Pixel(this, toMargin - fromMargin));
		animation.setDuration(250);
		animation.setAnimationListener(new Animation.AnimationListener()
		{
			public void onAnimationEnd(Animation animation)
			{
				// Cancel the animation to stop the menu from popping back.
				toolsBox.clearAnimation();

				// Set the new bottom margin.
				setTopMargin(toolsBox, toMargin);
			}

			public void onAnimationStart(Animation animation) {}

			public void onAnimationRepeat(Animation animation) {}
		});
		return animation;
	}

	private void setRightMargin(View view, int rightMarginInDips) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
		layoutParams.topMargin = MARGIN_UP;
		layoutParams.rightMargin = DisplayUtil.Dip2Pixel(this, rightMarginInDips);
		view.requestLayout();
	}

	private void setTopMargin(View view, int topMarginInDips) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
		layoutParams.rightMargin = MARGIN_UP;
		layoutParams.topMargin = DisplayUtil.Dip2Pixel(this, topMarginInDips);
		view.requestLayout();
	}

	private synchronized void forceCloseConnection() {
		if (isForceClosing || Global.GetInstance() == null) return;
		isForceClosing = true;

		remoteViewModel.disconnect();
		try {
			canvasSurfaceHX.release();
			canvasSurfaceHX.stopDecoder();
		} catch (NullPointerException e) {
		}

    	clearDirtyValue();

		System.gc();

		exitSystem();

    	isForceClosing = false;
    }

	private void makeSessionRelease() {
		if (Global.GetInstance().getScreenChannel() != null && Global.GetInstance().getScreenChannel().getSocket() != null) {
			Global.GetInstance().getScreenChannel().getSocket().setMakeRunningSession(false);
		}
		if (Global.GetInstance().getDataChannel() != null && Global.GetInstance().getDataChannel().getSocket() != null) {
			Global.GetInstance().getDataChannel().getSocket().setMakeRunningSession(false);
		}
	}

	private synchronized void closeConnection() {
    	RLog.d("closeConnection");
    	remoteViewModel.disconnect();

		if (Global.GetInstance().getDataChannel() != null) {
			Global.GetInstance().getDataChannel().sendPacket(channelConstants.rcpX264Stream, channelConstants.rcpX264StreamStop);

			makeSessionRelease();
		}

		try {
			canvasSurfaceHX.release();
			canvasSurfaceHX.stopDecoder();
		} catch (NullPointerException e) {
		}

    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    	clearDirtyValue();

		exitSystem();
    }

	private void exitSystem() {
		System.gc();
//    	startBackActivity(this);
    	finish();
	}

    private void clearDirtyValue() {
		GlobalStatic.unregisterPushMessaging(getApplicationContext(), RuntimeData.getInstance().getLastAgentInfo().guid);

    	GlobalStatic.G_ISCONNECTINGSESSION = false;
    	GlobalStatic.G_CONNECTEDSESSION = false;
    	GlobalStatic.g_canvasRatio = 0;

    	if (Global.GetInstance().getBitmap() != null) {
        	Global.GetInstance().setBitmap(null);
    	}
		GlobalStatic.g_errNumber = 0;
		GlobalStatic.CMD_PROC_ERR = 0;
    	GlobalStatic.sourceBitmapPixels = null;
		Global.GetInstance().setMobileViewer(null);
    	Global.GetInstance().setSendAction(null);
    	Global.GetInstance().setDataChannel(null);
		GlobalStatic.ISSAVERESOLUTION = false;
    	Global.GetInstance().setRemoteMonitorInfo(null);
    	GlobalStatic.ISVIEWGUIDETYPE = GlobalStatic.VIEWGUIDE_TOUCH;
    	GlobalStatic.SCREENSET_LOCKMONITOR = false;

		GlobalStatic.waitShowTime = 0;
		GlobalStatic.isDrawEraser = false;
		GlobalStatic.isPointerVisible = false;
    }

	private void showErrDialogHandler() {
		uiHandler.post(showErrDialog);
	}

	private void showErrDialog(String message) {
		if (message == null || message.length() < 2) {
			message = "ScreenCanvasMobile";
		}

//		showAlertDialog(null, message, RVDialog.STYLE_NOTICE, R.string.common_ok, EVENT_ID_CONNECT_ERROR);
	}

	private void showMessageInfoOnConnectAgent(String message) {
//		showAlertDialog(null, message, RVDialog.STYLE_NOTICE, R.string.common_ok, EVENT_ID_CONNECT_ERROR);
	}

	private void showMessageForceConnectAgent(String message) {
//		showAlertDialog(null, message, RVDialog.STYLE_NOTICE,	R.string.common_ok, EVENT_ID_FORCE_CONNECT_YES,
//																R.string.rv_cancel, EVENT_ID_FORCE_CONNECT_NO);
	}

//	private void startBackActivity(Context context) {
//		if (RuntimeData.getInstance().getLastAgentInfo().type == AgentInfo.VIRTUAL_AGENT) {
//			startBackLoginActivity(context);
//			return;
//		}
////		setResult(AgentCommon.EVENT_ID_ALERT_CONNCLOSE,AgentListActivity.getLastListActivityIntent(this));
//	}

//	private void startBackLoginActivity(Context context) {
////		Intent intent = new Intent(context, rsupport.androidViewer.login.MainActivity.class);
//		intent.putExtra(PreferenceConstant.RV_BUNDLE_ACTIVITY_BACK, PreferenceConstant.RV_TRUE);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		context.startActivity(intent);
//	}

	private String getAlertMessage(short type) {
    	String ret = "";
		switch (type) {
		case GlobalStatic.ALERT_LOGOFF:
			ret = getString(R.string.msg_logoff);
			break;
		case GlobalStatic.ALERT_RESTART:
			ret = getString(R.string.msg_restart);
			break;
		case GlobalStatic.ALERT_WINCLOSE:
			ret = getString(R.string.msg_remoteclose);
			break;
		case GlobalStatic.ALERT_CONNCLOSE:
			ret = getString(R.string.msg_endsession);
			break;
		case GlobalStatic.ALERT_CLOSE:
			ret = getString(R.string.msg_shotdown);
			break;
		case GlobalStatic.ALERT_DISCONNECT :
			ret = getString(R.string.msg_menu_disconnect);
			break;
		case GlobalStatic.ALERT_RESOLUTION :
			ret = getString(R.string.msg_display_resolution_restriction);
			break;
		}
		return ret;
    }

	private void showAlertDialogOkCancel(short type){
		int eventType = EVENT_ID_ALERT_CANCEL;

		switch (type) {
		case GlobalStatic.ALERT_CONNCLOSE:
			eventType = EVENT_ID_ALERT_CONNCLOSE;
			break;
		case GlobalStatic.ALERT_CLOSE:
			eventType = EVENT_ID_ALERT_CLOSE;
			break;
		case GlobalStatic.ALERT_DISCONNECT:
			eventType = EVENT_ID_ALERT_CLOSE;
			break;
		}

		String message = getAlertMessage(type);
//		showAlertDialog(null, message, RVDialog.STYLE_NOTICE,
//										R.string.common_ok, eventType,
//										R.string.rv_cancel, EVENT_ID_ALERT_CANCEL);
    }

//	public void removeLoadingDlg() {
//		hideProgressHandler();
//	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	private Runnable showErrDialog = new Runnable() {
		public void run() {
			showErrDialog(GlobalStatic.errMessageProc(ScreenCanvasMobileActivity.this));
		}
	};

	private OnTouchListener toolsButtonTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setPressed(true);
				toolPressY = (int) event.getRawY();
				toolTransY = (int) toolsView.getTranslationY();
				isToolMoved = false;
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if(!isToolMoved) {
					setToolsEvent(v.getId());
					v.setPressed(false);
				}
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				int diff = (int) event.getRawY() - toolPressY;

				if(Math.abs(diff) > 20) {
					//move
					toolsView.setTranslationY(diff + toolTransY);
					if(!isToolMoved) {
						isToolMoved = true;
						v.setPressed(false);
					}
				}
				return true;
			}
			return false;
		}
	};

	private Handler dealdyHideControlViewHandler = new Handler() {
		public void handleMessage(Message msg) {
			controlButtonEvent(true);
		}
	};

	private OnTouchListener controlTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				setControlEvent(v.getId());
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				return true;
			}
			return false;
		}
	};

	public void changeScreenOrientation() {
		switch (GlobalStatic.x264VideoHeader.getRotation()) {
			case 90:
			case 270:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			default: // 0, 180
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
		}
	}
}
