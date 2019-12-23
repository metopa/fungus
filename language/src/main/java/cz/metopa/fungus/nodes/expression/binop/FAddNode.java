package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FVec3;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "+")
public abstract class FAddNode extends FExpressionNode {
    @Specialization
    protected int intAdd(int lhs, int rhs) {
        return lhs + rhs;
    }

    @Specialization
    protected float floatAdd(float lhs, float rhs) {
        return lhs + rhs;
    }

    @Specialization
    protected String stringAdd(String lhs, String rhs) {
        return lhs + rhs;
    }

    @Specialization
    protected FVec3 vecVecAdd(FVec3 lhs, FVec3 rhs) {
        return new FVec3(lhs.x() + rhs.x(), lhs.y() + rhs.y(), lhs.z() + rhs.z());
    }

    @Specialization
    protected FVec3 vecFloatAdd(FVec3 lhs, float rhs) {
        return new FVec3(lhs.x() + rhs, lhs.y() + rhs, lhs.z() + rhs);
    }

    @Specialization
    protected FVec3 floatVecAdd(float lhs, FVec3 rhs) {
        return new FVec3(lhs + rhs.x(), lhs + rhs.y(), lhs + rhs.z());
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
