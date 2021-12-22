package com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rsupport.rv.viewer.sdk_viewer.R;


/**
 * Created by hyosang on 2017. 5. 19..
 */

public class PresetHeaderViewHolder extends BaseViewHolder {
    public View mainView;
    public TextView headerText;
    public TextView countText;
    public ImageView btnToggle;

    public PresetHeaderViewHolder(View itemView) {
        super(itemView);

        mainView = itemView;
//        headerText = (TextView) itemView.findViewById(R.id.quickmenu_edit_header_title);
//        countText = (TextView) itemView.findViewById(R.id.quickmenu_edit_header_count);
//        btnToggle = (ImageView) itemView.findViewById(R.id.btn_quickmenu_group_folding);
    }
}
