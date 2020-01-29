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

    public BaseDescriptionObject() {
        super();
        this.description = "";
        this.object = null;
        this.cover = null;
    }

    public BaseDescriptionObject(Object object) {
        super();
        this.description = "";
        this.object = object;
        this.cover = null;
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
        return this.cover;
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
}
