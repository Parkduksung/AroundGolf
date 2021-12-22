//package com.rsupport.rv.viewer.sdk.view.quickmenu;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.rsupport.rv.viewer.sdk_viewer.R;
//
///**
// * Created by hyosang on 2017. 5. 19..
// */
//
//public class QuickMenuGridItem extends RelativeLayout {
//
//    protected ImageView icon;
//    protected TextView label;
//    private QuickMenuItemData itemData;
//
//    public QuickMenuGridItem(Context context) {
//        super(context);
//
//        LayoutInflater.from(context).inflate(R.layout.quickmenu_grid_item, this, true);
//
//        icon = (ImageView) findViewById(R.id.quickmenu_grid_icon);
//        label = (TextView) findViewById(R.id.quickmenu_grid_label);
//    }
//
//    public void setAppInfo(QuickMenuItemData item) {
//        this.itemData = item;
//
//        icon.setImageResource(item.gridIconResId);
//        label.setText(item.appNameString);
//    }
//
//    public QuickMenuItemData getAppData() {
//        return this.itemData;
//    }
//}
