package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

public abstract class FShortCircuitNode extends FExpressionNode {
    @Child private FExpressionNode lhs;
    @Child private FExpressionNode rhs;

    public FShortCircuitNode(FExpressionNode lhs, FExpressionNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public final Object executeGeneric(VirtualFrame frame) {
        return executeBoolean(frame);
    }

    @Override
    public final boolean executeBoolean(VirtualFrame frame) {
        boolean leftValue;
        try {
            leftValue = lhs.executeBoolean(frame);
        } catch (UnexpectedResultException e) {
            throw FException.typeError(this, e.getResult(), null);
        }
        boolean rightValue;
        try {
            if (isEvaluateRight(leftValue)) { // TODO profile branch
                rightValue = rhs.executeBoolean(frame);
            } else {
                rightValue = false;
            }
        } catch (UnexpectedResultException e) {
            throw SLException.typeError(this, leftValue, e.getResult());
        }
        return execute(leftValue, rightValue);
    }

    protected abstract boolean isEvaluateRight(boolean leftValue);

    protected abstract boolean execute(boolean leftValue, boolean rightValue);
}
