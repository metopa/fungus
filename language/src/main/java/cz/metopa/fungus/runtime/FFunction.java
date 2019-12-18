package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.interop.TruffleObject;

public final class FFunction implements TruffleObject {
    private final String name;
    private final Integer parameterCount;
    private RootCallTarget callTarget;

    public FFunction(String name, RootCallTarget callTarget, Integer parameterCount) {
        this.name = name;
        this.callTarget = callTarget;
        this.parameterCount = parameterCount;
    }

    public String getName() { return name; }

    /// If null, callTarget checks parameter count by itself.
    public Integer getParameterCount() { return parameterCount; }

    public RootCallTarget getCallTarget() { return callTarget; }

    @Override
    public String toString() {
        return name;
    }
}
