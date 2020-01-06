package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FIndexable;

@NodeInfo(shortName = "[]")
@NodeChild(value = "array", type = FExpressionNode.class)
@NodeChild(value = "index", type = FExpressionNode.class)
abstract public class FArrayAccessNode extends FExpressionNode {
    @Specialization
    protected Object handleIndexable(FIndexable array, int index) {
        index = FIndexable.adjustIndex(index, array.size(), false, this);
        return array.get(index);
    }

    @Specialization
    protected String handleString(String str, int index) {
        index = FIndexable.adjustIndex(index, str.length(), false, this);
        return str.substring(index, index + 1);
    }

    @Fallback
    protected Object typeError(Object lhs, Object index) {
        throw FException.typeError(this, lhs, index);
    }
}
