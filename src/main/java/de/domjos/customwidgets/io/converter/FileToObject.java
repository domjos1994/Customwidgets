package de.domjos.customwidgets.io.converter;

import android.content.Context;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public abstract class FileToObject<T> {
    protected String path;
    protected Context context;
    protected Class<T> cls;

    public FileToObject(Class<T> cls, String path, Context context) {
        this.path = path;
        this.context = context;
        this.cls = cls;
    }

    public List<T> doImport() throws Exception {
        List<T> exportObjects;
        try {
            this.openFile();

            exportObjects = this.getObjects();
        } finally {
            this.closeFile();
        }
        return exportObjects;
    }

    protected abstract void openFile() throws Exception;
    protected abstract List<T> getObjects() throws Exception;
    protected abstract void closeFile() throws Exception;

    protected void setField(Class<?> cls, String name, Object value, Object instance) throws Exception {
        Field field = FieldUtils.getField(cls, name, true);
        if(field.getType() == Long.class || field.getType() == long.class) {
            FieldUtils.writeField(field, instance, Long.parseLong(value.toString()));
            return;
        }
        if(field.getType() == Integer.class || field.getType() == int.class) {
            FieldUtils.writeField(field, instance, Integer.parseInt(value.toString()));
            return;
        }
        if(field.getType() == Double.class || field.getType() == double.class) {
            FieldUtils.writeField(field, instance, Double.parseDouble(value.toString()));
            return;
        }
        if(field.getType() == Float.class || field.getType() == float.class) {
            FieldUtils.writeField(field, instance, Float.parseFloat(value.toString()));
            return;
        }
        if(field.getType() == String.class || field.getType() == char.class) {
            FieldUtils.writeField(field, instance, value.toString());
            return;
        }
        if(field.getType() == Date.class) {
            FieldUtils.writeField(field, instance, Date.parse(value.toString()));
            return;
        }
        if(field.getType() == Object.class) {
            FieldUtils.writeField(field, instance, value);
        }
    }
}
