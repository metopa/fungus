package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.nodes.expression.constants.FNullConstantNode;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "return")
public final class FReturnNode extends FStatementNode {
    @Child private FExpressionNode returnValue;

    public FReturnNode(FExpressionNode returnValue) {
        this.returnValue = returnValue != null ? returnValue : new FNullConstantNode();
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        throw new FReturnException(returnValue.executeGeneric(frame));
    }
}
