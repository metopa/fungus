package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "||")
public class FOrNode extends FShortCircuitNode {

    public FOrNode(FExpressionNode lhs, FExpressionNode rhs) { super(lhs, rhs); }

    @Override
    protected boolean isEvaluateRight(boolean leftValue) {
        return !leftValue;
    }

    @Override
    protected boolean execute(boolean leftValue, boolean rightValue) {
        return leftValue || rightValue;
    }
}
