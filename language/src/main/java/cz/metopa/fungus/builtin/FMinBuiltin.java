package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "min")
@NodeChild(value = "lhs", type = FExpressionNode.class)
@NodeChild(value = "rhs", type = FExpressionNode.class)
abstract public class FMinBuiltin extends FBuiltinNode {
    @Specialization
    public int intMin(int a, int b) {
        return Integer.min(a, b);
    }

    @Specialization
    public float floatMin(float a, float b) {
        return Float.min(a, b);
    }

    @Fallback
    protected Object typeError(Object lhs, Object rhs) {
        throw FException.typeError(this, lhs, rhs);
    }
}
