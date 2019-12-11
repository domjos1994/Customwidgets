package de.domjos.customwidgets.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Map;

import de.domjos.customwidgets.R;

public class Helper {

    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            return Locale.GERMAN;
        } else {
            return Locale.ENGLISH;
        }
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundToActivity(Activity activity, Map.Entry<String, byte[]> entry, int resId) {
        if(entry!=null) {
            if(!entry.getKey().equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    activity.getWindow().getDecorView().getRootView().setBackground(new BitmapDrawable(activity.getResources(), BitmapFactory.decodeByteArray(entry.getValue(), 0, entry.getValue().length)));
                } else {
                    activity.getWindow().getDecorView().getRootView().setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeByteArray(entry.getValue(), 0, entry.getValue().length)));
                }
                return;
            }
        }
        activity.getWindow().getDecorView().getRootView().setBackgroundResource(resId);
    }

    public static void printException(Context context, Throwable ex) {
        StringBuilder message = new StringBuilder(ex.getMessage() + "\n" + ex.toString());
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
