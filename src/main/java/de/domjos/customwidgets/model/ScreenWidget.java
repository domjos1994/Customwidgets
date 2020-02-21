/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model;

import android.app.Activity;
import android.view.View;

/**
 * A Parent-Class for Screen-Widgets which contains several useful Functionalities
 * @author Dominic Joas
 */
public abstract class ScreenWidget {
    protected View view;
    protected Activity activity;

    /**
     * Constructor with the Main-View and the Activity
     * @param view The Main-View
     * @param activity The Activity
     */
    public ScreenWidget(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }

    /**
     * Sets the Visibility of the View
     * @param visibility The Visibility of the View
     */
    public void setVisibility(boolean visibility) {
        if(visibility) {
            this.view.setVisibility(View.VISIBLE);
        } else {
            this.view.setVisibility(View.GONE);
        }
    }

    /**
     * Abstract-Method to initialize the Items
     */
    public abstract void init();
}
