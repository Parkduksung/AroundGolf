package com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsupport.rv.viewer.sdk_viewer.R;


/**
 * Created by hyosang on 2017. 5. 19..
 */

public class DragableItemViewHolder extends BaseViewHolder {
    public View mainView;
    public View upperLine;
    public ImageView btnRemove;
    public TextView appName;
    public ImageView dragHandle;
    public View lowerLine;

    public DragableItemViewHolder(View itemView, View.OnTouchListener dragStarter) {
        super(itemView);

        mainView = itemView;
//        upperLine = itemView.findViewById(R.id.quickmenu_upperline);
//        lowerLine = itemView.findViewById(R.id.quickmenu_lowerline);
//        btnRemove = (ImageView) itemView.findViewById(R.id.btn_quickmenu_del);
//        appName = (TextView) itemView.findViewById(R.id.quickmenu_appname);
//        dragHandle = (ImageView) itemView.findViewById(R.id.quickmenu_drag);
        dragHandle.setTag(this);
        dragHandle.setOnTouchListener(dragStarter);
    }

    public void setDragging(boolean bDrag) {
        lowerLine.setVisibility(bDrag ? View.VISIBLE : View.GONE);
        itemView.setAlpha(bDrag ? 0.9f : 1.0f);
    }
}
