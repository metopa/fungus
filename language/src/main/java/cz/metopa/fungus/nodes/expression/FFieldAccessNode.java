package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FIndexable;
import cz.metopa.fungus.runtime.FObject;

@NodeInfo(shortName = ".")
@NodeChild(value = "object", type = FExpressionNode.class)
abstract public class FFieldAccessNode extends FExpressionNode {
    final String fieldName;

    public FFieldAccessNode(String fieldName) { this.fieldName = fieldName; }

    @Specialization
    protected Object handleObject(FObject object) {
        return object.getField(fieldName);
    }

    @Fallback
    protected Object typeError(Object lhs) {
        throw FException.typeError(this, lhs);
    }
}
