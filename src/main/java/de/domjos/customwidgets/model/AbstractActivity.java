/*
 * Copyright (C) 2017-2019  Dominic Joas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import de.domjos.customwidgets.utils.Helper;

public abstract class AbstractActivity extends AppCompatActivity {
    private int id;
    private Map.Entry<String, byte[]> entry;
    private boolean noBackground;
    private int resId;

    public AbstractActivity(int id, Map.Entry<String, byte[]> entry, int resId) {
        super();
        this.id = id;
        this.entry = entry;
        this.noBackground = false;
        this.resId = resId;
    }

    public AbstractActivity(int id) {
        super();
        this.id = id;
        this.entry = null;
        this.noBackground = true;
        this.resId = 0;
    }

    public void setBackground(Map.Entry<String, byte[]> entry) {
        this.entry = entry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.id);
        this.initControls();
        this.hideExperimentalFeatures();
        this.initValidator();
        this.initActions();
        if(!this.noBackground) {
            Helper.setBackgroundToActivity(this, this.entry, this.resId);
        }
        this.reload();
        this.manageControls(false, true, false);
    }

    protected void reload() {

    }

    protected void manageControls(boolean editMode, boolean reset, boolean selected) {

    }

    protected abstract void initControls();
    protected void initValidator() {}
    protected abstract void initActions();
    protected void hideExperimentalFeatures() {}
}
