package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.builtin.FBuiltinNode;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild(value = "argument", type = FExpressionNode.class)
@NodeInfo(shortName = "type conversion")
abstract public class FTypeConversionNode extends FBuiltinNode {}
