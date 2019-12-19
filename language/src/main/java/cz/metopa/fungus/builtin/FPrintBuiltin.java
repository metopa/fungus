package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "print")
public class FPrintBuiltin extends FBuiltinNode {
    private final boolean lineEnd;
    private final boolean useDelimiter;
    @Children private final FExpressionNode[] children;

    public FPrintBuiltin(boolean lineEnd, boolean useDelimiter, FExpressionNode... children) {
        this.lineEnd = lineEnd;
        this.useDelimiter = useDelimiter;
        this.children = children;
    }

    public Object executeGeneric(VirtualFrame frame) {
        Object[] values = executeChildren(frame, children);

        for (int i = 0; i < children.length; i++) {
            if (useDelimiter && i > 0) {
                System.out.print(" ");
            }

            System.out.print(values[i]);
        }

        if (lineEnd) {
            System.out.println();
        }
        return FNull.SINGLETON;
    }
}