package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FStatementNode;

@NodeInfo(shortName = "continue")
public final class FContinueNode extends FStatementNode {
    @Override
    public void executeVoid(VirtualFrame frame) {
        throw FContinueException.SINGLETON;
    }
}
