package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;

@NodeInfo(shortName = ">")
public abstract class FGreaterNode extends FComparisonNode {
    @Specialization
    protected boolean intCmp(int lhs, int rhs) {
        return lhs > rhs;
    }

    @Specialization
    protected boolean floatCmp(float lhs, float rhs) {
        return lhs > rhs;
    }

    @Specialization
    protected boolean stringCmp(String lhs, String rhs) {
        return lhs.compareTo(rhs) > 0;
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
