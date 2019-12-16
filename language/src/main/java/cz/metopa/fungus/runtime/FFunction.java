package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.TruffleObject;

public final class FFunction implements TruffleObject {
    private final String name;

    private RootCallTarget callTarget;

    public FFunction(String name, RootCallTarget callTarget) {
        this.name = name;
        this.callTarget = callTarget;
    }

    public String getName() {
        return name;
    }

    public RootCallTarget getCallTarget() {
        return callTarget;
    }

    @Override
    public String toString() {
        return name;
    }

}
