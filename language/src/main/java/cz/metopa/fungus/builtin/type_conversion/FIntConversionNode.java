package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "int conversion")
abstract public class FIntConversionNode extends FTypeConversionNode {
    @Specialization
    public int fromBool(boolean b) {
        return b ? 1 : 0;
    }

    @Specialization
    public int fromInt(int i) {
        return i;
    }

    @Specialization
    public int fromFloat(float f) {
        return (int)f;
    }

    @Specialization
    public Object fromString(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            return FNull.SINGLETON;
        }
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
