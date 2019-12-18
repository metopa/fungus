package cz.metopa.fungus.nodes.expression.constants;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "string")
public final class FStringConstantNode extends FExpressionNode {
    private final String value;

    public FStringConstantNode(String value) { this.value = value; }

    @Override
    public String executeGeneric(VirtualFrame frame) {
        return value;
    }
}
