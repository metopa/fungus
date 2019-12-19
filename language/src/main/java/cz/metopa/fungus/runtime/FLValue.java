package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;

abstract public class FLValue implements TruffleObject {
    abstract public Object writeValue(Object value);
}
