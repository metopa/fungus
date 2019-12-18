package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("lhs")
@NodeChild("rhs")
@NodeInfo(shortName = "*")
public abstract class FMultiplicationNode extends FExpressionNode {
    @Specialization
    protected int intMultiplication(int lhs, int rhs) {
        return lhs * rhs;
    }

    @Specialization
    protected float floatMultiplication(float lhs, float rhs) {
        return lhs * rhs;
    }

    @Specialization
    protected String stringMultiplication(String lhs, int rhs) {
        if (rhs == 0) {
            return "";
        }

        if (rhs < 0) {
            lhs = new StringBuffer(lhs).reverse().toString();
            rhs = -rhs;
        }

        if (rhs == 1) {
            return lhs;
        }

        StringBuilder builder = new StringBuilder();

        while (rhs-- > 0) {
            builder.append(lhs);
        }

        return builder.toString();
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
