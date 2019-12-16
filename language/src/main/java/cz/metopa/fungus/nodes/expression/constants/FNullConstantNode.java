package cz.metopa.fungus.nodes.expression.constants;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "null")
public final class FNullConstantNode extends FExpressionNode {
    @Override
    public FNull executeGeneric(VirtualFrame frame) {
        return FNull.SINGLETON;
    }
}
