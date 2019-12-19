package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FReadArgumentNode;
import cz.metopa.fungus.parser.FNodeFactory;

@NodeChild("argument")
@NodeInfo(shortName = "type conversion")
abstract public class FTypeConversionNode extends FExpressionNode {
    public static void register(FNodeFactory factory) {
        factory.registerFunction("bool", 1, FBoolConversionNodeGen.create(new FReadArgumentNode(0)),
                                 null);
        factory.registerFunction("int", 1, FIntConversionNodeGen.create(new FReadArgumentNode(0)),
                                 null);
        factory.registerFunction("float", 1,
                                 FFloatConversionNodeGen.create(new FReadArgumentNode(0)), null);
        factory.registerFunction("string", 1,
                                 FStringConversionNodeGen.create(new FReadArgumentNode(0)), null);
    }
}
