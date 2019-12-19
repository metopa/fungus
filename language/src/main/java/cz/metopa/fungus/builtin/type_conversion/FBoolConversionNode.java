package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;

@NodeInfo(shortName = "bool conversion")
abstract public class FBoolConversionNode extends FTypeConversionNode {
    @Specialization
    public boolean fromBool(boolean b) {
        return b;
    }

    @Specialization
    public boolean fromInt(int i) {
        return i != 0;
    }

    @Specialization
    public boolean fromFloat(float f) {
        return f != 0;
    }

    @Specialization
    public Boolean fromString(String s) {
        return !s.isEmpty();
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
