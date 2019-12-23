package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FVec3;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "%")
public abstract class FModNode extends FExpressionNode {
    private final BranchProfile seenInt0 = BranchProfile.create();
    private final BranchProfile seenFloat0 = BranchProfile.create();

    @Specialization
    protected int intMod(int lhs, int rhs) {
        if (rhs == 0) {
            seenInt0.enter();
            throw FException.runtimeError("division by zero", this);
        }

        return lhs % rhs;
    }

    @Specialization
    protected float floatMod(float lhs, float rhs) {
        if (rhs == 0) {
            seenFloat0.enter();
            throw FException.runtimeError("division by zero", this);
        }

        return lhs % rhs;
    }

    @Specialization
    protected FVec3 vecVecMod(FVec3 lhs, FVec3 rhs) {
        if (rhs.x() == 0 || rhs.y() == 0 || rhs.z() == 0) {
            seenFloat0.enter();
            throw FException.runtimeError("division by zero", this);
        }
        return new FVec3(lhs.x() % rhs.x(), lhs.y() % rhs.y(), lhs.z() % rhs.z());
    }

    @Specialization
    protected FVec3 vecFloatMod(FVec3 lhs, float rhs) {
        if (rhs == 0) {
            seenFloat0.enter();
            throw FException.runtimeError("division by zero", this);
        }
        return new FVec3(lhs.x() % rhs, lhs.y() % rhs, lhs.z() % rhs);
    }

    @Specialization
    protected FVec3 floatVecMod(float lhs, FVec3 rhs) {
        if (rhs.x() == 0 || rhs.y() == 0 || rhs.z() == 0) {
            seenFloat0.enter();
            throw FException.runtimeError("division by zero", this);
        }
        return new FVec3(lhs % rhs.x(), lhs % rhs.y(), lhs % rhs.z());
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
