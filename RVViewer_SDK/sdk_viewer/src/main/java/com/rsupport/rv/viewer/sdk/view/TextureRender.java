package com.rsupport.rv.viewer.sdk.view;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.rsupport.rv.viewer.sdk.view.gesture.CustomGesture;
import com.rsupport.rv.viewer.sdk.view.gl.GLTextureRender;
import com.rsupport.rv.viewer.sdk.view.utils.FloatRect;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by taehwan on 6/10/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class TextureRender implements GLSurfaceView.Renderer, OnTextureListener {

    public static final int TEXTURE_TYPE_VIDEO = 1;
    public static final int TEXTURE_TYPE_CONTROL = 0;

    @interface TextureType {
    }

    private SurfaceTexture surfaceTexture;
    private Surface surface;

    private GLTextureRender glTextureRender;

    private OnTextureListener onTextureListener;

    private int frameBufferWidth;
    private int frameBufferHeight;

    private boolean isInitDecoder;
    private boolean isConnected;

    @TextureType
    private int textureType;

    public TextureRender(@TextureType int textureType) {
        glTextureRender = new GLTextureRender();

        this.textureType = textureType;
        isInitDecoder = false;
        isConnected = false;
    }

    public void setOnTextureListener(OnTextureListener onTextureListener) {
        this.onTextureListener = onTextureListener;
    }

    public void release() {
        isConnected = false;
        isInitDecoder = false;

        if (surface != null) {
            surface.release();
        }
    }

    private void setup() {
        if (!isConnected) {
            return;
        }

        glTextureRender.surfaceCreated();

        // Even if we don't access the SurfaceTexture after the constructor returns, we
        // still need to keep a reference to it.  The Surface doesn't retain a reference
        // at the Java level, so if we don't either then the object can get GCed, which
        // causes the native finalizer to run.
        Log.d("TextureRender","textureID=" + glTextureRender.getTextureId());
        surfaceTexture = new SurfaceTexture(glTextureRender.getTextureId());

        surface = new Surface(surfaceTexture);
        onCreateTexture(surface);

        isInitDecoder = true;
    }

    public void isConnect() {
        isConnected = true;
    }

    public void setFrameBufferSize(int frameBufferWidth, int frameBufferHeight) {
        glTextureRender.setFrameBufferSize(frameBufferWidth, frameBufferHeight);

        this.frameBufferWidth = frameBufferWidth;
        this.frameBufferHeight = frameBufferHeight;
        Log.i("TextureRender","onSurfaceChanged frameBufferWidth : " + frameBufferWidth + ", frameBufferHeight " + frameBufferHeight);
    }

    /**
     * Original 사이즈를 정의한다.
     */
    private FloatRect setOriginalSize(int width, int height) {
        float x = CustomGesture.DEFAULT_SCALE_VALUE;
        float y = CustomGesture.DEFAULT_SCALE_VALUE;

        if (width > height) { // 현재 스크린이 가로 모드일 경우
            float scale = (float) height / (float) width;
            if (frameBufferWidth > frameBufferHeight) { // 가로 영상일 경우
                y = getYScale() / scale;
                if (y > CustomGesture.DEFAULT_SCALE_VALUE) { // y scale 이 1.0f 을 초과할 경우
                    x = getXScale() * scale;
                    y = CustomGesture.DEFAULT_SCALE_VALUE;
                }

            } else { // 세로 영상일 경우
                x = getXScale() * scale;

                if (x > CustomGesture.DEFAULT_SCALE_VALUE) { // x scale 이 1.0f 을 초과할 경우
                    x = CustomGesture.DEFAULT_SCALE_VALUE;
                    y = getYScale() / scale;
                }
            }

        } else { // 현재 스크린이 세로 모드일 경우
            float scale = (float) width / (float) height;
            if (frameBufferWidth > frameBufferHeight) { // 가로 영상일 경우
                y = getYScale() * scale;

                if (y > CustomGesture.DEFAULT_SCALE_VALUE) {
                    x = getXScale() / scale;
                    y = CustomGesture.DEFAULT_SCALE_VALUE;
                }

            } else { // 세로 영상일 경우
                x = getXScale() / scale;

                if (x > CustomGesture.DEFAULT_SCALE_VALUE) {
                    x = CustomGesture.DEFAULT_SCALE_VALUE;
                    y = getYScale() * scale;
                }
            }
        }
        return new FloatRect(x, y);
    }

    private float getYScale() {
        return ((float) frameBufferHeight / (float) frameBufferWidth);
    }

    private float getXScale() {
        return ((float) frameBufferWidth / (float) frameBufferHeight);
    }


    /**
     * {@link #textureType}에 따라서 Texture scale을 정의한다.
     */
    private void setTextureScaleSize(int width, int height) {
        if (glTextureRender != null) {
            if (textureType == TEXTURE_TYPE_VIDEO) {
                FloatRect floatRect = setOriginalSize(width, height);
                glTextureRender.setTextureScaleSize(floatRect.x, floatRect.y);

            } else {
                glTextureRender.setTextureScaleSize(CustomGesture.DEFAULT_SCALE_VALUE, CustomGesture.DEFAULT_SCALE_VALUE);
            }
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Do noting
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        setTextureScaleSize(width, height);
        glTextureRender.setSurfaceSize(width, height);
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!isInitDecoder) {
            setup();

        } else {
            if (onDraw()) {
                surfaceTexture.updateTexImage();
            }
            glTextureRender.drawFrame(surfaceTexture);
        }
    }

    @Override
    public void onCreateTexture(Surface surface) {
        if (onTextureListener != null) {
            onTextureListener.onCreateTexture(surface);
        }
    }

    @Override
    public boolean onDraw() {
        if (onTextureListener != null) {
            return onTextureListener.onDraw();
        }
        return false;
    }

    public void setRotation(int rotation) {
        glTextureRender.setRotation(rotation);
    }

    public int getFrameBufferWidth() {
        return frameBufferWidth;
    }

    public int getFrameBufferHeight() {
        return frameBufferHeight;
    }
}
