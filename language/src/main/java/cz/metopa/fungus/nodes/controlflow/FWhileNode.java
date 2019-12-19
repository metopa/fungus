package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.type_conversion.FBoolConversionNodeGen;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;

@NodeInfo(shortName = "while")
public final class FWhileNode extends FStatementNode {
    @Child private FExpressionNode conditionNode;
    @Child private FStatementNode bodyNode;

    public FWhileNode(FExpressionNode conditionNode, FStatementNode bodyNode) {
        this.conditionNode = FBoolConversionNodeGen.create(conditionNode);
        this.bodyNode = bodyNode;
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        while (true) {
            try {
                if (!conditionNode.executeBoolean(frame)) {
                    break;
                }
            } catch (UnexpectedResultException e) {
                throw FException.runtimeError("error converting while condition value to boolean",
                                              this);
            }

            try {
                bodyNode.executeVoid(frame);
            } catch (FBreakException ex) {
                break;
            } catch (FContinueException ex) { // do nothing
            }
        }
    }
}
