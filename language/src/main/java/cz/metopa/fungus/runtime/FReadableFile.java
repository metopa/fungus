package cz.metopa.fungus.runtime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FReadableFile extends FFile {
    private final BufferedReader reader;

    public FReadableFile(String filename) throws FileNotFoundException {
        super(filename);
        reader = new BufferedReader(new FileReader(file));
    }

    public String readLine() throws IOException { return reader.readLine(); }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public String toString() {
        return String.format("file(\"%s\", \"r\"", filename);
    }
}
