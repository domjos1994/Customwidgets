package de.domjos.customwidgets.model.tasks;

public final class ExtendedTaskStatus<T> extends TaskStatus {
    private T object;

    public ExtendedTaskStatus(int status, String message, T object) {
        super(status, message);

        this.object = object;
    }

    public T getObject() {
        return this.object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
