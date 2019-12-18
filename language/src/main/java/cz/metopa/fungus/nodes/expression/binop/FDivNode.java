package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "/")
public abstract class FDivNode extends FExpressionNode {
    private final BranchProfile seenInt0 = BranchProfile.create();
    private final BranchProfile seenFloat0 = BranchProfile.create();

    @Specialization
    protected int intDiv(int lhs, int rhs) {
        if (rhs == 0) {
            seenInt0.enter();
            throw FException.runtimeError("division by zero", this);
        }

        return lhs / rhs;
    }

    @Specialization
    protected float floatDiv(float lhs, float rhs) {
        if (rhs == 0) {
            seenFloat0.enter();
            throw FException.runtimeError("division by zero", this);
        }

        return lhs / rhs;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
