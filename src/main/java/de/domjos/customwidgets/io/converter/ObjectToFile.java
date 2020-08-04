package de.domjos.customwidgets.io.converter;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class ObjectToFile {
    private List<ExportObject> objects;
    protected String path;
    protected Context context;
    protected Object[] params;

    public ObjectToFile(List<Object> objects, String path, Context context, Object... params) {
        this.objects = this.convertToExportObjects(objects);
        this.path = path;
        this.context = context;
        this.params = params;
    }

    public void doExport() throws Exception {
        try {
            this.openFile();

            for(ExportObject exportObject : this.objects) {
                this.writeObject(exportObject);
            }
        } finally {
            this.closeFile();
        }
    }

    protected abstract void openFile() throws Exception;
    protected abstract void writeObject(ExportObject exportObject) throws Exception;
    protected abstract void closeFile() throws Exception;

    private List<ExportObject> convertToExportObjects(List<Object> objects) {
        List<ExportObject> exportObjects = new LinkedList<>();

        if(objects != null) {
            if(!objects.isEmpty()) {
                for(Object object : objects) {
                    try {
                        if(object != null) {
                            exportObjects.add(this.convertToExportObject(object));
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        return exportObjects;
    }

    private ExportObject convertToExportObject(Object object) {
        ExportObject exportObject = new ExportObject();

        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                if(field.getType().isPrimitive()) {
                    exportObject.addProperty(field.getName(), field.get(object));
                } else if(field.getType() == String.class) {
                    exportObject.addProperty(field.getName(), String.valueOf(field.get(object)));
                } else if(field.getType().isArray()) {
                    Object[] items = (Object[]) field.get(object);
                    List<Object> objects = new LinkedList<>(Arrays.asList(Objects.requireNonNull(items)));
                    exportObject.addProperty(field.getName(), objects);
                } else {
                    exportObject.addProperty(field.getName(), this.convertToExportObject(Objects.requireNonNull(field.get(object))));
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        return exportObject;
    }
}
