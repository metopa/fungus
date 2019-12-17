package cz.metopa.fungus.nodes.expression.constants;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "float")
public final class FFloatConstantNode extends FExpressionNode {
    private final float value;

    public FFloatConstantNode(float value) {
        this.value = value;
    }

    public FFloatConstantNode(String str) {
        this.value = Float.parseFloat(str);
    }

    @Override
    public Float executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public float executeFloat(VirtualFrame frame) throws UnexpectedResultException {
        return value;
    }
}
