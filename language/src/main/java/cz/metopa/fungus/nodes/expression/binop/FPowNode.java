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
@NodeInfo(shortName = "power")
public abstract class FPowNode extends FExpressionNode {
    @Specialization()
    protected float floatPow(float lhs, float rhs) {
        return (float)Math.pow(lhs, rhs);
    }

    @Specialization()
    protected FVec3 vecPow(FVec3 lhs, float rhs) {
        return new FVec3((float)Math.pow(lhs.x(), rhs), (float)Math.pow(lhs.y(), rhs),
                         (float)Math.pow(lhs.z(), rhs));
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
