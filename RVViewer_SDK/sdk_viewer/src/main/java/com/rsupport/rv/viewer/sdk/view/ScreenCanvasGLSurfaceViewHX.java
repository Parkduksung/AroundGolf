package com.rsupport.rv.viewer.sdk.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;

import com.rsupport.rscommon.exception.RSException;
import com.rsupport.rv.viewer.sdk.common.log.RLog;
import com.rsupport.rv.viewer.sdk.data.decoder.H264Decoder;
import com.rsupport.rv.viewer.sdk.decorder.viewer.channelConstants;
import com.rsupport.rv.viewer.sdk.setting.Global;
import com.rsupport.rv.viewer.sdk.ui.IScreenController;
import com.rsupport.rv.viewer.sdk.ui.ScreenCanvasMobileActivity;
import com.rsupport.rv.viewer.sdk.ui.SendAction;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by taehwan on 6/10/15.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ScreenCanvasGLSurfaceViewHX extends GLSurfaceView implements IScreenController, OnTextureListener, GestureDetector.OnGestureListener {

    /**
     * GL ES 2.0 사용을 위한 코드
     */
    private final int GL_ES_20 = 2;

    /**
     * 화면 비율을 1:1로 정의할 경우 {@link TextureRender#TEXTURE_TYPE_CONTROL}
     * 화면 비율을 비디오의 원본 사이즈로 정의 할 경우 {@link TextureRender#TEXTURE_TYPE_VIDEO}
     */
    private final int SURFACE_VIEW_TYPE = TextureRender.TEXTURE_TYPE_CONTROL;

    private TextureRender textureRender;

    private H264Decoder decoder;

    private ScreenCanvasMobileActivity m_parentActivity;
    private SendAction m_sndAct;

    private int packetSize = 0;

    private OnRendererListener onRendererListener;
    private final AtomicBoolean isRendererStarted = new AtomicBoolean(false);

    @Override
    public boolean isDrawingRun() {
        return false;
    }

    @Override
    public void setDrawingRun(boolean b) {

    }

    /////////////////////////////////

    public ScreenCanvasGLSurfaceViewHX(Context context) {
        super(context);
        initialize(context);
    }

    public ScreenCanvasGLSurfaceViewHX(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(GL_ES_20);

        packetSize = 0;
        m_parentActivity = (ScreenCanvasMobileActivity) context;

        if (Global.GetInstance().getSendAction() == null) {
            Global.GetInstance().setSendAction(m_sndAct = new SendAction());

        } else {
            m_sndAct = Global.GetInstance().getSendAction();
        }

        textureRender = new TextureRender(SURFACE_VIEW_TYPE);
        textureRender.setOnTextureListener(this);
        setRenderer(textureRender);

        try {
            decoder = H264Decoder.getDecoder();
        } catch (RSException e) {
            RLog.w(e);
        }
    }

    public void resetScreen(int width, int height, int bitCount) {
        if (width <= 0 || height <= 0) return;

        if (textureRender != null) {
            textureRender.setFrameBufferSize(width, height);
        }

        m_sndAct.SetDataChannel(Global.GetInstance().getDataChannel());
    }

    public void setInitScreen(int width, int height, int bitCount) {
        if (width <= 0 || height <= 0) return;

        try {
            decoder.setSize(getWidth(), getHeight());

            textureRender.isConnect();
            textureRender.setFrameBufferSize(width, height);

            m_sndAct.SetDataChannel(Global.GetInstance().getDataChannel());
//            m_parentActivity.removeLoadingDlg();

            Global.GetInstance().getDataChannel().sendPacket(channelConstants.rcpX264Stream, channelConstants.rcpX264StreamReload);
            m_parentActivity.changeScreenOrientation();
        } catch (RSException e) {
            RLog.w(e);
        }
    }

    public boolean onCustomTouchEventEx(MotionEvent event, boolean isScreenMoveable, boolean isCursorMode, boolean isCursorEdge, int cursorX, int cursorY, int correctionX) {
        if (event.getPointerCount() == 1) {
            m_sndAct.sendMonkeyTouchEvent(event, (short) (event.getX() - correctionX), (short) event.getY(), (short) (event.getX() - correctionX), (short) event.getY());
        } else if (event.getPointerCount() == 2) {
            RLog.d("onCustomTouchEventEx");
            m_sndAct.sendMonkeyTouchEvent(event, (short) (event.getX(0) - correctionX), (short) event.getY(0), (short) (event.getX(1) - correctionX), (short) event.getY(1));
        }

        /*
        * 화면 컨트롤 Zoom In, Zoom Out
        if (textureRender != null) {
            return textureRender.onCustomTouchEvent(event);
        }
        * */
        return false;
    }

    @Override
    public void release() {

    }


    @Override
    public void render(@NotNull byte[] data, int size) {
        packetSize += size;
        decoder.inputBufferData(data, size);

        if(!isRendererStarted.getAndSet(true)){
            if(onRendererListener != null){
                onRendererListener.onStart();
            }
        }
    }

    public void sendMonkeyKeycode(int keycode) {
        m_sndAct.sendMonkeyKeypadEvent(keycode);
    }

    public void stopDecoder() {
        if (decoder != null) {
            decoder.release();
            decoder = null;
        }

        if (textureRender != null) {
            textureRender.release();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onCreateTexture(Surface surface) {
        try {
            decoder.setSurface(surface);
        } catch (RSException e) {
            RLog.w(e);
        }
    }

    @Override
    public boolean onDraw() {
        return decoder.renderOutput();
    }

    @Override
    public void saveScreenCtrl(byte[] data, int length) {

    }

    @Override
    public void saveHXSoundInfo(byte[] hxSoundInfo) {

    }

    @Override
    public void zoomReset() {

    }

    @Override
    public void startDrag(int x, int y) {

    }

    @Override
    public void dragMove(int x, int y) {

    }

    @Override
    public void endDrag(int x, int y) {

    }

    @Override
    public void moveScreen(int dx, int dy) {

    }

    @Override
    public boolean isScrollable(@NotNull Direction direction) {
        return false;
    }

    @Override
    public void setSourceSize(int width, int height) {
        setInitScreen(width, height, 0);
    }

    @Override
    public void setRemoteSize(int width, int height) {

    }

    @Override
    public void setClickPos(int viewX, int viewY) {

    }

    @Override
    public int getRealPosX(int viewX) {
        return 0;
    }

    @Override
    public int getRealPosY(int viewY) {
        return 0;
    }

    @Override
    public boolean isValidPosition(int x, int y) {
        return true;
    }

    @Override
    public void fitToScreen() {

    }

    @Override
    public int getViewWidth() {
        return 0;
    }

    @Override
    public int getViewHeight() {
        return 0;
    }

    @Override
    public void zoomBegin() {
    }

    @Override
    public void zoom(float scaleFactor) {
    }

    @Override
    public void setRenderKeyFrame() {
        decoder.setDropFrameBeforeIntoKeyFrame();
    }

    @Override
    public void videoOptionChanged() {
    }

    @Override
    public void setOnRendererListener(@NotNull OnRendererListener onRendererListener) {
        this.onRendererListener = onRendererListener;
    }

    @Override
    public void setVisibleHeight(int height) {

    }
}
