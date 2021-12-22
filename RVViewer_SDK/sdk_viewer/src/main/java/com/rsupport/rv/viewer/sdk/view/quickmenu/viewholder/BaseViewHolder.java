package com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hyosang on 2017. 5. 19..
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_SELECTED_HEADER = 0x01;
    public static final int TYPE_PRESET_HEADER = 0x02;
    public static final int TYPE_SELECTED = 0x03;
    public static final int TYPE_PRESET = 0x04;
    public static final int TYPE_LAST_LINE = 0x05;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
}
