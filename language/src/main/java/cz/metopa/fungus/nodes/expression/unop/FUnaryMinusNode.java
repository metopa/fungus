package cz.metopa.fungus.nodes.expression.unop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("rhs")
@NodeInfo(shortName = "-")
public abstract class FUnaryMinusNode extends FExpressionNode {
    @Specialization
    protected int minusInt(int rhs) {
        return -rhs;
    }

    @Specialization
    protected float minusFloat(float rhs) {
        return -rhs;
    }

    @Fallback
    protected Object typeError(Object rhs) {
        throw FException.typeError(this, rhs);
    }
}
