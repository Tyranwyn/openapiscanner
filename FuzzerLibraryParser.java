package org.zaproxy.zap.extension.openapiscanner;

import java.io.*;
import java.util.ArrayList;

public class FuzzerLibraryParser {
    private String dir;
    private File libraryFile;
    private ArrayList<String> list = new ArrayList<>();

    public FuzzerLibraryParser(String dir) {
        this.dir = dir;
        this.libraryFile = new File(dir);
        parse();
    }

    public ArrayList<String> getList() {
        return list;
    }

    private void parse() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.libraryFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith(">>>>"))
                    this.list.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
