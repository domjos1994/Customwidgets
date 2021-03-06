/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;

public class WidgetUtils {

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int resource) {
        if(resource == -1) {
            return null;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return context.getDrawable(resource);
            } else {
                return context.getResources().getDrawable(resource);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int resource) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.getColor(resource);
            } else {
                return context.getResources().getColor(resource);
            }
        } catch (Exception ex) {
            return -1;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getRaw(Context context, int rawID) throws IOException {
        byte[] b;
        Resources res = context.getResources();
        try (InputStream in_s = res.openRawResource(rawID)) {

            b = new byte[in_s.available()];
            in_s.read(b);
        }
        return new String(b);
    }
}
