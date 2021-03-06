/*
 * Copyright (C)  2019-2020 Domjos
 *  This file is part of UniTrackerMobile <https://unitrackermobile.de/>.
 *
 *  UniTrackerMobile is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  UniTrackerMobile is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with UniTrackerMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;

import java.lang.ref.WeakReference;

import de.domjos.customwidgets.utils.MessageHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

public abstract class AbstractTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private WeakReference<Context> weakReference;
    private final int icon;
    private int id = -1;
    private final String title, content;
    private boolean showNotifications;
    private PostExecuteListener postExecuteListener;
    private PreExecuteListener preExecuteListener;
    private boolean progress;
    final static CurrentTask CURRENT_TASK = new CurrentTask();
    protected NotificationCompat.Builder builder;
    protected final NotificationManager manager;

    public AbstractTask(Activity activity, int title, int content, boolean showNotifications, int icon) {
        super();

        this.manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        AbstractTask.CURRENT_TASK.setAbstractTask(this);
        this.weakReference = new WeakReference<>(activity);
        this.icon = icon;
        this.title = activity.getString(title);
        this.content = activity.getString(content);
        this.showNotifications = showNotifications;
        this.progress = false;
    }

    AbstractTask(Activity activity, int title, int content, boolean showNotifications, int icon, boolean progress) {
        super();

        this.manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        AbstractTask.CURRENT_TASK.setAbstractTask(this);
        this.weakReference = new WeakReference<>(activity);
        this.icon = icon;
        this.title = activity.getString(title);
        this.content = activity.getString(content);
        this.showNotifications = showNotifications;
        this.progress = progress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Intent intent = new Intent(this.getContext(), Receiver.class);
        intent.putExtra("id", this.id);
        if (this.showNotifications) {
            if(this.progress) {
                this.builder = MessageHelper.returnProgressNotification((Activity) this.getContext(), this.title, this.content, this.icon, this.id, intent, 100, 0);
            } else {
                MessageHelper.startProgressNotification((Activity) this.getContext(), this.title, this.content, this.icon, this.id, intent);
            }
        }

        if(this.preExecuteListener != null) {
            this.preExecuteListener.onPreExecute();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (this.showNotifications) {
            MessageHelper.stopNotification(this.getContext(), this.id);
        }
        if (this.postExecuteListener != null) {
            this.postExecuteListener.onPostExecute(result);
        }
    }

    public void printMessage(String message) {
        ((Activity) this.getContext()).runOnUiThread(() -> MessageHelper.printMessage(message, this.icon, this.getContext()));
    }

    public void printException(Exception ex) {
        ((Activity) this.getContext()).runOnUiThread(() -> MessageHelper.printException(ex, this.icon, this.getContext()));
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void before(PreExecuteListener preExecuteListener) {
        this.preExecuteListener = preExecuteListener;
    }

    public void after(PostExecuteListener postExecuteListener) {
        this.postExecuteListener = postExecuteListener;
    }

    protected Context getContext() {
        return this.weakReference.get();
    }

    @FunctionalInterface
    public interface PostExecuteListener<Result> {
        void onPostExecute(Result result);
    }

    @FunctionalInterface
    public interface PreExecuteListener {
        void onPreExecute();
    }
}
