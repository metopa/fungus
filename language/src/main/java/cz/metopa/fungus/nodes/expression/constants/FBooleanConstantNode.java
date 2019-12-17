package cz.metopa.fungus.nodes.expression.constants;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "bool")
public final class FBooleanConstantNode extends FExpressionNode {
    private final boolean value;

    public FBooleanConstantNode(boolean value) {
        this.value = value;
    }

    public FBooleanConstantNode(String str) {
        this.value = Boolean.parseBoolean(str);
    }

    @Override
    public Boolean executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public boolean executeBoolean(VirtualFrame frame) {
        return value;
    }
}
