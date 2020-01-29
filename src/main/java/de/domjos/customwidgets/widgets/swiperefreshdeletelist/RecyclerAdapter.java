/*
 * Copyright (C)  2019 Domjos
 * This file is part of UniTrackerMobile <https://github.com/domjos1994/UniTrackerMobile>.
 *
 * UniTrackerMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UniTrackerMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UniTrackerMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package de.domjos.customwidgets.widgets.swiperefreshdeletelist;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.LinkedList;
import java.util.List;

import de.domjos.customwidgets.R;
import de.domjos.customwidgets.model.objects.BaseDescriptionObject;

public class RecyclerAdapter extends Adapter<RecyclerAdapter.RecycleViewHolder> {
    private List<BaseDescriptionObject> data;
    private View.OnClickListener mClickListener;
    private RecyclerView recyclerView;
    private int menuId = -1;
    int noEntryItem = -1;
    private Activity activity;
    private String currentTitle;
    private Drawable icon, background;
    private LinearLayout controls;
    private SwipeRefreshDeleteList.ReloadListener reloadListener;
    private boolean showCheckBoxes, readOnly;

    class RecycleViewHolder extends ViewHolder implements View.OnCreateContextMenuListener {
        private TextView mTitle, mSubTitle;
        private CheckBox chkSelector;
        private ImageView ivIcon;
        private RelativeLayout rl;

        RecycleViewHolder(View itemView) {
            super(itemView);

            rl = itemView.findViewById(R.id.rl);
            mTitle = itemView.findViewById(R.id.lblTitle);
            mSubTitle = itemView.findViewById(R.id.lblSubTitle);
            ivIcon = itemView.findViewById(R.id.ivIcon);

            chkSelector = itemView.findViewById(R.id.chkSelector);
            chkSelector.setChecked(false);

            itemView.setOnCreateContextMenuListener(this);

            if(menuId==-1) {
                itemView.setOnLongClickListener(view -> {
                    if(!readOnly) {
                        showCheckBoxes = !showCheckBoxes;
                        controls.setVisibility(showCheckBoxes ? View.VISIBLE : View.GONE);
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
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (menuId != -1) {
                currentTitle = mTitle.getText().toString();
                menu.add(R.string.sys_multiple).setOnMenuItemClickListener(menuItem -> {
                    showCheckBoxes = !showCheckBoxes;
                    controls.setVisibility(showCheckBoxes ? View.VISIBLE : View.GONE);
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
                inflater.inflate(menuId, menu);
            }
        }
    }

    RecyclerAdapter(RecyclerView recyclerView, Activity activity, Drawable drawable, Drawable background, LinearLayout controls, boolean readOnly) {
        this.data = new LinkedList<>();
        this.recyclerView = recyclerView;
        this.activity = activity;
        this.icon = drawable;
        this.background = background;
        this.controls = controls;
        this.readOnly = readOnly;
    }

    void reload(SwipeRefreshDeleteList.ReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    public BaseDescriptionObject getObject() {
        if(this.currentTitle!=null) {
            if (!this.currentTitle.isEmpty()) {
                for (BaseDescriptionObject listObject : data) {
                    if (listObject.getTitle().equals(this.currentTitle)) {
                        return listObject;
                    }
                }
            }
        }
        return null;
    }

    @Override
    @NonNull
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecycleViewHolder(itemView);
    }

    void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }

    void onSwipeListener(SwipeToDeleteCallback callback) {
        if(callback!=null) {
            new ItemTouchHelper(callback).attachToRecyclerView(this.recyclerView);
        }
    }

    void setContextMenu(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        if(data!=null) {
            if(data.get(position)!=null) {
                holder.mTitle.setText(data.get(position).getTitle());
                holder.mSubTitle.setText(data.get(position).getDescription());
                byte[] cover = data.get(position).getCover();
                if(cover!=null) {
                    holder.ivIcon.setImageBitmap(BitmapFactory.decodeByteArray(cover, 0, cover.length));
                } else {
                    if (this.icon != null) {
                        holder.ivIcon.setImageDrawable(this.icon);
                    }
                }
                holder.itemView.setOnClickListener(view -> {
                    if (mClickListener != null) {
                        mClickListener.onClick(view);
                    }
                });
                if(this.background!=null) {
                    holder.rl.setBackground(this.background);
                }
                holder.chkSelector.setChecked(false);
                holder.chkSelector.setVisibility(this.showCheckBoxes ? View.VISIBLE : View.GONE);
                holder.chkSelector.setOnCheckedChangeListener((compoundButton, b) -> data.get(position).setSelected(b));
            }
        }
    }

    public BaseDescriptionObject getItem(int position) {
        if(position==noEntryItem) {
            return null;
        } else {
            return data.get(position);
        }
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
