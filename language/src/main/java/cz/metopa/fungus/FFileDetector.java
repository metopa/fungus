package cz.metopa.fungus;

import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.sl.SLLanguage;
import java.nio.charset.Charset;

public final class FFileDetector implements TruffleFile.FileTypeDetector {
    @Override
    public String findMimeType(TruffleFile file) {
        String name = file.getName();
        if (name != null && name.endsWith(".fun")) {
            return SLLanguage.MIME_TYPE;
        }
        return null;
    }

    @Override
    public Charset findEncoding(TruffleFile file) {
        return null;
    }
}
