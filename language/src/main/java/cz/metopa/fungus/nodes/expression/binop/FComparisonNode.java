package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "comparison base")
public abstract class FComparisonNode extends FExpressionNode {}
