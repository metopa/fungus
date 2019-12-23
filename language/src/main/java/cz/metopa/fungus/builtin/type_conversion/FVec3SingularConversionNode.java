package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FNull;
import cz.metopa.fungus.runtime.FVec3;

@NodeInfo(shortName = "vec3 conversion")
abstract public class FVec3SingularConversionNode extends FTypeConversionNode {
    @Specialization
    public FVec3 fromFloat(float f) {
        return new FVec3(f, f, f);
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
