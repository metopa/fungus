package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.type_conversion.FBoolConversionNodeGen;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.nodes.expression.constants.FBooleanConstantNode;

@NodeInfo(shortName = "for")
public final class FForNode extends FStatementNode {
    @Child private FStatementNode prologueNode;
    @Child private FExpressionNode conditionNode;
    @Child private FStatementNode iterUpdateNode;
    @Child private FStatementNode bodyNode;
    // TODO Use RepeatingNode
    public FForNode(FStatementNode prologueNode, FExpressionNode conditionNode,
                    FStatementNode iterUpdateNode, FStatementNode bodyNode) {
        this.prologueNode = prologueNode;
        this.conditionNode = conditionNode != null ? FBoolConversionNodeGen.create(conditionNode)
                                                   : new FBooleanConstantNode(true);
        this.iterUpdateNode = iterUpdateNode;
        this.bodyNode = bodyNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        if (prologueNode != null) {
            prologueNode.executeVoid(frame);
        }

        while (true) {
            try {
                if (!conditionNode.executeBoolean(frame)) {
                    break;
                }
            } catch (UnexpectedResultException e) {
                throw FException.runtimeError("error converting for condition value to boolean",
                                              this);
            }

            try {
                bodyNode.executeVoid(frame);
            } catch (FBreakException ex) {
                break;
            } catch (FContinueException ex) { // do nothing
            }

            if (iterUpdateNode != null) {
                iterUpdateNode.executeVoid(frame);
            }
        }
    }
}
