package de.domjos.customwidgets.io;

import android.content.Context;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.domjos.customwidgets.utils.MessageHelper;

public class PDFWriter {
    private final Map<FontFormat, Font> fonts;
    private final Map<Position, Integer> positions;
    private final WeakReference<Context> context;
    private final int icon;

    private Document document;
    private PdfWriter pdfWriter;


    public PDFWriter(String path, Context context, int icon) throws Exception {
        this.fonts = new LinkedHashMap<>();
        this.positions = new LinkedHashMap<>();
        this.context = new WeakReference<>(context);
        this.icon = icon;

        this.fillFonts();
        this.fillPositions();

        this.document = new Document();
        this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(path));
        this.document.open();
    }

    public void addHeadPage(byte[] icon, String title, String subTitle) {
        try {
            this.addParagraph(title, FontFormat.Header3, Position.Center, 40f);
            this.addImage(icon, Position.Center, 256f, 256f, 10f);
            this.addParagraph(subTitle, FontFormat.Header3, Position.Center, 30f);
            this.newPage();
        } catch (Exception ex) {
            MessageHelper.printException(ex, this.icon, this.context.get());
        }
    }

    public void addFooter(byte[] background, byte[] icon) {
        this.pdfWriter.setPageEvent(new HeaderFooter(0, background, icon));
    }

    public void addTable(Map<String, Float> columns, List<List<String>> content, int headerColor, int contentColor, float padding) {
        try {
            float[] columnSizes = new float[columns.values().size()];
            int i = 0;
            for(Float size : columns.values()) {
                columnSizes[i] = size;
                i++;
            }

            PdfPTable pdfPTable = new PdfPTable(columnSizes);
            pdfPTable.setSpacingBefore(padding);
            pdfPTable.setSpacingAfter(padding);
            for(String columnHeader : columns.keySet()) {
                PdfPCell cell = new PdfPCell(new Phrase(columnHeader, this.fonts.get(FontFormat.Header5)));
                cell.setBackgroundColor(new BaseColor(headerColor));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setPadding(8.0f);
                pdfPTable.addCell(cell);
            }

            for(List<String> row : content) {
                for(String cellContent : row) {
                    PdfPCell cell = new PdfPCell(new Phrase(cellContent, this.fonts.get(FontFormat.Content)));
                    cell.setBackgroundColor(new BaseColor(contentColor));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setPadding(8.0f);
                    pdfPTable.addCell(cell);
                }
            }
            this.document.add(pdfPTable);
        } catch (Exception ex) {
            MessageHelper.printException(ex, this.icon, this.context.get());
        }
    }

    public void addParagraph(String content, FontFormat font, Position position, float padding) {
        try {
            Paragraph paragraph = new Paragraph(content, this.fonts.get(font));

            Integer pos = this.positions.get(position);
            if(pos != null) {
                paragraph.setAlignment(pos);
            }

            paragraph.setSpacingBefore(padding);
            paragraph.setSpacingAfter(padding);
            this.document.add(paragraph);
        } catch (Exception ex) {
            MessageHelper.printException(ex, this.icon, this.context.get());
        }
    }

    public void addImage(byte[] imageContent, Position position, float maxWidth, float maxHeight, float padding) {
        try {
            Image image = Image.getInstance(imageContent);
            float imgWidth = image.getWidth();
            float imgHeight = image.getHeight();

            float width = imgWidth;
            float height = imgHeight;
            if(maxHeight<imgHeight) {
                float factor = maxHeight /imgHeight;
                width = imgWidth * factor;
                height = maxHeight;
            } else if(maxWidth<imgWidth) {
                float factor = maxWidth /imgWidth;
                width = maxWidth;
                height = imgHeight * factor;
            }

            image.scaleAbsolute(width, height);
            image.setSpacingBefore(padding);
            image.setSpacingAfter(padding);

            Integer pos = this.positions.get(position);
            if(pos != null) {
                image.setAlignment(pos);
            }

            this.document.add(image);
        } catch (Exception ex) {
            MessageHelper.printException(ex, this.icon, this.context.get());
        }
    }

    public void newPage() {
        this.document.newPage();
    }

    public void close() {
        this.document.close();
    }

    private void fillFonts() {
        this.fonts.put(FontFormat.Header1, new Font(Font.FontFamily.HELVETICA, 72f, Font.BOLDITALIC));
        this.fonts.put(FontFormat.Header2, new Font(Font.FontFamily.HELVETICA, 64f, Font.BOLDITALIC));
        this.fonts.put(FontFormat.Header3, new Font(Font.FontFamily.HELVETICA, 56f, Font.BOLD));
        this.fonts.put(FontFormat.Header4, new Font(Font.FontFamily.HELVETICA, 48f, Font.BOLD));
        this.fonts.put(FontFormat.Header5, new Font(Font.FontFamily.HELVETICA, 24f, Font.BOLD));
        this.fonts.put(FontFormat.Content, new Font(Font.FontFamily.HELVETICA, 24f, Font.NORMAL));
        this.fonts.put(FontFormat.ContentSmall, new Font(Font.FontFamily.HELVETICA, 20f, Font.NORMAL));
        this.fonts.put(FontFormat.ContentItalic, new Font(Font.FontFamily.HELVETICA, 24f, Font.ITALIC));
    }

    private void fillPositions() {
        this.positions.put(Position.Left, Paragraph.ALIGN_LEFT);
        this.positions.put(Position.Right, Paragraph.ALIGN_RIGHT);
        this.positions.put(Position.Center, Paragraph.ALIGN_CENTER);
        this.positions.put(Position.Top, PdfPCell.ALIGN_TOP);
        this.positions.put(Position.Bottom, PdfPCell.ALIGN_BOTTOM);
        this.positions.put(Position.Middle, PdfPCell.ALIGN_MIDDLE);
    }

    public enum FontFormat {
        Header1,
        Header2,
        Header3,
        Header4,
        Header5,
        Content,
        ContentSmall,
        ContentItalic
    }

    public enum Position {
        Left,
        Center,
        Right,
        Top,
        Middle,
        Bottom
    }

    private static class HeaderFooter extends PdfPageEventHelper {
        private int maxPage;
        private byte[] background, icon;

        HeaderFooter(int maxPage, byte[] bg, byte[] icon) {
            this.maxPage = maxPage;
            this.background = bg;
            this.icon = icon;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            Rectangle rect = writer.getBoxSize("art");
            ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(document.getPageNumber() + " / " + (this.maxPage != 0 ? this.maxPage : "")), rect.getRight(), rect.getBottom(), 0);


            try {
                if(this.background != null) {
                    this.addBackground(this.background, writer, document);
                }
                if(this.icon != null) {
                    this.addIcon(this.icon, writer);
                }
            } catch (Exception ignored) {}
        }

        private void addIcon(byte[] array, PdfWriter writer) throws Exception {
            Image image = Image.getInstance(array);
            image.setAbsolutePosition(10, 10);
            image.scaleAbsolute(32, 32);
            writer.getDirectContentUnder().addImage(image);
        }

        private void addBackground(byte[] array, PdfWriter writer, Document document) throws Exception {
            Rectangle rectangle = document.getPageSize();
            Image image = Image.getInstance(array);
            image.scaleAbsolute(rectangle.getWidth(), rectangle.getHeight());
            image.setAlignment(Image.UNDERLYING);
            image.setAbsolutePosition(0, 0);
            writer.getDirectContentUnder().addImage(image);
        }
    }
}
