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
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.domjos.customwidgets.R;
import de.domjos.customwidgets.model.BaseDescriptionObject;

public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private TextView mTitle, mSubTitle;
    private CheckBox chkSelector;
    private ImageView ivIcon;
    private RelativeLayout rl;

    private int menuId;
    private String currentTitle;
    private LinearLayout controls;
    private List<BaseDescriptionObject> data;
    private SwipeRefreshDeleteList.ReloadListener reloadListener;
    private Activity activity;
    private RecyclerAdapter recyclerAdapter;

    RecycleViewHolder(@NonNull View itemView, int menuId, String currentTitle, boolean readOnly, boolean scrollList, LinearLayout controls, SwipeRefreshDeleteList.ReloadListener reloadListener, List<BaseDescriptionObject> data, Activity activity, int color, RecyclerAdapter recyclerAdapter) {
        super(itemView);

        this.menuId = menuId;
        this.currentTitle = currentTitle;
        this.controls = controls;
        this.reloadListener = reloadListener;
        this.data = data;
        this.activity = activity;
        this.recyclerAdapter = recyclerAdapter;


        rl = itemView.findViewById(R.id.itemLayout);
        mTitle = itemView.findViewById(R.id.lblTitle);
        mSubTitle = itemView.findViewById(R.id.lblSubTitle);
        this.ivIcon = itemView.findViewById(R.id.ivIcon);

        if(scrollList) {
            this.mTitle.setMaxLines(1);
            this.mTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            this.mTitle.setMarqueeRepeatLimit(-1);
            this.mTitle.setSingleLine(true);
            this.mTitle.setSelected(true);
        }

        chkSelector = itemView.findViewById(R.id.chkSelector);
        chkSelector.setChecked(false);
        chkSelector.setVisibility(this.recyclerAdapter.showCheckboxes ? View.VISIBLE : View.GONE);

        itemView.setOnCreateContextMenuListener(this);

        if(this.menuId==-1) {
            itemView.setOnLongClickListener(view -> {
                if(!readOnly) {
                    this.recyclerAdapter.showCheckboxes = !this.recyclerAdapter.showCheckboxes;
                    controls.setVisibility(this.recyclerAdapter.showCheckboxes ? View.VISIBLE : View.GONE);
                    this.chkSelector.setVisibility(this.recyclerAdapter.showCheckboxes ? View.VISIBLE : View.GONE);
                    if(reloadListener!=null) {
                        reloadListener.onReload();
                        for(int i = 0; i<=data.size()-1; i++) {
                            data.get(i).setSelected(false);
                            chkSelector.setChecked(false);
                        }
                    }
                }
                return true;
            });
        }

        if(color != 0) {
            mTitle.setTextColor(color);
            mSubTitle.setTextColor(color);
        }
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    void setSubTitle(String subTitle) {
        this.mSubTitle.setText(subTitle);
    }

    ImageView getIconView() {
        return this.ivIcon;
    }

    void setBackground(Drawable drawable) {
        this.rl.setBackground(drawable);
    }

    CheckBox getSelector() {
        return this.chkSelector;
    }

    boolean isShowCheckBoxes() {
        return this.recyclerAdapter.showCheckboxes;
    }

    String getCurrentTitle() {
        return this.currentTitle;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        if(this.menuId != -1) {
            contextMenu.add(R.string.sys_multiple).setOnMenuItemClickListener(menuItem -> {
                this.recyclerAdapter.showCheckboxes = !this.recyclerAdapter.showCheckboxes;
                controls.setVisibility(this.recyclerAdapter.showCheckboxes ? View.VISIBLE : View.GONE);
                chkSelector.setVisibility(this.recyclerAdapter.showCheckboxes ? View.VISIBLE : View.GONE);
                if(reloadListener!=null) {
                    reloadListener.onReload();
                    for(int i = 0; i<=data.size()-1; i++) {
                        data.get(i).setSelected(false);
                        chkSelector.setChecked(false);
                    }
                }
                return true;
            });
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(this.menuId, contextMenu);
        }
    }
}
