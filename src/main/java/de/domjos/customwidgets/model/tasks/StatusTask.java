package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public abstract class StatusTask<Params, Result> extends AbstractTask<Params, TaskStatus, Result> {
    private WeakReference<ProgressBar> progressBar;
    private WeakReference<TextView> status;
    protected int max;

    public StatusTask(Activity activity, int title, int content, boolean showNotifications, int icon, ProgressBar progressBar, TextView status) {
        super(activity, title, content, showNotifications, icon);

        this.progressBar = new WeakReference<>(progressBar);
        this.status = new WeakReference<>(status);
    }

    protected void onProgressUpdate(TaskStatus... values) {
        ((Activity) this.getContext()).runOnUiThread(()->{
            this.progressBar.get().setProgress((int) (values[0].getStatus() / (this.max / 100.0)));
            this.status.get().setText(values[0].getMessage());
        });
    }
}
