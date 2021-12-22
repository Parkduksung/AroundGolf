package com.rsupport.rv.viewer.sdk.view.utils;

/**
 * Created by taehwan on 6/10/15.
 */
public class FloatRect extends Rect<Float> {

    public FloatRect(Float x, Float y) {
        super(x, y);
    }

    @Override
    public void set(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
        x = 0.0f;
        y = 0.0f;
    }
}
