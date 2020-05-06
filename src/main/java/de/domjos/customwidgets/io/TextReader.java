package de.domjos.customwidgets.io;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TextReader {
    private CSVReader reader;

    public TextReader(String path) throws Exception {
        this.reader = this.getReader(path);
    }

    public List<String> getHeader() throws Exception {
        List<String[]> lines = this.reader.readAll();
        this.reader.close();

        if(lines != null) {
            if (lines.size() != 0) {
                return Arrays.asList(lines.get(0));
            }
        }
        return new LinkedList<>();
    }

    public List<Map<String, String>> readFile() throws Exception {
        List<Map<String, String>> items = new LinkedList<>();

        List<String[]> lines = this.reader.readAll();

        if(lines != null) {
            if(lines.size() != 0) {
                String[] header = lines.get(0);

                for(int row = 1; row <= lines.size()-1; row++) {
                    Map<String, String> mpRow = new LinkedHashMap<>();
                    for(int column = 0; column <= lines.get(row).length - 1; column++) {
                        mpRow.put(header[column], lines.get(row)[column]);
                    }
                    items.add(mpRow);
                }
            }
        }
        this.reader.close();
        return items;
    }

    private CSVReader getReader(String path) throws Exception {
        CSVParser parser = new CSVParserBuilder().withSeparator(this.getSplitter(path)).build();
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
        return new CSVReaderBuilder(inputStreamReader).withCSVParser(parser).build();
    }

    private char getSplitter(String path) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String line = bufferedReader.readLine();

        char splitter = '\t';
        if(line.contains(";")) {
            splitter = ';';
        } else if(line.contains(",")) {
            splitter = ',';
        }
        bufferedReader.close();
        return splitter;
    }
}
