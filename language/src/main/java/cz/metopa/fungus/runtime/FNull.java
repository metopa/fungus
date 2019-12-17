package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;

public final class FNull implements TruffleObject {
    public static final FNull SINGLETON = new FNull();

    private FNull() {}

    @Override
    public String toString() {
        return "NULL";
    }
}
