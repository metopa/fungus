package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "float conversion")
abstract public class FFloatConversionNode extends FTypeConversionNode {
    @Specialization
    public float fromBool(boolean b) {
        return b ? 1 : 0;
    }

    @Specialization
    public float fromInt(int i) {
        return i;
    }

    @Specialization
    public float fromFloat(float f) {
        return f;
    }

    @Specialization
    public Object fromString(String s) {
        try {
            return Float.valueOf(s);
        } catch (NumberFormatException ex) {
            return FNull.SINGLETON;
        }
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
