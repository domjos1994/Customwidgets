package de.domjos.customwidgets.io;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class TextWriter {
    private final ICSVWriter writer;

    public TextWriter(String path) throws Exception {
        this.writer = this.getWriter(path);
    }

    public void writeLine(String line) {
        this.writer.writeNext(new String[]{line});
    }

    public void writeLine(String[] line) {
        this.writer.writeNext(line);
    }

    public void closeWriter() throws Exception {
        this.writer.close();
    }

    private ICSVWriter getWriter(String path) throws Exception {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
        return new CSVWriterBuilder(outputStreamWriter).withParser(parser).build();
    }
}
