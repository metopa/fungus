package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;

@NodeInfo(shortName = "string conversion")
abstract public class FStringConversionNode extends FTypeConversionNode {
    @Specialization
    public String fromBool(boolean b) {
        return String.valueOf(b);
    }

    @Specialization
    public String fromInt(int i) {
        return String.valueOf(i);
    }

    @Specialization
    public String fromFloat(float f) {
        return String.valueOf(f);
    }

    @Specialization
    public Object fromString(String s) {
        return s;
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
