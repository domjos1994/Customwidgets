package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public abstract class StatusTask<Params, Result> extends AbstractTask<Params, TaskStatus, Result> {
    private WeakReference<ProgressBar> progressBar;
    private WeakReference<TextView> status;
    private boolean showNotifications;
    protected int max;

    public StatusTask(Activity activity, int title, int content, boolean showNotifications, int icon, ProgressBar progressBar, TextView status) {
        super(activity, title, content, showNotifications, icon, true);

        this.progressBar = new WeakReference<>(progressBar);
        this.status = new WeakReference<>(status);
        this.showNotifications = showNotifications;
    }

    protected void onProgressUpdate(TaskStatus... values) {
        final int percentage = (int) (values[0].getStatus() / (this.max / 100.0));
        ((Activity) this.getContext()).runOnUiThread(()->{
            this.progressBar.get().setProgress(percentage);
            this.status.get().setText(values[0].getMessage());
        });

        if(this.showNotifications) {
            if(this.builder != null) {
                this.builder.setProgress(100, percentage, false);
                if(this.manager != null) {
                    this.manager.notify(-1, this.builder.build());
                }
            }
        }
    }
}
