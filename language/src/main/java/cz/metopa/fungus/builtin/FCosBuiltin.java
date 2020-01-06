package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "cos")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FCosBuiltin extends FBuiltinNode {
    @Specialization
    public float sqrt(float arg) {
        return (float)Math.cos(arg);
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
