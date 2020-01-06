package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.runtime.FObject;
import cz.metopa.fungus.runtime.FVec3;

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

    @Specialization
    protected void handleVector(FVec3 vec, float value) {
        switch (fieldName) {
            case "x":
                vec.setFloat(0, value);
                break;
            case"y":
                vec.setFloat(1, value);
                break;
            case "z":
                vec.setFloat(2, value);
                break;
            default:
                throw FException.runtimeError(
                        String.format("vec3 has no field '%s'", fieldName), null);

        }
    }

    @Fallback
    protected void typeError(Object lhs, Object value) {
        throw FException.typeError(this, lhs, value);
    }
}
