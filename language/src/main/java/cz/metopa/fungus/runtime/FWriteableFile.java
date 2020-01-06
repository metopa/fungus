package cz.metopa.fungus.runtime;

import java.io.*;

public class FWriteableFile extends FFile {
    private final BufferedWriter writer;
    private final boolean appendMode;

    public FWriteableFile(String filename, boolean appendMode) throws IOException {
        super(filename);
        this.writer = new BufferedWriter(new FileWriter(filename, appendMode));
        this.appendMode = appendMode;
    }

    public void writeString(String data) throws IOException { writer.write(data); }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public String toString() {
        return String.format("file(\"%s\", \"%s\"", filename, appendMode ? "wa" : "w");
    }
}
