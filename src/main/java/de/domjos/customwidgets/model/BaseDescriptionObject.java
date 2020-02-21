/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model;

import androidx.annotation.NonNull;
import java.util.Arrays;

/**
 * Item-Class for the Swipe-Refresh-Delete-List
 * @see de.domjos.customwidgets.widgets.swiperefreshdeletelist.SwipeRefreshDeleteList
 * @author Dominic Joas
 */
public class BaseDescriptionObject {
    private long id;
    private String title;
    private String description;
    private Object object;
    private byte[] cover;
    private boolean selected;
    private boolean state;

    /**
     * Default-Constructor
     */
    public BaseDescriptionObject() {
        super();
        this.id = 0;
        this.title = "";
        this.description = "";
        this.object = null;
        this.cover = null;
        this.selected = false;
        this.state = false;
    }


    /**
     * Constructor with the Object as Param
     * @param object Object as Param
     */
    public BaseDescriptionObject(Object object) {
        super();
        this.id = 0;
        this.title = "";
        this.description = "";
        this.object = object;
        this.cover = null;
        this.selected = false;
        this.state = false;
    }

    /**
     * Gets the ID of the Item
     * @return the ID
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the ID of the Item
     * @param id the ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the Title of the Item
     * @return the Title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the Title of the Item
     * @param title the Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the Description of the Item
     * @return the Description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the Description of the Item
     * @param description the Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the Object of the Item
     * @return the Object
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * Sets the Object of the Item
     * @param object the Object
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Gets the Cover of the Item
     * @return the Cover
     */
    public byte[] getCover() {
        if(this.cover != null) {
            return this.cover.clone();
        } else {
            return null;
        }
    }

    /**
     * Sets the Cover of the Item
     * @param cover the Cover
     */
    public void setCover(byte[] cover) {
        if(cover == null) {
            this.cover = null;
        } else {
            this.cover = Arrays.copyOf(cover, cover.length);
        }
    }

    /**
     * Shows whether the Item is selected
     * @return the Item is selected
     */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Sets the Selection of the Item
     * @param selected the Selection
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Shows whether the Item is positive
     *
     * Notice: The Item changes the Background-Color
     * if selected and attribute "listItemBackgroundStatePositive" is Set
     * @return the Item is positive
     */
    public boolean isState() {
        return this.state;
    }

    /**
     * Sets the State of the Item
     *
     * Notice: The Item changes the Background-Color
     * if selected and attribute "listItemBackgroundStatePositive" is Set
     * @param state the State
     */
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    @NonNull
    public String toString() {
        return this.title;
    }
}
