/*
 * Copyright (C) 2017-2019  Dominic Joas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model;

import android.app.Activity;
import android.view.View;

public abstract class ScreenWidget {
    protected View view;
    protected Activity activity;

    public ScreenWidget(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    public void setVisibility(boolean visibility) {
        if(visibility) {
            this.view.setVisibility(View.VISIBLE);
        } else {
            this.view.setVisibility(View.GONE);
        }
    }

    public abstract void init();
}
