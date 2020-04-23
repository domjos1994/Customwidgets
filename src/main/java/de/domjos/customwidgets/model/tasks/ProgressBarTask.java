package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public abstract class ProgressBarTask<Params, Result> extends AbstractTask<Params, Integer, Result> {
    private WeakReference<ProgressBar> progressBar;
    protected int max;

    public ProgressBarTask(Activity activity, int title, int content, boolean showNotifications, int icon, ProgressBar progressBar) {
        super(activity, title, content, showNotifications, icon);

        this.progressBar = new WeakReference<>(progressBar);
        this.max = 100;
    }

    protected void onProgressUpdate(Integer... values) {
        ((Activity) this.getContext()).runOnUiThread(()->this.progressBar.get().setProgress((int) (values[0] / (this.max / 100.0))));
    }
}
