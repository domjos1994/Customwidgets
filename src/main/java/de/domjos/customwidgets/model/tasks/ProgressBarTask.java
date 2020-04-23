package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.content.Intent;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import de.domjos.customwidgets.utils.MessageHelper;

public abstract class ProgressBarTask<Params, Result> extends AbstractTask<Params, Integer, Result> {
    private WeakReference<ProgressBar> progressBar;
    private final int icon;
    private final String title, content;
    private boolean showNotifications;
    protected int max;

    public ProgressBarTask(Activity activity, int title, int content, boolean showNotifications, int icon, ProgressBar progressBar) {
        super(activity, title, content, showNotifications, icon, true);

        this.progressBar = new WeakReference<>(progressBar);
        this.title = activity.getString(title);
        this.content = activity.getString(content);
        this.icon = icon;
        this.showNotifications = showNotifications;
    }

    protected void onProgressUpdate(Integer... values) {
        int percentage = (int) (values[0] / (this.max / 100.0));
        ((Activity) this.getContext()).runOnUiThread(()->this.progressBar.get().setProgress(percentage));

        Intent intent = new Intent(this.getContext(), Receiver.class);
        int id = -1;
        intent.putExtra("id", id);
        if(this.showNotifications) {
            MessageHelper.startProgressNotification(((Activity) this.getContext()), this.title, this.content, this.icon, id, intent, 100, percentage);
        }
    }
}
