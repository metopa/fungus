package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "abs")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FAbsBuiltin extends FBuiltinNode {
    @Specialization
    public float abs(float arg) {
        return Math.abs(arg);
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
