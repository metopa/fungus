package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.runtime.FObject;

@NodeInfo(shortName = "[]")
@NodeChild(value = "object", type = FExpressionNode.class)
@NodeChild(value = "value", type = FExpressionNode.class)
abstract public class FFieldWriteNode extends FStatementNode {
    private final String fieldName;

    public FFieldWriteNode(String fieldName) { this.fieldName = fieldName; }

    @Specialization
    protected void handleIndexable(FObject object, Object value) {
        object.setField(fieldName, value);
    }

    @Fallback
    protected void typeError(Object lhs, Object value) {
        throw FException.typeError(this, lhs, value);
    }
}
