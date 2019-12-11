/*
 * MamaPlanner
 * Copyright (C) 2019 Domjos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.domjos.customwidgets.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import de.domjos.customwidgets.R;

import java.util.Random;

public class MessageHelper {
    private final static String id = "UniBuggerChannel";

    public static void printException(Exception ex, Context context) {
        try {
            if(ex!=null) {
                if(ex.getMessage()!=null) {
                    StringBuilder builder = new StringBuilder(ex.getMessage()).append("\n");
                    for (StackTraceElement element : ex.getStackTrace()) {
                        builder.append(String.format("%s.%s#%s(%s)%n", element.getFileName(), element.getClassName(), element.getMethodName(), element.getLineNumber()));
                    }
                    MessageHelper.printMessage(ex.toString(), context, false);
                    MessageHelper.log(ex, context);
                }
            }
        } catch (Exception ignored) {
        }
        Log.e("Exception", "Error", ex);
    }

    public static void printMessage(String message, Context context) {
        MessageHelper.printMessage(message, context, true);
    }

    private static void printMessage(String message, Context context, boolean log) {
        if (context instanceof Activity) {
            MessageHelper.printMessage(message, (Activity) context);
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        if (log) {
            if (MessageHelper.getSetting("swtLogCreateLog", true, context) &&
                    MessageHelper.getSetting("swtLogNormalMessages", false, context)) {

                MessageHelper.log(message, context);
            }
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
        if (context instanceof Activity) {
            if (MessageHelper.getSetting("swtLogCreateLog", true, context)) {
                LogHelper logHelper = new LogHelper((Activity) context);
                logHelper.logError(ex);
            }
        }
    }

    private static void log(String messages, Context context) {
        if (context instanceof Activity) {
            if (MessageHelper.getSetting("swtLogCreateLog", true, context)) {
                LogHelper logHelper = new LogHelper((Activity) context);
                logHelper.logMessage(messages);
            }
        }
    }

    private static void printMessage(String message, Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, activity.findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static int startProgressNotification(Activity activity, String title, String content, int icon) {
        MessageHelper.createChannel(activity.getApplicationContext());
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, id);
        Notification notification = builder.setContentTitle(title).setContentText(content).setSmallIcon(icon).setProgress(0, 0, true).build();
        Random random = new Random();
        int id = random.nextInt();
        if(manager!=null) {
            manager.notify(id, notification);
        }
        return id;
    }

    public static int showNotification(Context context, String title, String content, int icon) {
        MessageHelper.createChannel(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id);
        Notification notification = builder.setContentTitle(title).setContentText(content).setSmallIcon(icon).build();
        Random random = new Random();
        int id = random.nextInt();
        if(manager!=null) {
            manager.notify(id, notification);
        }
        return id;
    }

    public static void stopProgressNotification(Activity activity, int id) {
        NotificationManager manager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager!=null) {
            manager.cancel(id);
        }
    }

    private static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.app_name);
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
