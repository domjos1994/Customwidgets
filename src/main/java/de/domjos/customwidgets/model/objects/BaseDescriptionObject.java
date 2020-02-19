/*
 * Copyright (C) 2017-2019  Dominic Joas
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.model.objects;

import java.util.Arrays;

public class BaseDescriptionObject extends BaseObject {
    private String description;
    private Object object;
    private byte[] cover;
    private boolean selected;
    private boolean state;

    public BaseDescriptionObject() {
        super();
        this.description = "";
        this.object = null;
        this.cover = null;
        this.selected = false;
        this.state = false;
    }

    public BaseDescriptionObject(Object object) {
        super();
        this.description = "";
        this.object = object;
        this.cover = null;
        this.selected = false;
        this.state = false;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public byte[] getCover() {
        if(this.cover != null) {
            return this.cover.clone();
        } else {
            return null;
        }
    }

    public void setCover(byte[] cover) {
        if(cover == null) {
            this.cover = null;
        } else {
            this.cover = Arrays.copyOf(cover, cover.length);
        }
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
