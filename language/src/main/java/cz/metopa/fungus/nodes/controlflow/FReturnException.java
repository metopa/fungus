package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public final class FReturnException extends ControlFlowException {
    private final Object result;

    public FReturnException(Object result) { this.result = result; }

    public Object getResult() { return result; }
}
