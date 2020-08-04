package de.domjos.customwidgets.io.converter;

import android.content.Context;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.domjos.customwidgets.io.PDFWriter;
import de.domjos.customwidgets.utils.ConvertHelper;

public final class ObjectToPDF extends ObjectToFile {
    private PDFWriter pdfWriter;
    private int icon;
    private int background;
    private String title, subTitle;

    public ObjectToPDF(List<Object> objects, String path, Context context, Object... params) {
        super(objects, path, context);

        this.icon = -1;
        this.background = -1;
        this.title = "";
        this.subTitle = "";
        if(params.length > 0) {
            this.icon = (int) params[0];
        }
        if(params.length > 1) {
            this.background = (int) params[1];
        }
        if(params.length > 2) {
            this.title = (String) params[2];
        }
        if(params.length > 3) {
            this.subTitle = (String) params[3];
        }
    }

    @Override
    protected void openFile() throws Exception {
        this.pdfWriter = new PDFWriter(super.path, super.context, this.icon);

        if(this.icon != -1 && !this.title.equals("")) {
            byte[] iconSrc = ConvertHelper.convertDrawableToByteArray(super.context, this.icon);
            byte[] bgSrc = null;
            if(this.background != -1) {
                bgSrc = ConvertHelper.convertDrawableToByteArray(super.context, this.background);
            }
            this.pdfWriter.addFooter(bgSrc, iconSrc);
            this.pdfWriter.addHeadPage(ConvertHelper.convertDrawableToByteArray(super.context, this.icon), this.title, this.subTitle);
        }
    }

    @Override
    protected void writeObject(ExportObject exportObject) {
        this.writeExportObjectToTable(null, exportObject);
        this.pdfWriter.newPage();
    }


    private void writeExportObjectToTable(String name, ExportObject exportObject) {
        if(name != null) {
            this.pdfWriter.addParagraph(name, PDFWriter.FontFormat.Header2, PDFWriter.Position.Center, 2.0f);
        }

        Map<String, ExportObject> complicatedObjects = new LinkedHashMap<>();
        Map<String, String> simpleObjects = new LinkedHashMap<>();

        for(Map.Entry<String, Object> entry : exportObject.getProperties().entrySet()) {
            if(entry.getValue() instanceof ExportObject) {
                complicatedObjects.put(entry.getKey(), (ExportObject) entry.getValue());
            } else if(entry.getValue() instanceof List) {
                StringBuilder items = new StringBuilder();
                List<?> ls = (List<?>) entry.getValue();
                for(Object item : ls) {
                    items.append(item).append(", ");
                }
                simpleObjects.put(entry.getKey(), items.toString());
            } else {
                simpleObjects.put(entry.getKey(), entry.getValue().toString());
            }
        }

        List<List<String>> tableContent = new LinkedList<>();
        for(Map.Entry<String, String> simpleObject : simpleObjects.entrySet()) {
            tableContent.add(Arrays.asList(simpleObject.getKey(), simpleObject.getValue()));
        }
        Map<String, Float> columns = new LinkedHashMap<>();
        columns.put("Name", 1.0f);
        columns.put("Value", 1.0f);
        this.pdfWriter.addTable(columns, tableContent, android.R.color.darker_gray, android.R.color.holo_blue_light, 1.0f);

        for(Map.Entry<String, ExportObject> entry : complicatedObjects.entrySet()) {
            this.writeExportObjectToTable(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void closeFile() {
        if(this.pdfWriter != null) {
            this.pdfWriter.close();
        }
    }
}
