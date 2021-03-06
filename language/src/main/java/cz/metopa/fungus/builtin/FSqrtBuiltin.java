package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "sqrt")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FSqrtBuiltin extends FBuiltinNode {
    @Specialization
    public float sqrt(float arg) {
        return (float)Math.sqrt(arg);
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
