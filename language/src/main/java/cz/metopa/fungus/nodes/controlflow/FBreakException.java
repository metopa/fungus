package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class FBreakException extends ControlFlowException {
    public static final ControlFlowException SINGLETON = new FBreakException();

    private FBreakException() {}
}
