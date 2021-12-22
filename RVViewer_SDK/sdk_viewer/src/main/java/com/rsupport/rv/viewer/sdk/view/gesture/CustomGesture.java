package com.rsupport.rv.viewer.sdk.view.gesture;

import android.view.MotionEvent;

import com.rsupport.rv.viewer.sdk.view.gl.GLTextureRender;


/**
 * Created by taehwan on 6/10/15.
 */
public class CustomGesture {

    public static final float DEFAULT_SCROLL_VALUE = 0f;
    public static final float DEFAULT_SCALE_VALUE = 1f;

    private final int PER_ZOOM = 7;

    private final int SINGLE_TOUCH = 1;
    private final int TWO_TOUCH = 2;

    private OnGestureListener onGestureListener;

    private float scaleX = DEFAULT_SCALE_VALUE;
    private float scaleY = DEFAULT_SCALE_VALUE;
    /**
     * 최소 Scale의 값을 초기 셋팅 값으로 잡기 위한 변수.
     */
    private float backupScaleX = scaleX;
    private float backupScaleY = scaleY;

    private float zoomScaleX = scaleX;
    private float zoomScaleY = scaleY;

    private float scrollX = DEFAULT_SCROLL_VALUE;
    private float scrollY = DEFAULT_SCROLL_VALUE;

    private float zoomScrollX = scrollX;
    private float zoomScrollY = scrollY;

    private float mPrevX = 0.0f;
    private float mPrevY = 0.0f;
    private float mPrevX2 = 0.0f;
    private float mPrevY2 = 0.0f;
    private float mPrevMidX = 0.0f;
    private float mPrevMidY = 0.0f;

    private boolean mDrag = false;

    private int frameBufferWidth;
    private int frameBufferHeight;

    private int surfaceWidth;
    private int surfaceHeight;

    /**
     * Scale 초기 값 지정.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        backupScaleX = scaleX;
        backupScaleY = scaleY;

        zoomScaleX = scaleX;
        zoomScaleY = scaleY;
//        Log.i("scaleX " + scaleX + ", scaleY " + scaleY);
    }

    /**
     * Scroll 초기 값 지정.
     */
    public void setScroll(float scrollX, float scrollY) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
//        Log.i("scrollX " + scrollX + ", scrollY " + scrollY);
    }

    /**
     * FrameBuffer 의 사이즈를 정의한다.
     */
    public void setFrameBufferSize(int frameBufferWidth, int frameBufferHeight) {
        this.frameBufferWidth = frameBufferWidth;
        this.frameBufferHeight = frameBufferHeight;
    }

    public void setSurfaceSize(int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
    }

    /**
     * Event 처리
     */
    public void setOnGestureListener(GLTextureRender onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    public boolean onCustomToucheEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == SINGLE_TOUCH) {
                    mPrevX = event.getX(0);
                    mPrevY = event.getY(0);
                    mDrag = true;
                }

                if (event.getPointerCount() == TWO_TOUCH) {
                    mPrevX = event.getX(0);
                    mPrevY = event.getY(0);
                    mPrevX2 = event.getX(1);
                    mPrevY2 = event.getY(1);

                    mPrevMidX = (mPrevX + mPrevX2) / 2;
                    mPrevMidY = (mPrevY + mPrevY2) / 2;

//                    Log.w("-------------new-----------");
//                    Log.w("mid position zoomScaleX " + zoomScaleX + " zoomScaleY " + zoomScaleY);
//                    Log.w("mid position zoomScrollX " + zoomScrollX + " zoomScrollY " + zoomScrollY);
//                    Log.w("mid position mPrevMidX " + mPrevMidX + " mPrevMidY " + mPrevMidY);
//                    Log.w("-------------new-----------");
                    mDrag = false;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == SINGLE_TOUCH && mDrag) {
                    actionMove(event);

                } else if (event.getPointerCount() == TWO_TOUCH && !mDrag) {
                    actionZoom(event);
                }

                if (onGestureListener != null) {
                    onGestureListener.onScale(scaleX, scaleY);
                    onGestureListener.onScroll(scrollX, scrollY);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                zoomScaleX = scaleX;
                zoomScaleY = scaleY;

                zoomScrollX = scrollX;
                zoomScrollY = scrollY;

                return true;

        }
        return false;
    }

    /**
     * X 좌표의 최대 이동 가능 float
     */
    private float getPosition(float scale) {
        return (DEFAULT_SCALE_VALUE - (DEFAULT_SCALE_VALUE / scale));
    }

    /**
     * Move 정의
     */
    private boolean actionMove(MotionEvent e) {
        float nNowX = e.getX(0);
        float nNowY = e.getY(0);

        float dx = nNowX - mPrevX;
        float dy = mPrevY - nNowY;

        mPrevX = nNowX;
        mPrevY = nNowY;

        dx = (dx / surfaceWidth) / scaleX;
        dy = (dy / surfaceHeight) / scaleY;

        scrollX = setActionMove(dx, scrollX, scaleX);
        scrollY = setActionMove(dy, scrollY, scaleY);

//        Log.i("scrollX " + scrollX + " scrollY " + scrollY);
        return true;
    }

    private float setActionMove(float gesturePosition, float scroll, float scale) {
        float hide = getPosition(scale);
        if (gesturePosition > 0) {
            hide = -hide;
        }

        scroll += gesturePosition;
        if (Math.abs(hide) < Math.abs(scroll)) {
            if (scale < DEFAULT_SCALE_VALUE) {
                scroll = hide;

            } else {
                scroll = -hide;
            }
        }

        return scroll;
    }

    /**
     * Zoom 정의
     */
    private boolean actionZoom(MotionEvent event) {
        final float perWidth = (float) PER_ZOOM / (float) frameBufferWidth;
        final float perHeight = (float) PER_ZOOM / (float) frameBufferHeight;

        float nNowX = event.getX(0);
        float nNowY = event.getY(0);
        float nNowX2 = event.getX(1);
        float nNowY2 = event.getY(1);

        float dx = (nNowX - nNowX2) * perWidth;
        float dy = (nNowY - nNowY2) * perHeight;
        float dx2 = (mPrevX - mPrevX2) * perWidth;
        float dy2 = (mPrevY - mPrevY2) * perHeight;

        float lenOrig = (float) Math.sqrt(dx * dx + dy * dy);
        float lenMove = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        float scale = lenOrig / lenMove;
        scaleX -= (1f - scale) * backupScaleX;
        if (scaleX < backupScaleX) {
            scaleX = backupScaleX;
        }

        scaleY -= (1f - scale) * backupScaleY;
        if (scaleY < backupScaleY) {
            scaleY = backupScaleY;
        }

        actionZoomMove();

        mPrevX = nNowX;
        mPrevY = nNowY;
        mPrevX2 = nNowX2;
        mPrevY2 = nNowY2;
        return true;
    }

    private float setScaleMove(float gesturePosition, float scale) {
        float hide = getPosition(scale);
        if (gesturePosition > 0) {
            hide = -hide;
        }

        float scroll = gesturePosition;
//        Log.e("-- scale " + scale + " hide " + hide + " scroll " + scroll);
        if (Math.abs(hide) < Math.abs(scroll)) {
            if (scale < DEFAULT_SCALE_VALUE) {
                scroll = hide;

            } else {
                scroll = -hide;
            }
        }

        return scroll;
    }

    /**
     * Action move 처리
     */
    private void actionZoomMove() {
//        Log.d("---- mPrevMidX " + mPrevMidX + " surfaceWidth " + surfaceWidth + " frameBufferWidth " + frameBufferWidth);
//        Log.d("---- mPrevMidY " + mPrevMidY + " surfaceHeight " + surfaceHeight + " frameBufferHeight " + frameBufferHeight);

        // 화면상 좌표를 구한다.
        float viewPositionX = mPrevMidX / surfaceWidth;
        float viewPositionY = mPrevMidY / surfaceHeight;
//        Log.e("viewPositionX " + viewPositionX + " viewPositionY " + viewPositionY);

        float framePositionX = (viewPositionX * frameBufferWidth);
        float framePositionY = (viewPositionY * frameBufferHeight);
//        Log.i("---- framePositionX " + framePositionX + " framePositionY " + framePositionY);

        float dx = (DEFAULT_SCALE_VALUE - (framePositionX / frameBufferWidth) * 2);
        float dy = -(DEFAULT_SCALE_VALUE - (framePositionY / frameBufferHeight) * 2);
//        Log.i("--- dx " + dx + " dy " + dy + ", scrollX " + scrollX + " scrollY " + scrollY);
//        Log.i("--- dx : " + ((zoomScaleX - scaleX) * dx) + " dy : " + ((zoomScaleY - scaleY) * dy));
        dx = -((zoomScaleX - scaleX) * dx) + zoomScrollX;
        dy = -((zoomScaleY - scaleY) * dy) + zoomScrollY;
//        Log.i("--- dx " + dx + " dy " + dy);

        scrollX = setScaleMove(dx, scaleX);
        scrollY = setScaleMove(dy, scaleY);
//        Log.w("Result dx " + dx + " dy " + dy + " scrollX " + scrollX + " scrollY " + scrollY);
    }
}
