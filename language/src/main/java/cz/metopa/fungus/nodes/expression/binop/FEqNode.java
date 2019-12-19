package cz.metopa.fungus.nodes.expression.binop;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FTypes;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "==")
public abstract class FEqNode extends FComparisonNode {
    @Specialization
    protected boolean boolCmp(boolean lhs, boolean rhs) {
        return lhs == rhs;
    }

    @Specialization
    protected boolean intCmp(int lhs, int rhs) {
        return lhs == rhs;
    }

    @Specialization
    protected boolean floatCmp(float lhs, float rhs) {
        return lhs == rhs;
    }

    @Specialization
    protected boolean stringCmp(String lhs, String rhs) {
        return lhs.equals(rhs);
    }

    @Specialization
    protected boolean nullCmpL(FNull lhs, Object rhs) {
        return FTypes.isFNull(rhs);
    }

    @Specialization
    protected boolean nullCmpR(Object lhs, FNull rhs) {
        return FTypes.isFNull(lhs);
    }

    @Fallback
    protected Object typeError(Object left, Object right) {
        throw FException.typeError(this, left, right);
    }
}
