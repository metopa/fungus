package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.parser.FNodeFactory;
import java.util.Arrays;
import java.util.Formatter;
import java.util.IllegalFormatConversionException;

@NodeInfo(shortName = "format")
public class FFormatBuiltin extends FExpressionNode {

    private FFormatBuiltin() {}

    public static void register(FNodeFactory factory) {
        factory.registerFunction("format", null, new FFormatBuiltin(), null);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        if (args.length < 1 || !(args[0] instanceof String)) {
            throw FException.parsingError("format() expects string as the first argument");
        }
        String formatString = (String)args[0];

        args = Arrays.copyOfRange(args, 1, args.length);
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        try {
            fmt.format(formatString, args);
            return sb.toString();
        } catch (IllegalFormatConversionException ex) {
            throw FException.runtimeError(
                "invalid format string \"" + formatString + "\": " + ex.getMessage(), this);
        }
    }
}
