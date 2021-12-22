//package com.rsupport.rv.viewer.sdk.view.quickmenu;
//
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.rsupport.rv.viewer.sdk.common.log.RLog;
//import com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder.BaseViewHolder;
//import com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder.DragableItemViewHolder;
//import com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder.PresetHeaderViewHolder;
//import com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder.PresetItemViewHolder;
//import com.rsupport.rv.viewer.sdk.view.quickmenu.viewholder.SelectedHeaderViewHolder;
//import com.rsupport.rv.viewer.sdk_viewer.R;
//
//import java.util.Collections;
//
///**
// * Created by hyosang on 2017. 5. 19..
// */
//
//public class QuickMenuEditViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
//    public interface IQuickMenuEditListener {
//        boolean onDelete(int currentCount);
//        boolean onAdd(int currentCount);
//    }
//
//    private List<QuickMenuItemSelected> selectedList = new ArrayList<QuickMenuItemSelected>();
//    private List<QuickMenuItemGroup> presetGroups = new ArrayList<QuickMenuItemGroup>();
//
//    private ItemTouchHelper touchHelper;
//    private DragableItemViewHolder currentMoveItem = null;
//    private IQuickMenuEditListener listener;
//
//    public QuickMenuEditViewAdapter() {
//        super();
//
//        listener = defaultListener;
//
//        touchHelper = new ItemTouchHelper(itemTouchCallback);
//    }
//
//    public void attachTouchHelper(RecyclerView view) {
//        touchHelper.attachToRecyclerView(view);
//    }
//
//    @Override
//    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View v = null;
//        BaseViewHolder viewHolder = null;
//
//        if(viewType == BaseViewHolder.TYPE_SELECTED_HEADER) {
//            v = inflater.inflate(R.layout.quickmenu_edit_header, parent, false);
//            viewHolder = new SelectedHeaderViewHolder(v);
//        }else if(viewType == BaseViewHolder.TYPE_PRESET_HEADER) {
//            v = inflater.inflate(R.layout.quickmenu_edit_preset_header, parent, false);
//            viewHolder = new PresetHeaderViewHolder(v);
//        }else if(viewType == BaseViewHolder.TYPE_PRESET) {
//            v = inflater.inflate(R.layout.quickmenu_edit_preset_item, parent, false);
//            viewHolder = new PresetItemViewHolder(v);
//        }else if(viewType == BaseViewHolder.TYPE_SELECTED) {
//            v = inflater.inflate(R.layout.quickmenu_edit_drag_item, parent, false);
//            viewHolder = new DragableItemViewHolder(v, dragStarter);
//        }else if(viewType == BaseViewHolder.TYPE_LAST_LINE) {
//            v = inflater.inflate(R.layout.quickmenu_edit_footer, parent, false);
//            viewHolder = new BaseViewHolder(v);
//        }
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        Object item = getItemObject(position);
//
//        if((position == 0) && (holder instanceof SelectedHeaderViewHolder)) {
//            //내 퀵메뉴 헤더
//            SelectedHeaderViewHolder vh = (SelectedHeaderViewHolder) holder;
//            vh.countText.setText(String.valueOf(selectedList.size()));
//        }else if((item instanceof QuickMenuItemGroup) && (holder instanceof PresetHeaderViewHolder)) {
//            //각 어플리케이션별 헤더
//            PresetHeaderViewHolder vh = (PresetHeaderViewHolder) holder;
//            QuickMenuItemGroup group = (QuickMenuItemGroup) item;
//
//            vh.headerText.setText(group.headerTitleResId);
//            vh.countText.setText(String.valueOf(group.items.size()));
//            if(group.items.size() == 0) {
//                vh.btnToggle.setVisibility(View.GONE);
//            }else {
//                vh.btnToggle.setVisibility(View.VISIBLE);
//
//                if(group.isExpand) {
//                    vh.btnToggle.setImageResource(R.drawable.img_category_close);
//                }else {
//                    vh.btnToggle.setImageResource(R.drawable.img_category_open);
//                }
//            }
//            vh.mainView.setOnClickListener(expandToggleListener);
//            vh.mainView.setTag(group);
//        }else if((item instanceof QuickMenuItemSelected) && (holder instanceof DragableItemViewHolder)) {
//            //선택된 항목 아이템들
//            DragableItemViewHolder vh = (DragableItemViewHolder) holder;
//            QuickMenuItemSelected data = (QuickMenuItemSelected) item;
//            vh.appName.setText(data.appNameString);
//            if(data == selectedList.get(0)) {
//                vh.upperLine.setBackgroundColor(0xFFD9D9D9);
//            }else {
//                vh.upperLine.setBackgroundColor(0xFFE5E5E5);
//            }
//            vh.lowerLine.setVisibility(View.GONE);
//
//            vh.btnRemove.setOnClickListener(removeListener);
//            vh.btnRemove.setTag(data);
//        }else if((item instanceof QuickMenuItemPreset) && (holder instanceof PresetItemViewHolder)) {
//            //선택가능한 항목 아이템들
//            PresetItemViewHolder vh = (PresetItemViewHolder) holder;
//            QuickMenuItemPreset data = (QuickMenuItemPreset) item;
//            vh.appName.setText(data.appNameString);
//            vh.btnAddIcon.setOnClickListener(addListener);
//            vh.btnAddIcon.setTag(data);
//        }else if(position == (getItemCount() - 1)) {
//            //Footer. nothing to do.
//        }else {
//            RLog.w("Not matches viewholder type and data type. pos=" + position + ", viewholder=" + holder + ", data=" + item);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = 0;
//
//        count += 1;     /* 내 퀵메뉴 헤더 */
//        count += selectedList.size();
//
//        for(QuickMenuItemGroup group : presetGroups) {
//            count += 1;     /* 각 그룹 헤더 */
//
//            if(group.isExpand) {
//                count += group.items.size();
//            }
//        }
//
//        count += 1;     /* 마지막 라인 */
//
//        return count;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(position == 0) {
//            return BaseViewHolder.TYPE_SELECTED_HEADER;
//        }else if(position == (getItemCount() - 1)) {
//            return BaseViewHolder.TYPE_LAST_LINE;
//        }else {
//            Object obj = getItemObject(position);
//
//            if (obj instanceof QuickMenuItemGroup) {
//                return BaseViewHolder.TYPE_PRESET_HEADER;
//            } else if (obj instanceof QuickMenuItemPreset) {
//                return BaseViewHolder.TYPE_PRESET;
//            } else if (obj instanceof QuickMenuItemSelected) {
//                return BaseViewHolder.TYPE_SELECTED;
//            } else {
//                RLog.w("Cannot determine item type: " + position);
//            }
//        }
//
//        return 0;
//    }
//
//    public String[] getSelectedKeys() {
//        String [] keys = new String[selectedList.size()];
//        for(int i=0;i<selectedList.size();i++) {
//            keys[i] = selectedList.get(i).getKey();
//        }
//
//        return keys;
//    }
//
//    private int getSelectedBaseIndex() {
//        return 1;
//    }
//
//    public Object getItemObject(int position) {
//        if(position == 0) return null;
//        position--;
//
//        if(position < selectedList.size()) return selectedList.get(position);
//        position -= selectedList.size();
//
//        for(QuickMenuItemGroup group : presetGroups) {
//            if(position == 0) return group;
//            position--;
//
//            if(group.isExpand) {
//                if(position < group.items.size()) return group.items.get(position);
//                position -= group.items.size();
//            }
//        }
//
//        return null;
//    }
//
//    public void addSelectedItem(QuickMenuItemSelected item) {
//        selectedList.add(item);
//    }
//
//    public void addPresetItemGroup(QuickMenuItemGroup group) {
//        presetGroups.add(group);
//    }
//
//    public void addPresetItemGroup(List<QuickMenuItemGroup> list) {
//        presetGroups.addAll(list);
//    }
//
//    public void presetToSelected(String key) {
//        for(QuickMenuItemGroup group : presetGroups) {
//            QuickMenuItemPreset item = group.getItem(key);
//            if(item != null) {
//                group.removeItem(item);
//                selectedList.add(new QuickMenuItemSelected(group.getGroupId(), item));
//            }
//        }
//
//        notifyDataSetChanged();
//    }
//
//    public void selectedToPreset(QuickMenuItemSelected item) {
//        boolean bFound = false;
//        for(QuickMenuItemGroup group : presetGroups) {
//            if(group.getGroupId() == item.presetGroupId) {
//                selectedList.remove(item);
//                group.addItem(new QuickMenuItemPreset(item));
//                group.sort();
//                bFound = true;
//                break;
//            }
//        }
//
//        if(!bFound) {
//            RLog.w("Cannot find preset group: " + item.getKey());
//
//            //remove anyway.
//            selectedList.remove(item);
//        }
//
//        notifyDataSetChanged();
//    }
//
//    private boolean moveItem(int oldPosition, int newPosition) {
//        int baseIdx = getSelectedBaseIndex();
//        int oldp = oldPosition - baseIdx;
//        int newp = newPosition - baseIdx;
//
//        Collections.swap(selectedList, oldp, newp);
//
//        return true;
//    }
//
//    public void setListener(IQuickMenuEditListener listener) {
//        if(listener == null) {
//            this.listener = defaultListener;
//        }else {
//            this.listener = listener;
//        }
//    }
//
//    private IQuickMenuEditListener defaultListener = new IQuickMenuEditListener() {
//        @Override
//        public boolean onDelete(int currentCount) {
//            return true;
//        }
//
//        @Override
//        public boolean onAdd(int currentCount) {
//            return true;
//        }
//    };
//
//    private View.OnClickListener addListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Object tag = v.getTag();
//            if(tag instanceof QuickMenuItemPreset) {
//                if(listener.onAdd(selectedList.size())) {
//                    QuickMenuItemPreset item = (QuickMenuItemPreset) tag;
//                    presetToSelected(item.getKey());
//                }
//            }
//        }
//    };
//
//    private View.OnClickListener removeListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Object tag = v.getTag();
//            if(tag instanceof QuickMenuItemSelected) {
//                if(listener.onDelete(selectedList.size())) {
//                    QuickMenuItemSelected item = (QuickMenuItemSelected) tag;
//                    selectedToPreset(item);
//                }
//            }
//        }
//    };
//
//    private View.OnClickListener expandToggleListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Object tag = v.getTag();
//            if(tag instanceof QuickMenuItemGroup) {
//                QuickMenuItemGroup group = (QuickMenuItemGroup) tag;
//                group.isExpand = !group.isExpand;
//            }
//            notifyDataSetChanged();
//        }
//    };
//
//    private View.OnTouchListener dragStarter = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//                Object t = v.getTag();
//                if(t instanceof DragableItemViewHolder) {
//                    if(currentMoveItem != null) {
//                        currentMoveItem.setDragging(false);
//                    }
//                    DragableItemViewHolder vh = (DragableItemViewHolder) t;
//                    currentMoveItem = vh;
//                    vh.setDragging(true);
//                    touchHelper.startDrag(vh);
//                    return true;
//                }
//                return false;
//            }
//
//            return false;
//        }
//    };
//
//
//    private ItemTouchHelper.Callback itemTouchCallback = new ItemTouchHelper.Callback() {
//
//        @Override
//        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//            int dragFlags = 0;//ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//            int swipeFlags = 0;//ItemTouchHelper.START | ItemTouchHelper.END;
//
//            if(viewHolder instanceof DragableItemViewHolder) {
//                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//            }else {
//                dragFlags = 0;
//            }
//
//            return makeMovementFlags(dragFlags, swipeFlags);
//        }
//
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            int oldp = viewHolder.getAdapterPosition();
//            int newp = target.getAdapterPosition();
//
//            if(getItemObject(newp) instanceof QuickMenuItemSelected) {
//                //can move.
//                if(moveItem(oldp, newp)) {
//                    notifyItemMoved(oldp, newp);
//                    return true;
//                }
//            }
//
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//        }
//
//        @Override
//        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//            super.onSelectedChanged(viewHolder, actionState);
//
//            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
//                //Drag ended
//                if(currentMoveItem != null) {
//                    currentMoveItem.setDragging(false);
//                    currentMoveItem = null;
//                }
//            }
//        }
//    };
//}
