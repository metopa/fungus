package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "&&")
public abstract class FAndNode extends FExpressionNode {
    @Specialization
    protected boolean boolAnd(boolean lhs, boolean rhs) {
        return lhs && rhs;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
