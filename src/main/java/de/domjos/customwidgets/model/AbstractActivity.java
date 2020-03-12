/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import de.domjos.customwidgets.utils.Helper;

/**
 * A Parent-Class for Activity which contains several useful Functionalities
 * and basic methods to fill
 * @author Dominic Joas
 */
public abstract class AbstractActivity extends AppCompatActivity {
    private int id;
    private Map.Entry<String, byte[]> entry;
    private boolean noBackground;
    private int resId;
    protected Bundle savedInstanceState;

    /**
     * Constructor with Background-Image for the Activity
     * @param id The Layout-ID from the Resources
     * @param entry The Entry with the Image-Name and the Content (Can be null)
     * @param resId The Image from the Resources (Will be taken if entry is null)
     */
    public AbstractActivity(int id, Map.Entry<String, byte[]> entry, int resId) {
        super();

        this.id = id;
        this.entry = entry;
        this.noBackground = false;
        this.resId = resId;
    }

    /**
     * Constructor with no Image for the Activity
     * @param id The Layout-ID from the Resources
     */
    public AbstractActivity(int id) {
        super();

        this.id = id;
        this.entry = null;
        this.noBackground = true;
        this.resId = 0;
    }

    /**
     * Sets the Background-Entry of the Image
     * @param entry The Entry of the Image
     */
    public void setBackground(Map.Entry<String, byte[]> entry) {
        this.entry = entry;
    }

    /**
     * The OnCreate Method with the Function-Calls from this Class
     * @see androidx.appcompat.app.AppCompatActivity#onCreate(Bundle, PersistableBundle)
     *
     * Notice: Don't call manually when extending this Class
     * @param savedInstanceState The PersistableBundle
     */
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

    /**
     * Method to reload things like lists...
     */
    protected void reload() {

    }

    /**
     * Method to manage the Controls of an Activity
     * @param editMode Controls are enabled
     * @param reset Control-Values will be reseted
     * @param selected Item of a List is selected
     */
    protected void manageControls(boolean editMode, boolean reset, boolean selected) {

    }

    /**
     * Finishes Activity on Back Pressed
     */
    @Override
    public void onBackPressed() {
        this.setResult(RESULT_OK);
        this.finish();
    }

    /**
     * Method to initialize the Controls of an Activity
     */
    protected abstract void initControls();

    /**
     * Method to initialize the Validators
     * @see de.domjos.customwidgets.utils.Validator
     */
    protected void initValidator() {}

    /**
     * Method to initialize the Actions like onClick of an Activity
     */
    protected abstract void initActions();

    /**
     * Method to hide experimental Features of an Activity
     */
    protected void hideExperimentalFeatures() {}
}
