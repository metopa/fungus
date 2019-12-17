package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "+")
public abstract class FSubNode extends FExpressionNode {
    @Specialization
    protected int intAdd(int lhs, int rhs) {
        return lhs - rhs;
    }

    @Specialization
    protected float floatAdd(float lhs, float rhs) {
        return lhs - rhs;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
