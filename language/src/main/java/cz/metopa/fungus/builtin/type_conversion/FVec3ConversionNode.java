package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.builtin.FBuiltinNode;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FVec3;

@NodeInfo(shortName = "vec3 conversion")
@NodeChild(value = "x", type = FExpressionNode.class)
@NodeChild(value = "y", type = FExpressionNode.class)
@NodeChild(value = "z", type = FExpressionNode.class)
abstract public class FVec3ConversionNode extends FBuiltinNode {
    @Specialization
    public FVec3 fromFloat(float x, float y, float z) {
        return new FVec3(x, y, z);
    }

    @Fallback
    protected Object typeError(Object x, Object y, Object z) {
        throw FException.typeError(this, x, y, z);
    }
}
