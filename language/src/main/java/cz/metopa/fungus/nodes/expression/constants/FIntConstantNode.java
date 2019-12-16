package cz.metopa.fungus.nodes.expression.constants;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "int")
public final class FIntConstantNode extends FExpressionNode {
    private final int value;

    public FIntConstantNode(int value) {
        this.value = value;
    }

    public FIntConstantNode(String str) {
        this.value = Integer.parseInt(str);
    }

    @Override
    public Integer executeGeneric(VirtualFrame frame) {
        return value;
    }

    @Override
    public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
        return value;
    }
}
