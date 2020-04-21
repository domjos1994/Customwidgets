/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import de.domjos.customwidgets.R;

import java.util.Random;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MessageHelper {
    private final static String id = "UniBuggerChannel";
    public final static int CANCEL_CODE = 2345;

    public static void printException(Exception ex, int icon, Context context) {
        try {
            if(ex!=null && ex.getMessage()!=null) {
                StringBuilder builder = new StringBuilder(ex.getMessage()).append("\n");
                for (StackTraceElement element : ex.getStackTrace()) {
                    builder.append(String.format("%s.%s#%s(%s)%n", element.getFileName(), element.getClassName(), element.getMethodName(), element.getLineNumber()));
                }
                MessageHelper.printMessage(ex.toString(), icon, context, false);
                MessageHelper.log(ex, context);
            }
        } catch (Exception ignored) {
        }
        Log.e("Exception", "Error", ex);
    }

    public static void printMessage(String message, int icon, Context context) {
        MessageHelper.printMessage(message, icon, context, true);
    }

    private static void printMessage(String message, int icon, Context context, boolean log) {
        if (context instanceof Activity) {
            MessageHelper.printMessage(message, icon, (Activity) context);
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        if (log && MessageHelper.getSetting("swtLogCreateLog", true, context) &&
                MessageHelper.getSetting("swtLogNormalMessages", false, context)) {
            MessageHelper.log(message, context);
        }
    }

    private static boolean getSetting(String key, boolean defaultValue, Context context) {
        SharedPreferences sharedPreferences;
        if (context instanceof Activity) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } else {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    private static void log(Exception ex, Context context) {
        if (context instanceof Activity && MessageHelper.getSetting("swtLogCreateLog", true, context)) {
            LogHelper logHelper = new LogHelper((Activity) context);
            logHelper.logError(ex);
        }
    }

    private static void log(String messages, Context context) {
        if (context instanceof Activity && MessageHelper.getSetting("swtLogCreateLog", true, context)) {
            LogHelper logHelper = new LogHelper((Activity) context);
            logHelper.logMessage(messages);
        }
    }

    private static void printMessage(String message, int icon, Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, activity.findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.text);
        ImageView iv = layout.findViewById(R.id.ivIcon);
        iv.setImageDrawable(WidgetUtils.getDrawable(activity, icon));
        text.setText(message);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static int startProgressNotification(Activity activity, String title, String content, int icon) {
        Random random = new Random();
        int id = random.nextInt();
        return MessageHelper.startProgressNotification(activity, title, content, icon, id);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static int startProgressNotification(Activity activity, String title, String content, int icon, Intent cancelIntent) {
        Random random = new Random();
        int id = random.nextInt();
        return MessageHelper.startProgressNotification(activity, title, content, icon, id, cancelIntent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static int startProgressNotification(Activity activity, String title, String content, int icon, int notId) {
        return MessageHelper.startProgressNotification(activity, title, content, icon, notId, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static int startProgressNotification(Activity activity, String title, String content, int icon, int notId, Intent cancelIntent) {
        MessageHelper.createChannel(activity.getApplicationContext());
        NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, id);

        if(cancelIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                cancelIntent.putExtra(EXTRA_NOTIFICATION_ID, notId);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, MessageHelper.CANCEL_CODE, cancelIntent, 0);
            builder.addAction(R.drawable.ic_cancel_black_24dp, activity.getString(R.string.sys_cancel), pendingIntent);
        }

        Notification notification = builder.setContentTitle(title).setContentText(content).setSmallIcon(icon).setProgress(0, 0, true).build();
        if(manager!=null) {
            manager.notify(notId, notification);
        }
        return notId;
    }


    public static int showNotification(Context context, String title, String content, int icon) {
        MessageHelper.createChannel(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);
        Notification notification = builder.setContentTitle(title).setContentText(content).setSmallIcon(icon).build();
        Random random = new Random();
        int id = random.nextInt();
        if(manager!=null) {
            manager.notify(id, notification);
        }
        return id;
    }

    public static int showNotification(Context context, String title, String content, int icon, Intent intent, int requestCode) {
        MessageHelper.createChannel(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);
        Notification notification = builder.setContentTitle(title).setContentText(content).setSmallIcon(icon).build();
        builder.setContentIntent(PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Random random = new Random();
        int id = random.nextInt();
        if(manager!=null) {
            manager.notify(id, notification);
        }
        return id;
    }

    public static void stopNotification(Context context, int id) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if(manager!=null) {
            manager.cancel(id);
        }
    }

    private static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.module_name);
            String description = context.getString(R.string.module_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager!=null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
