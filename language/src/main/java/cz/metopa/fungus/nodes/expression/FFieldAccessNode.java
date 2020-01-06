package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FIndexable;
import cz.metopa.fungus.runtime.FObject;
import cz.metopa.fungus.runtime.FVec3;

@NodeInfo(shortName = ".")
@NodeChild(value = "object", type = FExpressionNode.class)
abstract public class FFieldAccessNode extends FExpressionNode {
    final String fieldName;

    public FFieldAccessNode(String fieldName) { this.fieldName = fieldName; }

    @Specialization
    protected Object handleObject(FObject object) {
        return object.getField(fieldName);
    }

    @Specialization
    protected float handleVector(FVec3 vec) {
        switch (fieldName) {
            case "x":
                return vec.x();
            case"y":
                return vec.y();
            case "z":
                return vec.z();
            default:
                throw FException.runtimeError(
                        String.format("vec3 has no field '%s'", fieldName), null);

        }
    }

    @Fallback
    protected Object typeError(Object lhs) {
        throw FException.typeError(this, lhs);
    }
}
