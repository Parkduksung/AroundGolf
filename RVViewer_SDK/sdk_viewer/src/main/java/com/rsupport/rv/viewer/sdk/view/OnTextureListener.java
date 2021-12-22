package com.rsupport.rv.viewer.sdk.view;

import android.view.Surface;

/**
 * Created by taehwan on 6/10/15.
 */
public interface OnTextureListener {

    /**
     * Surface가 만들어지면 callback
     */
    void onCreateTexture(Surface surface);

    /**
     * onDraw 시 호출
     */
    boolean onDraw();
}
