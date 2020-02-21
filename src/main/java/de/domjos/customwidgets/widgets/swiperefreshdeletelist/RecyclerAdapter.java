/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.widgets.swiperefreshdeletelist;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import de.domjos.customwidgets.R;
import de.domjos.customwidgets.model.BaseDescriptionObject;

public class RecyclerAdapter extends RecyclerView.Adapter<RecycleViewHolder> {
    private List<BaseDescriptionObject> data;
    private View.OnClickListener mClickListener;
    private RecyclerView recyclerView;
    private int menuId = -1;
    int noEntryItem = -1;
    private Activity activity;
    private String currentTitle;
    private Drawable icon, background, backgroundStatePositive;
    private LinearLayout controls;
    private SwipeRefreshDeleteList.ReloadListener reloadListener;
    private boolean readOnly;
    private ItemTouchHelper itemTouchHelper;
    private View lastView;
    private boolean lastPositive;
    private int color;
    public boolean showCheckboxes;

    RecyclerAdapter(RecyclerView recyclerView, Activity activity, Drawable drawable, Drawable background, Drawable backgroundStatePositive, LinearLayout controls, boolean readOnly, int color, boolean showCheckboxes) {
        this.data = new LinkedList<>();
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.icon = drawable;
        this.background = background;
        this.backgroundStatePositive = backgroundStatePositive;
        this.controls = controls;
        this.readOnly = readOnly;
        this.color = color;
        this.itemTouchHelper = null;
        this.showCheckboxes = showCheckboxes;
    }

    void setSelectedView(View view, boolean positive) {
        this.lastView = view;
        this.lastPositive = positive;
    }

    void resetLastView() {
        if(this.lastView != null) {
            if(this.lastPositive) {
                if(this.backgroundStatePositive != null) {
                    this.lastView.setBackground(this.backgroundStatePositive);
                }
            } else {
                this.lastView.setBackground(this.background);
            }
        }
    }

    void reload(SwipeRefreshDeleteList.ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    public BaseDescriptionObject getObject() {
        if(this.currentTitle!=null && !this.currentTitle.isEmpty()) {
            for (BaseDescriptionObject listObject : data) {
                if (listObject.getTitle().equals(this.currentTitle)) {
                    return listObject;
                }
            }
        }
        return null;
    }

    @Override
    @NonNull
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecycleViewHolder(itemView, this.menuId, this.currentTitle, this.readOnly, this.controls, this.reloadListener, this.data, this.activity, this.color, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        if(data!=null && data.get(position)!=null) {
            boolean showCheckBoxes = holder.isShowCheckBoxes();
            holder.setTitle(data.get(position).getTitle());
            holder.setSubTitle(data.get(position).getDescription());
            byte[] cover = data.get(position).getCover();
            if(cover!=null) {
                holder.getIconView().setImageBitmap(BitmapFactory.decodeByteArray(cover, 0, cover.length));
            } else {
                if (this.icon != null) {
                    holder.getIconView().setImageDrawable(this.icon);
                }
            }
            holder.itemView.setOnClickListener(view -> {
                if (mClickListener != null) {
                    mClickListener.onClick(view);
                }
            });
            if(this.background!=null) {
                holder.setBackground(this.background);
            }
            if(this.backgroundStatePositive != null && data.get(position).isState()) {
                holder.setBackground(this.backgroundStatePositive);
            }
            holder.getSelector().setChecked(false);
            holder.getSelector().setVisibility(showCheckBoxes ? View.VISIBLE : View.GONE);
            holder.getSelector().setOnCheckedChangeListener((compoundButton, b) -> data.get(position).setSelected(b));
            this.currentTitle = holder.getCurrentTitle();
        }
    }

    void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    void onSwipeListener(SwipeToDeleteCallback callback) {
        if(callback != null) {
            this.itemTouchHelper = new ItemTouchHelper(callback);
            this.itemTouchHelper.attachToRecyclerView(this.recyclerView);
        }
    }

    void setContextMenu(int menuId) {
        this.menuId = menuId;
    }

    public int getItemPosition(BaseDescriptionObject listObject) {
        try {
            int position  = 0;
            for(BaseDescriptionObject obj : data) {
                if(obj.getTitle().equals(listObject.getTitle())) {
                    return position;
                }
                position++;
            }
        } catch (Exception ignored) {}
        return -1;
    }


    public BaseDescriptionObject getItem(int position) {
        if(position==noEntryItem) {
            return null;
        } else {
            return data.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void deleteItem(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = data.size();
        if (size > 0) {
            data.subList(0, size).clear();
            notifyItemRangeRemoved(0, size);
        }

        BaseDescriptionObject baseDescriptionObject = new BaseDescriptionObject();
        baseDescriptionObject.setTitle(this.activity.getString(R.string.main_noEntry));
        this.data.add(baseDescriptionObject);
        this.noEntryItem = this.data.indexOf(baseDescriptionObject);
    }

    public void add(BaseDescriptionObject object) {
        if (this.noEntryItem != -1) {
            this.data.remove(this.noEntryItem);
            this.noEntryItem = -1;
        }

        data.add(object);

        synchronized (this) {
            notifyDataSetChanged();
        }
    }

    public List<BaseDescriptionObject> getList() {
        return this.data;
    }
}
