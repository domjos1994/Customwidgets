package de.domjos.customwidgets.io.converter;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.domjos.customwidgets.model.BaseDescriptionObject;
import de.domjos.customwidgets.sample.R;
import de.domjos.customwidgets.sample.utils.Helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConverterTest {
    private List<Object> objects;
    private int icon;
    private String file;

    @Before
    public void init() {
        this.icon = R.drawable.ic_cancel_black_24dp;
        this.file = Helper.getContext().getFilesDir().getAbsolutePath() + File.separatorChar + "test.";

        this.objects = new LinkedList<>();
        for(int i = 0; i<=9; i++) {
            BaseDescriptionObject baseDescriptionObject = new BaseDescriptionObject();
            baseDescriptionObject.setTitle("title " + i);
            baseDescriptionObject.setDescription("description " + i);
            baseDescriptionObject.setId(i);

            BaseDescriptionObject subBaseDescriptionObject = new BaseDescriptionObject();
            subBaseDescriptionObject.setTitle("subTitle " + i);
            subBaseDescriptionObject.setDescription("subDescription " + i);
            subBaseDescriptionObject.setId(i);
            baseDescriptionObject.setObject(subBaseDescriptionObject);

            this.objects.add(baseDescriptionObject);
        }
    }

    @Test
    public void testObjectToPDF() throws Exception {
        ObjectToPDF objectToPDF = new ObjectToPDF(this.objects, this.file + "pdf", Helper.getContext(), this.icon);
        objectToPDF.doExport();

        assertTrue(new File(this.file + "pdf").exists());
    }

    @Test
    public void testObjectToText() throws Exception {
        ObjectToText objectToText = new ObjectToText(this.objects, this.file + "csv", Helper.getContext(), this.icon);
        objectToText.doExport();

        assertTrue(new File(this.file + "csv").exists());
    }

    @Test
    public void testObjectToXML() throws Exception {
        ObjectToXML objectToXML = new ObjectToXML(this.objects, this.file + "xml", Helper.getContext());
        objectToXML.doExport();

        assertTrue(new File(this.file + "xml").exists());
    }

    @Test
    public void testTextToObject() throws Exception {
        TextToObject<BaseDescriptionObject> textToObject = new TextToObject<>(BaseDescriptionObject.class, this.file + "csv", Helper.getContext());
        List<BaseDescriptionObject> baseDescriptionObjects = textToObject.doImport();

        assertEquals(10, baseDescriptionObjects.size());
    }

    @Test
    public void testXMLToObject() throws Exception {
        XMLToObject<BaseDescriptionObject> xmlToObject = new XMLToObject<>(BaseDescriptionObject.class, this.file + "xml", Helper.getContext());
        List<BaseDescriptionObject> baseDescriptionObjects = xmlToObject.doImport();

        assertEquals(10, baseDescriptionObjects.size());
    }
}
