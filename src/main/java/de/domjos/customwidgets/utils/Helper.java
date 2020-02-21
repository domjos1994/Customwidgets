/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Map;

public class Helper {

    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return Locale.GERMAN;
        } else {
            return Locale.ENGLISH;
        }
    }

    public static void setBackgroundToActivity(Activity activity, Map.Entry<String, byte[]> entry, int resId) {
        if(entry!=null && !entry.getKey().equals("")) {
            activity.getWindow().getDecorView().getRootView().setBackground(new BitmapDrawable(activity.getResources(), BitmapFactory.decodeByteArray(entry.getValue(), 0, entry.getValue().length)));
            return;
        }
        activity.getWindow().getDecorView().getRootView().setBackgroundResource(resId);
    }

    public static void printException(Context context, Throwable ex) {
        String init = ex.getMessage() + "\n" + ex.toString();
        StringBuilder message = new StringBuilder(init);
        for(StackTraceElement element : ex.getStackTrace()) {
            message.append(element.getFileName()).append(":").append(element.getClassName()).append(":").append(element.getMethodName()).append(":").append(element.getLineNumber());
        }
        Log.e("Exception", message.toString(), ex);
        Helper.createToast(context, ex.getLocalizedMessage());
    }

    private static void createToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
