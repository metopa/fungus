package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;


@TypeSystemReference(FTypes.class)
@NodeInfo(description = "The abstract base node for all expressions")
public abstract class FExpressionNode extends FStatementNode {
    public abstract Object executeGeneric(VirtualFrame frame);

    @Override
    public void executeVoid(VirtualFrame frame) {
        executeGeneric(frame);
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return FTypesGen.expectBoolean(executeGeneric(frame));
    }

    public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
        return FTypesGen.expectInteger(executeGeneric(frame));
    }

    public float executeFloat(VirtualFrame frame) throws UnexpectedResultException {
        return FTypesGen.expectFloat(executeGeneric(frame));
    }
}
