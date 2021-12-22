package com.rsupport.rv.viewer.sdk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import com.rsupport.rv.viewer.sdk.common.RuntimeData;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.decorder.Converter;
import com.rsupport.rv.viewer.sdk.decorder.scapDec.Decoder;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.CRCVDataChannel;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpKeyEventMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpMonkeyEventMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpMonkeyKeypadMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpMouseEventMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpPointerMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channel.rcpX264VideoHeaderMsg;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.setting.GlobalStatic;


public class SendAction {

	final static int rfbButton1Mask = 0x01; // LEFT
	final static int rfbButton2Mask = 0x02; // TOP
	final static int rfbButton3Mask = 0x04; // MIDDLE
	final static int rfbButton4Mask = 0x08; // WhellUp
	final static int rfbButton5Mask = 0x10; // WhellDown
	final static int MK_MOUSE_MOVE  = 0x0020; //Mouse move
	
	final static int rfbWheelUpMask = rfbButton4Mask;
	final static int rfbWheelDownMask = rfbButton5Mask;
	final static int rfbWheelMask = (rfbWheelUpMask | rfbWheelDownMask);
	
	int buttonMask = 0;
	
	byte[] eventBuf = new byte[72];
	byte[] sendBuf = new byte[1024]; 
	
	public Decoder decoder;
	public CRCVDataChannel m_dataChannel;

	private rcpMouseEventMsg pe = new rcpMouseEventMsg();
	private rcpKeyEventMsg ke = new rcpKeyEventMsg();
	private rcpMonkeyEventMsg me = new rcpMonkeyEventMsg();
	private rcpMonkeyKeypadMsg mke = new rcpMonkeyKeypadMsg();

	private rcpPointerMsg pm = new rcpPointerMsg();
	
	boolean brokenKeyPressed = false;
	public int m_nKeyMouseControlState =  channelConstants.KeyMouseControl_None;
	
	int oldModifiers = 0;

	final static int MONKEY_TOUCH_ACTION_DOWN = 0;
	final static int MONKEY_TOUCH_ACTION_UP = 1;
	final static int MONKEY_TOUCH_ACTION_MOVE = 2;

	public short[] ArrayFreeLineClient = new short[256];
	float[] beforePoint = null;
	private int num = 0;
	private String PICDATAKEY = "picdata";
	private String PICDATASIZE = "picdatasize";

	public SendAction() {		
	}
	
	public boolean SendMouseWheelEvent(boolean up) {
		if (m_dataChannel == null) return false;
		boolean ret = true;
		int wheelRotation = 0;

		RLog.d("Send mousewheel up = " + up + ", x = " + lastTappedX + ", y=" + lastTappedY);

		if (!up) {
			wheelRotation = 1;
			for (; wheelRotation > 0; wheelRotation--) {
				pe.buttonMask = rfbWheelDownMask;
				pe.x = (short) lastTappedX;
				pe.y = (short) lastTappedY;
				ret = m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
				
				pe.buttonMask = 0;
				ret = m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
			}
		} else {
			wheelRotation = -1;
			for (; wheelRotation < 0; wheelRotation++) {
				pe.buttonMask = rfbWheelUpMask;
				pe.x = (short) lastTappedX;
				pe.y = (short) lastTappedY;
				ret = m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent,	pe, channelConstants.sz_rcpMouseEventMsg);
				
				pe.buttonMask = 0;
				ret = m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
			}
		}
		return ret;
	}

	private int lastTappedX = 0;
	private int lastTappedY = 0;

	public boolean SendMouseClickEvent(int x, int y, boolean right) {
		if((m_dataChannel != null) && (m_dataChannel.isConnected())) {
			if(right) {
				buttonMask = rfbButton3Mask;
			}else {
				buttonMask = rfbButton1Mask;
			}

			if(!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
				if(x < 0) x = 0;
				if(y < 0) y = 0;
			}

			pe.buttonMask = buttonMask;
			pe.x = (short) x;
			pe.y = (short) y;

			lastTappedX = x;
			lastTappedY = y;

			//press
			m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);

			//release
			pe.buttonMask = 0;
			m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);

			return true;
		}

		return false;
	}

	public boolean SendMousePressedEvent(int newX, int newY, boolean right) {
		if (m_dataChannel == null) return false;

		if (right) 	buttonMask = rfbButton3Mask;
		else 		buttonMask = rfbButton1Mask;

		int x = newX;
		int y = newY;

		if (!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
			if (x < 0) x = 0;
			if (y < 0) y = 0;
		}

		if(!m_dataChannel.isConnected()) return false;

		if (buttonMask == 0) {}

		lastTappedX = newX;
		lastTappedY = newY;

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;
		return m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}



	public boolean SendMousePressedEvent(MotionEvent evt, int newX, int newY, boolean right) {
		if (m_dataChannel == null) return false;
		
		if (right) 	buttonMask = rfbButton3Mask;
		else 		buttonMask = rfbButton1Mask;

		int eventBufLen = 0;
		int x = newX;
		int y = newY;

		if (!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
			if (x < 0) x = 0;
			if (y < 0) y = 0;
		}
		
		if(!m_dataChannel.isConnected()) return false;
			
//		if((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Mouse) != channelConstants.KeyMouseControl_Mouse || 
//		   (m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
//		{
//			return; 
//		}
			
		if (buttonMask == 0) {}

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;
//		RLog.d("SendMousePressedEvent x : " + pe.x);
//		RLog.d("SendMousePressedEvent y : " + pe.y);
//		RLog.d("SendMousePressedEvent buttonMask : " + pe.buttonMask);
		return m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}
	
	public boolean SendMouseMoveEvent(MotionEvent evt, int newX, int newY) {
		if (m_dataChannel == null) return false;

		buttonMask = 0;//MK_MOUSE_MOVE;

		int eventBufLen = 0;
		int x = newX;
		int y = newY;

        if (!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
		
		if(!m_dataChannel.isConnected()) return false;
				
		if (buttonMask == 0) {}

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;

		lastTappedX = x;
		lastTappedY = y;

		return m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}

	public boolean SendMouseReleasedEvent(int newX, int newY) {
		if (m_dataChannel == null) return false;

		buttonMask = 0;

		int x = newX;
		int y = newY;

		if (!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
			if (x < 0) x = 0;
			if (y < 0) y = 0;
		}

		if(!m_dataChannel.isConnected()) return false;

		if (buttonMask == 0) {}

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;
		return m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}



	public boolean SendMouseReleasedEvent(MotionEvent evt, int newX, int newY) {
		if (m_dataChannel == null) return false;

		buttonMask = 0;

		int eventBufLen = 0;
		int x = newX;
		int y = newY;

        if (!RuntimeData.getInstance().getAgentConnectOption().isHxEngine()) {
            if (x < 0) x = 0;
            if (y < 0) y = 0;
        }
		
		if(!m_dataChannel.isConnected()) return false;
			
//		if((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Mouse) != channelConstants.KeyMouseControl_Mouse || 
//		   (m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
//		{
//			return; 
//		}
			
		if (buttonMask == 0) {}

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;
//		RLog.d("SendMouseReleasedEvent x : " + pe.x);
//		RLog.d("SendMouseReleasedEvent y : " + pe.y);
//		RLog.d("SendMouseReleasedEvent buttonMask : " + pe.buttonMask);
		return m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}

	public void SendMouseEvent(MotionEvent evt, int newX, int newY) {
//		int modifiers = evt.getModifiers();
//		
//		if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
//			if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
//				buttonMask = rfbButton3Mask;
//				modifiers &= ~ALT_MASK;
//			} else if ((modifiers & InputEvent.BUTTON2_MASK) != 0) {
//				buttonMask = rfbButton2Mask;
//				modifiers &= ~META_MASK;
//			} else {
//				buttonMask = rfbButton1Mask;
//			}
//		} else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
//			buttonMask = 0;
//			if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
//				modifiers &= ~ALT_MASK;
//			} else if ((modifiers & InputEvent.BUTTON2_MASK) != 0) {
//				modifiers &= ~META_MASK;
//			}
//		}
		
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			buttonMask = rfbButton1Mask;
		} else if (evt.getAction() == MotionEvent.ACTION_UP) {
			buttonMask = 0;
		}
		
		int eventBufLen = 0;
		int x = newX;
		int y = newY;

		if (x < 0) x = 0;
		if (y < 0) y = 0;
		
		if(!m_dataChannel.isConnected()) return;
			
//		if((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Mouse) != channelConstants.KeyMouseControl_Mouse || 
//		   (m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
//		{
//			return; 
//		}
			
		if (buttonMask == 0) {}

//		if (evt instanceof MouseWheelEvent) {
//			int wheelRotation = ((MouseWheelEvent) evt).getWheelRotation();
//			for (; wheelRotation > 0; wheelRotation--) {
//				int mask = buttonMask | rfbWheelDownMask;
//				pe.buttonMask = mask;
//				pe.x = (short) x;
//				pe.y = (short) y;
//				m_dataChannel.SendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
//				
//				mask = buttonMask & ~rfbWheelDownMask;
//				pe.buttonMask = mask;
//				m_dataChannel.SendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
//			}
//			for (; wheelRotation < 0; wheelRotation++) {
//				int mask = buttonMask | rfbWheelUpMask;
//				pe.buttonMask = mask;
//				pe.x = (short) x;
//				pe.y = (short) y;
//				m_dataChannel.SendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent,	pe, channelConstants.sz_rcpMouseEventMsg);
//				
//				mask = buttonMask & ~ rfbWheelUpMask;
//				pe.buttonMask = mask;
//				m_dataChannel.SendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
//			}
//		} 

		pe.buttonMask = buttonMask;
		pe.x = (short) x;
		pe.y = (short) y;
		m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMouseEvent, pe, channelConstants.sz_rcpMouseEventMsg);
	}

	int oldCount = 0;
	public void sendMonkeyTouchEvent(MotionEvent evt, short x1, short y1, short x2, short y2) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			buttonMask = MONKEY_TOUCH_ACTION_DOWN;
		} else if (evt.getAction() == MotionEvent.ACTION_UP) {
			buttonMask = MONKEY_TOUCH_ACTION_UP;
		} else if (evt.getAction() == MotionEvent.ACTION_MOVE) {
			buttonMask = MONKEY_TOUCH_ACTION_MOVE;
		}

		if (oldCount < 2 && evt.getPointerCount() > 1) {
			buttonMask = MONKEY_TOUCH_ACTION_DOWN;
		} else if (evt.getPointerCount() < 2 && oldCount > 1) {
			buttonMask = MONKEY_TOUCH_ACTION_UP;
		}
		
		if(!m_dataChannel.isConnected()) return;
		
		me.action = buttonMask;
		me.count = 1;
		me.x1 = getRatioPositionX(x1);
		me.y1 = getRatioPositionY(y1);
		
		if (evt.getPointerCount() == 1) {
			me.x2 = (short)0x8000;
			me.y2 = (short)0x8000;
		} else {
			me.x2 = getRatioPositionX(x2);
			me.y2 = getRatioPositionY(y2);
		}
		
		RLog.d("sendMonkeyTouchEvent me.x1 : " + me.x1);
		RLog.d("sendMonkeyTouchEvent me.y1 : " + me.y1);
		
		RLog.d("sendMonkeyTouchEvent me.x2 : " + me.x2);
		RLog.d("sendMonkeyTouchEvent me.y2 : " + me.y2);

		if (GlobalStatic.isPointerVisible) {
			pm.x = me.x1;
			pm.y = me.y1;
			if (evt.getAction() == MotionEvent.ACTION_UP) {
				m_dataChannel.sendPacket2(channelConstants.rcpLaserPointer, channelConstants.rcpLaserPointerEnd, pm, pm.size());
			} else {
				m_dataChannel.sendPacket2(channelConstants.rcpLaserPointer, channelConstants.rcpLaserPointerPos, pm, pm.size());
			}
		} else {
			m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMonkeyTouch, me, me.size());
		}
		
		oldCount = evt.getPointerCount();
	}
	
	private short getRatioPositionX(short x) {
		RLog.d("sendMonkeyTouchEvent getWidthRatio : " + getWidthRatio());
		return (short)(getWidthRatio() * x);
	}
	
	private short getRatioPositionY(short y) {
		RLog.d("sendMonkeyTouchEvent getHeightRatio : " + getHeightRatio());
		return (short)(getHeightRatio() * y);
	}

	private float getWidthRatio() {
		int frameWidth = 0;
		float width = 0.0f;
		if (GlobalStatic.x264VideoHeader.getSourceType() == rcpX264VideoHeaderMsg.ENCODER_TYPE_KNOX) {
			if (GlobalStatic.x264VideoHeader.getIsLandscape() == 0) {
				// 세로가 디폴트인 단말기
				if (GlobalStatic.screenAngle == 0 || GlobalStatic.screenAngle == 180) {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameWidth();
				} else {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameHeight();
				}
			} else {
				// 가로가 디폴트인 단말기
				if (GlobalStatic.screenAngle == 0 || GlobalStatic.screenAngle == 180) {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameHeight();
				} else {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameWidth();
				}
			}

		} else if (GlobalStatic.x264VideoHeader.getSourceType() == rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX) {
			frameWidth = GlobalStatic.x264VideoHeader.getFrameWidth();
		} else {
			if (GlobalStatic.x264VideoHeader.getWidth() > GlobalStatic.x264VideoHeader.getHeight()) {
				if (GlobalStatic.x264VideoHeader.getFrameWidth() > GlobalStatic.x264VideoHeader.getFrameHeight()) {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameWidth();
				} else {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameHeight();
				}
			} else {
				if (GlobalStatic.x264VideoHeader.getFrameWidth() < GlobalStatic.x264VideoHeader.getFrameHeight()) {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameWidth();
				} else {
					frameWidth = GlobalStatic.x264VideoHeader.getFrameHeight();
				}
			}
		}

		width = (float) Global.GetInstance().getMobileViewer().getWindowRect().right;
		float widthScale = (float)frameWidth / width;
		return widthScale;
	}

	private float getHeightRatio() {
		int frameHeight = 0;
		float height = 0.0f;
		if (GlobalStatic.x264VideoHeader.getSourceType() == rcpX264VideoHeaderMsg.ENCODER_TYPE_KNOX) {
			if (GlobalStatic.x264VideoHeader.getIsLandscape() == 0) {
				// 세로가 디폴트인 단말기
				if (GlobalStatic.screenAngle == 0 || GlobalStatic.screenAngle == 180) {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameHeight();
				} else {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameWidth();
				}
			} else {
				// 가로가 디폴트인 단말기
				if (GlobalStatic.screenAngle == 0 || GlobalStatic.screenAngle == 180) {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameWidth();
				} else {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameHeight();
				}
			}
		} else if (GlobalStatic.x264VideoHeader.getSourceType() == rcpX264VideoHeaderMsg.ENCODER_TYPE_OMX) {
			frameHeight = GlobalStatic.x264VideoHeader.getFrameHeight();
		} else {
			if (GlobalStatic.x264VideoHeader.getWidth() > GlobalStatic.x264VideoHeader.getHeight()) {
				if (GlobalStatic.x264VideoHeader.getFrameWidth() > GlobalStatic.x264VideoHeader.getFrameHeight()) {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameHeight();
				} else {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameWidth();
				}
			} else {
				if (GlobalStatic.x264VideoHeader.getFrameWidth() < GlobalStatic.x264VideoHeader.getFrameHeight()) {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameHeight();
				} else {
					frameHeight = GlobalStatic.x264VideoHeader.getFrameWidth();
				}
			}
		}
		height = (float) Global.GetInstance().getMobileViewer().getWindowRect().bottom;
		float heightScale = (float)frameHeight / height;
		return heightScale;
	}
	
	public void sendMonkeyKeypadEvent(int keycode) {

		if(!m_dataChannel.isConnected()) return;
		
		mke.action = (short) MONKEY_TOUCH_ACTION_DOWN;
		mke.count = 1;
		mke.keycode = (short) keycode;
		
		m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMonkeyKeypad, mke, mke.size());
		
		mke.action = (short) MONKEY_TOUCH_ACTION_UP;
		
		m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpMonkeyKeypad, mke, mke.size());
	}

	public void clickLanguageKey() {
		if (GlobalStatic.g_hostOsType == GlobalStatic.HOST_WINDOWS) {
			writeWindowSystemKey(KEY_HANGULE, true, 0);
			writeWindowSystemKey(KEY_HANGULE, false, 0);
		} else {
			boolean click = true;
			__writeKeyEvent(0xffe7, click, 0);
			__writeKeyEvent(0x20, click, 0);
			__writeKeyEvent(0x20, !click, 0);
			__writeKeyEvent(0xffe7, !click, 0);
		}
	}

	public boolean isMouseKeyboardControl() {
		boolean ret = true;
		if ((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Keyboard) != channelConstants.KeyMouseControl_Keyboard || 
			(m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
		{
			ret = false;
		}
		return ret;
	}
	
	final int KEY_HANGULE	= 0xFFEB;
	final int KEY_LWIN		= 0xFFEC;
	final int KEY_RWIN		= 0xFFED;
	final int KEY_HANJA		= 0xFFF0;
	final int KEY_ALT_L		= 0xFFE9;
	final int KEY_TAB		= 0xFF09;
	final int KEY_F4		= 0xFF94;
	private boolean m_keyflag_win	= false;
	private boolean m_keyflag_alt 	= false;
	public void writeWindowSystemKey(int key, boolean down, int specialkey) {
		if ((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Keyboard) != channelConstants.KeyMouseControl_Keyboard || 
			(m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
		{
			return;
		}
		
		if (key == KEY_LWIN && down == m_keyflag_win) return;
		if (key == KEY_ALT_L && down == m_keyflag_alt) return;
		
		if (key == KEY_LWIN && down == true) {
			m_keyflag_win = true;
		} else if (key == KEY_LWIN && down == false) {
			m_keyflag_win = false;
		}
		
		if (key == KEY_ALT_L && down == true) {
			m_keyflag_alt = true;
		} else if (key == KEY_ALT_L && down == false) {
			m_keyflag_alt = false;
		}
		
		ke.down = down ? (byte) 1 : (byte) 0;
		ke.key = key;
		ke.specialkeystate = (short) specialkey;

		m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl,
			channelConstants.rcpKeyEvent, ke, ke.size());
	}
	
	public void SendKeyEvent(int key, boolean down) {
		__writeKeyEvent(key, down, 0);
	}
	
	public void SendKeyEvent(int key, boolean down, int specialkey) {
		__writeKeyEvent(key, down, specialkey);
	}
	

	private void __writeKeyEvent(int keysym, boolean down, int specialkey) {
		if (m_dataChannel == null) 	return;

//		if (keysym >= 96 && keysym <= 105) {
//			keysym -= 48;
//		} else if (keysym == 109 || keysym == 111 || keysym == 110) {
//			keysym -= 64;
//		} else if (keysym == 106 || keysym == 107) {
//			keysym -= 64;
//			specialkey |= channelConstants.rcpKeyShift;
//		} else if (keysym == 192) {
//			keysym = 96;
//		}

		ke.down = down ? (byte) 1 : (byte) 0;
		ke.key = keysym;
		ke.specialkeystate = (short) specialkey;

		RLog.d("__writeKeyEvent key : " + keysym);
		RLog.d("__writeKeyEvent key down : " + down);
		RLog.d("__writeKeyEvent key specialkeystate : " + ke.specialkeystate);

//		RLog.d("__writeKeyEvent key : " + keysym);
//		RLog.d("__writeKeyEvent key down : " + down);
		m_dataChannel.sendPacket2(channelConstants.rcpKeyMouseCtrl,
			channelConstants.rcpKeyEvent, ke, ke.size());
	}

	private void __writeModifierKeyEvents(int newModifiers) {
	}
	  
	private void __TraceKeyEvents(int newModifiers, int key) {
//		if ((newModifiers & CTRL_MASK) != (oldModifiers & CTRL_MASK))
//		if ((newModifiers & SHIFT_MASK) != (oldModifiers & SHIFT_MASK))
//		if ((newModifiers & META_MASK) != (oldModifiers & META_MASK))
//		if ((newModifiers & ALT_MASK) != (oldModifiers & ALT_MASK))
	}
	
	public void SendPacket(int payloadtype, int msgid, byte[] data, int dataSize) {
		if(decoder == null)
			return;
		
		int nPacketSize = 0;
		if(dataSize > 0) {
			nPacketSize = channelConstants.sz_rcpPacket + channelConstants.sz_rcpDataMessage + dataSize;
		}
		else {
			nPacketSize = channelConstants.sz_rcpPacket + channelConstants.sz_rcpMessage;
		}
		int msgsize = nPacketSize - channelConstants.sz_rcpPacket;
		
		int nIndex = 0;
		sendBuf[nIndex++] = (byte) payloadtype;
		System.arraycopy(Converter.getBytesFromIntLE(msgsize), 0, sendBuf, nIndex, 4);
		nIndex += 4;	
		
		if(dataSize > 0) {
			sendBuf[nIndex++] = (byte) msgid;
			System.arraycopy(Converter.getBytesFromIntLE(dataSize), 0, sendBuf, nIndex, 4);
			nIndex += 4;
			System.arraycopy(data, 0, sendBuf, nIndex, dataSize);
			nIndex += dataSize;
		}
		else {
			sendBuf[nIndex++] = (byte) msgid;
		}
		decoder.WriteExact(sendBuf, 0, nIndex);
		
	}
	
	public boolean enableKeyMouseControl() {
		if(m_dataChannel == null || !m_dataChannel.isConnected()) return false;
		
		boolean ret = true;
		if(!m_dataChannel.sendPacket(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpKeyMouseCtrlRequest)) {
			return false;
		}
		return ret;
	}

	public boolean EnableKeyMouseControl(boolean bEnable) {
		if(m_dataChannel == null || !m_dataChannel.isConnected()) return false;
		
		if(bEnable) {
			if(m_nKeyMouseControlState ==  channelConstants.KeyMouseControl_None) {
				if(!m_dataChannel.sendPacket(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpKeyMouseCtrlRequest)) {
					return false;
				}
			}

			if((m_nKeyMouseControlState & channelConstants.KeyMouseControl_Suspend) == channelConstants.KeyMouseControl_Suspend)
			{
				if(!m_dataChannel.sendPacket(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpKeyMouseCtrlResume))
					return false;
				
				m_nKeyMouseControlState &= ~channelConstants.KeyMouseControl_Suspend;
				m_nKeyMouseControlState |= channelConstants.KeyMouseControl_Mouse;
				m_nKeyMouseControlState |= channelConstants.KeyMouseControl_Keyboard;
			}
		}
		else if(m_nKeyMouseControlState != channelConstants.KeyMouseControl_None)
		{
			if(!m_dataChannel.sendPacket(channelConstants.rcpKeyMouseCtrl, channelConstants.rcpKeyMouseCtrlSuspend))
				return false;
				
			m_nKeyMouseControlState |= channelConstants.KeyMouseControl_Suspend;
			m_nKeyMouseControlState &= ~channelConstants.KeyMouseControl_Mouse;
			m_nKeyMouseControlState &= ~channelConstants.KeyMouseControl_Keyboard;
		}
		
		return true;
	}
	
	public void sendDrawingEvent(MotionEvent event) {
		float[] eventPoint = new float[4];

		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN :
				eventPoint[0] = eventPoint[2] = event.getX();
				eventPoint[1] = eventPoint[3] = event.getY();

				beforePoint = eventPoint;
				sendDrawingDataHandler.sendEmptyMessage(channelConstants.rcpDrawInfo);

				num = 0;
				ArrayFreeLineClient[num++] = getRatioPositionX((short) event.getX());
				ArrayFreeLineClient[num++] = getRatioPositionY((short) event.getY());
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];
				break;
			case MotionEvent.ACTION_MOVE :
				eventPoint[0] = beforePoint[2];
				eventPoint[1] = beforePoint[3];
				eventPoint[2] = event.getX();
				eventPoint[3] = event.getY();

				beforePoint = eventPoint;

				ArrayFreeLineClient[num++] = getRatioPositionX((short) event.getX());
				ArrayFreeLineClient[num++] = getRatioPositionY((short) event.getY());
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];

				// 그리기 데이터가 너무 길어지면
				if(num >= 256) {
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putByteArray(PICDATAKEY, Converter.getBytesFromShorts(ArrayFreeLineClient));
					data.putInt(PICDATASIZE, num);

					ArrayFreeLineClient[0] = ArrayFreeLineClient[2] = ArrayFreeLineClient[num-2];
					ArrayFreeLineClient[1] = ArrayFreeLineClient[3] = ArrayFreeLineClient[num-1];
					num = 4;

					msg.setData(data);
					msg.what = channelConstants.rcpDrawData;
					sendDrawingDataHandler.sendMessage(msg);
				}
				break;
			case MotionEvent.ACTION_UP :
				beforePoint = null;

				ArrayFreeLineClient[num++] = getRatioPositionX((short) event.getX());
				ArrayFreeLineClient[num++] = getRatioPositionY((short) event.getY());
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];
				ArrayFreeLineClient[num++] = ArrayFreeLineClient[num-3];

				Message msg = new Message();
				Bundle data = new Bundle();
				data.putByteArray(PICDATAKEY, Converter.getBytesFromShorts(ArrayFreeLineClient));
				data.putInt(PICDATASIZE, num);
				num=0;

				msg.setData(data);
				msg.what = channelConstants.rcpDrawData;
				sendDrawingDataHandler.sendMessage(msg);
				break;
			default :
				Log.e("SendAction", "Ignore to onTouchEvent : " + event.getAction());
				break;
		}

//		m_drawCanvas.drawLines(getOrientationPoints(eventPoint), paint_client);
//		invalidate();
	}

	/** 단말에서 그리기 할 떄 정보. 현재 freeline, blue 고정 **/
	public byte[] getDrawInfo(int drawType, Color colors) {
		byte[] drawinfo = new byte[7];

		// drawType		1byte
		drawinfo[0] = (byte) drawType; //RcpCmd.rcpDrawFreeLine;

		// RGB color	4byte
		drawinfo[1] = (byte) 255;
		drawinfo[2] = 0;
		drawinfo[3] = 0;
		drawinfo[4] = 0;

		// thickness	1byte
		drawinfo[5] = (byte) 2;

		// realtime		1byte
		drawinfo[6] = 0;

		return drawinfo;
	}

	// 그리기 데이터 정보를 뷰어로 전송
	public Handler sendDrawingDataHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			if(Global.GetInstance().getDataChannel() == null) return;
			switch(msg.what) {
				case channelConstants.rcpDrawInfo :
					int msgId = channelConstants.rcpDrawFreeLine;

					if (GlobalStatic.isDrawEraser)
						msgId = channelConstants.rcpDrawEraser;

					Global.GetInstance().getDataChannel().sendDrawingData(channelConstants.rcpDrawInfo, getDrawInfo(msgId, null), 7);
					break;
				case channelConstants.rcpDrawData :
					int length = msg.getData().getInt(PICDATASIZE) * 2; // short[] -> byte[] 변환으로 사이즈 X2
					Global.GetInstance().getDataChannel().sendDrawingData(channelConstants.rcpDrawData, msg.getData().getByteArray(PICDATAKEY), length);
					break;
			}
		}
	};
	
	public void SetDecoder(Decoder decoder) {
		this.decoder = decoder;
	}
	
	public void SetDataChannel(CRCVDataChannel channel) {
		m_dataChannel =  channel;	
	}
}
