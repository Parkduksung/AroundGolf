//package com.rsupport.rv.viewer.sdk.view.quickmenu;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.widget.FrameLayout;
//
//import com.rsupport.rv.viewer.sdk.common.log.RLog;
//import com.rsupport.rv.viewer.sdk_viewer.R;
//
//
///**
// * Created by hyosang on 2017. 5. 19..
// */
//
//public class QuickMenuGridView extends FrameLayout {
//    public interface IQuickMenuListener {
//        void onApplicationClicked(QuickMenuItemData app);
//        void onAddApplication();
//    }
//
//    private static final int BGCOLOR_A = R.drawable.selector_quickmenu_grid_a;
//    private static final int BGCOLOR_B = R.drawable.selector_quickmenu_grid_b;
//
//    private static final int[] BG_PORT = {BGCOLOR_A, BGCOLOR_B, BGCOLOR_B, BGCOLOR_A, BGCOLOR_A, BGCOLOR_B, BGCOLOR_B, BGCOLOR_A};
//    private static final int[] BG_LAND = {BGCOLOR_A, BGCOLOR_B, BGCOLOR_A, BGCOLOR_B, BGCOLOR_B, BGCOLOR_A, BGCOLOR_B, BGCOLOR_A};
//
//    private int [] bgcolors = BG_PORT;
//    private int [][] positions = new int[8][2];
//
//    private QuickMenuGridItem[] viewItems = new QuickMenuGridItem[8];
//    private QuickMenuGridAddItem addItemView;
//    private int currentCount = 0;
//    private IQuickMenuListener listener = null;
//
//    private int itemWidth = 0;
//    private int itemHeight = 0;
//    private int itemSpace = 0;
//    private int horiPadding = 0;
//    private int vertPadding = 0;
//    private boolean measurementsUpdated = false;
//
//    public QuickMenuGridView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        if("true".equals(context.getResources().getString(R.string.is_xlarge))) {
//            //xlarge (tablet)
//            horiPadding = (int) (metrics.density * 10f);
//            itemSpace = (int)(metrics.density * 5f);
//            vertPadding = (int)(metrics.density * 5f);
//        }else {
//            horiPadding = (int) (metrics.density * 17f);
//            itemSpace = (int) (metrics.density * 6f);
//            vertPadding = 0;
//        }
//
//        //각 아이템 미리 만들어놓음
//        for(int i=0;i<8;i++) {
//            QuickMenuGridItem itemView = new QuickMenuGridItem(context);
//            addView(itemView);
//            itemView.setVisibility(View.GONE);
//            itemView.setOnClickListener(itemClicked);
//            viewItems[i] = itemView;
//        }
//        addItemView = new QuickMenuGridAddItem(context);
//        addView(addItemView);
//        addItemView.setOnClickListener(itemClicked);
//        addItemView.setVisibility(View.GONE);
//    }
//
//    public void reset() {
//        currentCount = 0;
//    }
//
//    public int getCurrentCount() {
//        return currentCount;
//    }
//
//    public void addQuickMenuItem(QuickMenuItemData item) {
//        if(currentCount < viewItems.length) {
//            viewItems[currentCount].setAppInfo(item);
//
//            currentCount++;
//        }else {
//            RLog.w("Cannot add QuickMenu item. Current count = " + currentCount);
//        }
//
//        rearrangeItemViews();
//    }
//
//    public void setListener(IQuickMenuListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if(getWidth() > getHeight()) {
//            //landscape
//            itemWidth = (getWidth() - (horiPadding * 2) - (itemSpace * 3)) / 4;
//            itemHeight = (getHeight() - (vertPadding * 2) - itemSpace) / 2;
//            calcPresetsLand();
//        }else {
//            //portrait
//            itemWidth = (getWidth() - (horiPadding * 2) - itemSpace) / 2;
//            itemHeight = (getHeight() - (vertPadding * 2) - (itemSpace * 3)) / 4;
//            calcPresetsPort();
//        }
//
//        measurementsUpdated = true;
//
//        super.onLayout(changed, left, top, right, bottom);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if(measurementsUpdated) {
//            rearrangeItemViews();
//
//            measurementsUpdated = false;
//        }
//
//        super.onDraw(canvas);
//    }
//
//    private void calcPresetsPort() {
//        bgcolors = BG_PORT;
//
//        int top = 0;
//        for(int i=0;i<8;i++) {
//            positions[i][0] = ((i % 2) == 0) ? horiPadding : (horiPadding + itemWidth + itemSpace);
//            positions[i][1] = top;
//
//            if((i % 2) == 1) {
//                top += (itemHeight + itemSpace);
//            }
//        }
//    }
//
//    private void calcPresetsLand() {
//        bgcolors = BG_LAND;
//
//        int left = horiPadding;
//        for(int i=0;i<8;i++) {
//            positions[i][0] = left;
//            positions[i][1] = (i < 4) ? 0 : (itemHeight + itemSpace);
//
//            if(i == 3) {
//                left = horiPadding;
//            }else {
//                left += (itemSpace + itemWidth);
//            }
//        }
//    }
//
//    public void rearrangeItemViews() {
//        int i;
//        for(i=0;i<currentCount;i++) {
//            View v = viewItems[i];
//
//            LayoutParams lp = (LayoutParams) v.getLayoutParams();
//            lp.width = itemWidth;
//            lp.height = itemHeight;
//            lp.leftMargin = positions[i][0];
//            lp.topMargin = positions[i][1];
//            v.setLayoutParams(lp);
//
//            v.setBackgroundResource(bgcolors[i]);
//
//            v.setVisibility(View.VISIBLE);
//        }
//
//        if(i == 8) {
//            addItemView.setVisibility(View.GONE);
//        }else {
//            addItemView.setVisibility(View.VISIBLE);
//
//            LayoutParams lp = (LayoutParams) addItemView.getLayoutParams();
//            lp.width = itemWidth;
//            lp.height = itemHeight;
//            lp.leftMargin = positions[i][0];
//            lp.topMargin = positions[i][1];
//            addItemView.setLayoutParams(lp);
//        }
//
//        //나머지는 보이지 않게 처리
//        for( ; i<8;i++) {
//            viewItems[i].setVisibility(View.GONE);
//        }
//    }
//
//    public QuickMenuItemData getAppInfo(View v) {
//        for(QuickMenuGridItem item : viewItems) {
//            if(item == v) {
//                return item.getAppData();
//            }
//        }
//
//        return null;
//    }
//
//    private OnClickListener itemClicked = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(v == addItemView) {
//                if(listener != null) {
//                    listener.onAddApplication();
//                }
//
//            }else if(v instanceof QuickMenuGridItem) {
//                QuickMenuItemData data = ((QuickMenuGridItem) v).getAppData();
//                if(data != null) {
//                    if(listener != null) {
//                        listener.onApplicationClicked(data);
//                    }
//                }else {
//                    RLog.w("No AppInfo");
//                }
//
//            }
//        }
//    };
//}
