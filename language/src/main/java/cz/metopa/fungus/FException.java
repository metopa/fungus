package cz.metopa.fungus;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.sl.runtime.SLContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FException extends RuntimeException implements TruffleException {
    private final Node location;
    private final boolean isInternal;

    @TruffleBoundary
    public FException(String message, Node location, boolean isInternal) {
        super(message);
        this.location = location;
        this.isInternal = isInternal;
    }

    @SuppressWarnings("sync-override")
    @Override
    public final Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public Node getLocation() {
        return location;
    }

    @Override
    public boolean isInternalError() {
        return isInternal;
    }

    @TruffleBoundary
    public static FException typeError(Node operation, Object... values) {
        StringBuilder result = new StringBuilder();
        result.append("Type error: operation ");

        if (operation != null) {
            NodeInfo nodeInfo = SLContext.lookupNodeInfo(operation.getClass());
            if (nodeInfo != null) {
                result.append("\"").append(nodeInfo.shortName()).append("\"");
            }
        }

        result.append("not defined for ");
        result.append(Arrays.stream(Arrays.copyOf(values, values.length)).
                map(Object::toString).collect(Collectors.joining(", ")));

        return new FException(result.toString(), operation, false);
    }

    @TruffleBoundary
    public static FException parsingError(String message) {
        return new FException("Parsing error: " + message, null, false);
    }

    @TruffleBoundary
    public static FException internalError(String message) {
        return new FException("Internal error: " + message, null, true);
    }
}
