package de.domjos.customwidgets.model.tasks;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class ExtendedStatusTask<Params, Result> extends AbstractTask<Params, ExtendedTaskStatus<Result>, List<Result>> {
    private WeakReference<ProgressBar> progressBar;
    private WeakReference<TextView> status;
    private UpdateProgressListener<Result> updateProgressListener;
    protected int max;

    public ExtendedStatusTask(Activity activity, int title, int content, boolean showNotifications, int icon, ProgressBar progressBar, TextView status) {
        super(activity, title, content, showNotifications, icon);

        this.progressBar = new WeakReference<>(progressBar);
        this.status = new WeakReference<>(status);
    }

    @SafeVarargs
    protected final void onProgressUpdate(ExtendedTaskStatus<Result>... values) {
        ((Activity) this.getContext()).runOnUiThread(()->{
            this.progressBar.get().setProgress((int) (values[0].getStatus() / (this.max / 100.0)));
            this.status.get().setText(values[0].getMessage());

            if(this.updateProgressListener != null) {
                this.updateProgressListener.onUpdate(values[0].getObject());
            }
        });
    }

    public void setUpdateProgressListener(UpdateProgressListener<Result> updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

    @FunctionalInterface
    public interface UpdateProgressListener<Result> {
        void onUpdate(Result result);
    }
}
