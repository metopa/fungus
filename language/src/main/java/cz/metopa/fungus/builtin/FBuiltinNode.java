package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.type_conversion.FBoolConversionNodeGen;
import cz.metopa.fungus.builtin.type_conversion.FFloatConversionNodeGen;
import cz.metopa.fungus.builtin.type_conversion.FIntConversionNodeGen;
import cz.metopa.fungus.builtin.type_conversion.FStringConversionNodeGen;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.parser.FNodeFactory;
import java.util.List;

abstract public class FBuiltinNode extends FExpressionNode {
    public static void register(FNodeFactory factory) {
        // =========== IO builtins ===========
        factory.registerBuiltin(
            "print", args -> new FPrintBuiltin(false, false, args.toArray(new FExpressionNode[0])));
        factory.registerBuiltin(
            "prints", args -> new FPrintBuiltin(false, true, args.toArray(new FExpressionNode[0])));
        factory.registerBuiltin(
            "println",
            args -> new FPrintBuiltin(true, false, args.toArray(new FExpressionNode[0])));
        factory.registerBuiltin(
            "printsln",
            args -> new FPrintBuiltin(true, true, args.toArray(new FExpressionNode[0])));
        factory.registerBuiltin("format", args -> {
            checkArgCountAtLeast(1, "format", args);
            return new FFormatBuiltin(args.get(0),
                                      args.subList(1, args.size()).toArray(new FExpressionNode[0]));
        });
        factory.registerBuiltin("read", args -> {
            checkArgCount(0, "read", args);
            return new FReadBuiltin();
        });

        // =========== Type conversion builtins ===========
        factory.registerBuiltin("bool", args -> {
            checkArgCountAtLeast(1, "bool", args);
            return FBoolConversionNodeGen.create(args.get(0));
        });
        factory.registerBuiltin("int", args -> {
            checkArgCountAtLeast(1, "int", args);
            return FIntConversionNodeGen.create(args.get(0));
        });
        factory.registerBuiltin("float", args -> {
            checkArgCountAtLeast(1, "float", args);
            return FFloatConversionNodeGen.create(args.get(0));
        });
        factory.registerBuiltin("string", args -> {
            checkArgCountAtLeast(1, "string", args);
            return FStringConversionNodeGen.create(args.get(0));
        });

        // =========== Misc builtins ===========
        factory.registerBuiltin("max", args -> {
            checkArgCount(2, "max", args);
            return FMaxBuiltinNodeGen.create(args.get(0), args.get(1));
        });
        factory.registerBuiltin("min", args -> {
            checkArgCount(2, "min", args);
            return FMinBuiltinNodeGen.create(args.get(0), args.get(1));
        });

        factory.registerBuiltin("sqrt", args -> {
            checkArgCount(1, "sqrt", args);
            return FSqrtBuiltinNodeGen.create(args.get(0));
        });

        factory.registerBuiltin("rand", args -> {
            checkArgCount(0, "rand", args);
            return new FRandBuiltin();
        });

        factory.registerBuiltin("now", args -> {
            checkArgCount(0, "now", args);
            return new FNowBuiltin();
        });

        factory.registerBuiltin("typename", args -> {
            checkArgCount(1, "typename", args);
            return FTypeNameBuiltinNodeGen.create(args.get(0));
        });

        factory.registerBuiltin("len", args -> {
            checkArgCount(1, "len", args);
            return FLenBuiltinNodeGen.create(args.get(0));
        });
    }

    private static void checkArgCount(int expected, String name, List<FExpressionNode> args) {
        if (args.size() != expected) {
            throw FException.parsingError(
                String.format("%s expects %d arguments, %d provided", name, expected, args.size()));
        }
    }

    private static void checkArgCountAtLeast(int expected, String name,
                                             List<FExpressionNode> args) {
        if (args.size() < expected) {
            throw FException.parsingError(String.format(
                "%s expects at least %d arguments, %d provided", name, expected, args.size()));
        }
    }

    protected static Object[] executeChildren(VirtualFrame frame, FExpressionNode[] nodes) {
        Object[] values = new Object[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            values[i] = nodes[i].executeGeneric(frame);
        }

        return values;
    }
}
