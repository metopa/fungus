package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import cz.metopa.fungus.FException;
import java.io.File;
import java.io.IOException;

public abstract class FFile implements TruffleObject {
    protected final File file;
    protected final String filename;

    protected FFile(String filename) {
        this.filename = filename;
        this.file = new File(filename);
    }

    public static FFile create(String filename, String mode) throws IOException {
        switch (mode) {
        case "r":
            return new FReadableFile(filename);
        case "w":
            return new FWriteableFile(filename, false);
        case "wa":
            return new FWriteableFile(filename, true);
        default:
            throw FException.runtimeError("Unsupported file opening mode: " + mode, null);
        }
    }

    public abstract void close() throws IOException;
}
