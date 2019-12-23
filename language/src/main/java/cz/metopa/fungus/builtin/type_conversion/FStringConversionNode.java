package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FArray;
import cz.metopa.fungus.runtime.FIndexable;
import cz.metopa.fungus.runtime.FObject;

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
    public String fromString(String s) {
        return s;
    }

    @Specialization
    public String fromArray(FIndexable array) {
        return array.toString();
    }

    @Specialization
    public String fromObject(FObject object) {
        return object.toString();
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
