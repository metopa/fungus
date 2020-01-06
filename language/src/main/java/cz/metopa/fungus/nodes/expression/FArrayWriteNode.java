package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.runtime.FIndexable;

@NodeInfo(shortName = "[]")
@NodeChild(value = "array", type = FExpressionNode.class)
@NodeChild(value = "index", type = FExpressionNode.class)
@NodeChild(value = "value", type = FExpressionNode.class)
abstract public class FArrayWriteNode extends FStatementNode {
    @Specialization
    protected void handleIndexable(FIndexable array, int index, Object value) {
        index = FIndexable.adjustIndex(index, array.size(), false, this);
        array.set(index, value);
    }

    @Fallback
    protected void typeError(Object lhs, Object index, Object value) {
        throw FException.typeError(this, lhs, index, value);
    }
}
