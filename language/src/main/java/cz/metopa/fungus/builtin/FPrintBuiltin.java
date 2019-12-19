package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.parser.FNodeFactory;
import cz.metopa.fungus.runtime.FNull;
import java.util.Objects;

@NodeInfo(shortName = "print")
public class FPrintBuiltin extends FExpressionNode {
    private final boolean lineEnd;
    private final boolean delimiter;

    private FPrintBuiltin(boolean lineEnd, boolean delimiter) {
        this.lineEnd = lineEnd;
        this.delimiter = delimiter;
    }

    public static void register(FNodeFactory factory) {
        factory.registerFunction("print", null, new FPrintBuiltin(false, false), null);
        factory.registerFunction("println", null, new FPrintBuiltin(true, false), null);
        factory.registerFunction("prints", null, new FPrintBuiltin(false, true), null);
        factory.registerFunction("printsln", null, new FPrintBuiltin(true, true), null);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        for (int i = 0; i < args.length; i++) {
            if (delimiter && i > 0) {
                System.out.print(" ");
            }

            System.out.print(Objects.toString(args[i]));
        }

        if (lineEnd) {
            System.out.println();
        }
        return FNull.SINGLETON;
    }
}
