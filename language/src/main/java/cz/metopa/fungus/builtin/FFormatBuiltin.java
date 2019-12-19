package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import java.util.Formatter;
import java.util.IllegalFormatConversionException;

@NodeInfo(shortName = "format")
public class FFormatBuiltin extends FBuiltinNode {
    @Child private FExpressionNode formatNode;
    @Children private final FExpressionNode[] valueNodes;

    public FFormatBuiltin(FExpressionNode formatNode, FExpressionNode... valueNodes) {
        this.formatNode = formatNode;
        this.valueNodes = valueNodes;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object format = formatNode.executeGeneric(frame);

        if (!(format instanceof String)) {
            throw FException.parsingError("format() expects string as the first argument");
        }

        Object[] values = executeChildren(frame, valueNodes);

        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        try {
            fmt.format((String)format, values);
            return sb.toString();
        } catch (IllegalFormatConversionException ex) {
            throw FException.runtimeError(
                String.format("invalid format string \"%s\": %s", format, ex.getMessage()), this);
        }
    }
}
