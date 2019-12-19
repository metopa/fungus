package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FStatementNode;

@NodeInfo(shortName = "break")
public final class FBreakNode extends FStatementNode {
    @Override
    public void executeVoid(VirtualFrame frame) {
        throw FBreakException.SINGLETON;
    }
}
