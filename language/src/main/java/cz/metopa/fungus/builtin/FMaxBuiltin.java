package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "max")
@NodeChild(value = "lhs", type = FExpressionNode.class)
@NodeChild(value = "rhs", type = FExpressionNode.class)
abstract public class FMaxBuiltin extends FBuiltinNode {
    @Specialization
    public int intMax(int a, int b) {
        return Integer.max(a, b);
    }

    @Specialization
    public float floatMax(float a, float b) {
        return Float.max(a, b);
    }

    @Fallback
    protected Object typeError(Object lhs, Object rhs) {
        throw FException.typeError(this, lhs, rhs);
    }
}
