package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.parser.FNodeFactory;
import cz.metopa.fungus.runtime.FNull;
import java.util.Objects;

public class FPrintFunction extends FExpressionNode {
    private final boolean lineEnd;
    private final boolean delimiter;

    private FPrintFunction(boolean lineEnd, boolean delimiter) {
        this.lineEnd = lineEnd;
        this.delimiter = delimiter;
    }

    public static void register(FNodeFactory factory) {
        factory.registerFunction("print", null, new FPrintFunction(false, false), null);
        factory.registerFunction("println", null, new FPrintFunction(true, false), null);
        factory.registerFunction("prints", null, new FPrintFunction(false, true), null);
        factory.registerFunction("printsln", null, new FPrintFunction(true, true), null);
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
