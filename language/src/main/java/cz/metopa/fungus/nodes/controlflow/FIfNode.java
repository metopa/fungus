package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.type_conversion.FBoolConversionNodeGen;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;

@NodeInfo(shortName = "if")
public final class FIfNode extends FStatementNode {
    @Child private FExpressionNode conditionNode;
    @Child private FStatementNode thenPartNode;
    @Child private FStatementNode elsePartNode;

    public FIfNode(FExpressionNode conditionNode, FStatementNode thenPartNode,
                   FStatementNode elsePartNode) {
        this.conditionNode = FBoolConversionNodeGen.create(conditionNode);
        this.thenPartNode = thenPartNode;
        this.elsePartNode = elsePartNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        try {
            if (conditionNode.executeBoolean(frame)) {
                thenPartNode.executeVoid(frame);
            } else {
                if (elsePartNode != null) {
                    elsePartNode.executeVoid(frame);
                }
            }
        } catch (UnexpectedResultException e) {
            throw FException.runtimeError("error converting if condition value to boolean", this);
        }
    }
}
