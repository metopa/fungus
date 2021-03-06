package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.type_conversion.*;
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
            switch (args.size()) {
            case 0:
                return new FReadBuiltin();
            case 1:
                return FReadFileBuiltinNodeGen.create(args.get(0));
            default:
                throw FException.parsingError(
                    String.format("read expects 0 or 1 arguments, %d provided", args.size()));
            }
        });
        factory.registerBuiltin("write", args -> {
            checkArgCountAtLeast(2, "write", args);
            return FWriteFileBuiltinNodeGen.create(args.get(0), args.get(1));
        });
        factory.registerBuiltin("open", args -> {
            checkArgCountAtLeast(2, "write", args);
            return FOpenFileBuiltinNodeGen.create(args.get(0), args.get(1));
        });
        factory.registerBuiltin("close", args -> {
            checkArgCountAtLeast(1, "write", args);
            return FCloseFileBuiltinNodeGen.create(args.get(0));
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

        factory.registerBuiltin("vec3", args -> {
            switch (args.size()) {
                case 0:
                    return new FVec3ZeroInitNode();
                case 1:
                    return FVec3SingularConversionNodeGen.create(args.get(0));
                case 3:
                    return FVec3ConversionNodeGen.create(args.get(0), args.get(1), args.get(2));
                default:
                    throw FException.parsingError(String.format(
                        "vec3 expects 0, 1 or 3 arguments, %d provided", args.size()));
            }
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

        factory.registerBuiltin("sin", args -> {
            checkArgCount(1, "sin", args);
            return FSinBuiltinNodeGen.create(args.get(0));
        });

        factory.registerBuiltin("cos", args -> {
            checkArgCount(1, "cos", args);
            return FCosBuiltinNodeGen.create(args.get(0));
        });

        factory.registerBuiltin("abs", args -> {
            checkArgCount(1, "abs", args);
            return FAbsBuiltinNodeGen.create(args.get(0));
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

        factory.registerBuiltin("substr", args -> {
            switch (args.size()) {
            case 2:
                return FSubstr2BuiltinNodeGen.create(args.get(0), args.get(1));
            case 3:
                return FSubstr3BuiltinNodeGen.create(args.get(0), args.get(1), args.get(2));
            default:
                throw FException.parsingError(
                    String.format("substr expects 2 or 3 arguments, %d provided", args.size()));
            }
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

    @ExplodeLoop
    protected static Object[] executeChildren(VirtualFrame frame, FExpressionNode[] nodes) {
        Object[] values = new Object[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            values[i] = nodes[i].executeGeneric(frame);
        }

        return values;
    }
}
