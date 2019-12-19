package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.nodes.ControlFlowException;

public class FContinueException extends ControlFlowException {
    public static final ControlFlowException SINGLETON = new FContinueException();

    private FContinueException() {}
}
