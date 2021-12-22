package com.rsupport.rv.viewer.sdk.view.utils;

/**
 * Created by taehwan on 6/10/15.
 */
public abstract class Rect<K> {

    public K x;
    public K y;

    public Rect() {
        init();
    }

    public Rect(K x, K y) {
        this.x = x;
        this.y = y;
    }

    public abstract void set(K x, K y);

    public abstract void init();
}
