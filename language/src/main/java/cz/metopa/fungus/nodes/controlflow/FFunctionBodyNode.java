package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.FStatementNode;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "body")
public final class FFunctionBodyNode extends FExpressionNode {
    @Child private FStatementNode bodyNode;

    private final BranchProfile exceptionTaken = BranchProfile.create();
    private final BranchProfile nullTaken = BranchProfile.create();

    public FFunctionBodyNode(FStatementNode bodyNode) { this.bodyNode = bodyNode; }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            bodyNode.executeVoid(frame);
        } catch (FReturnException ex) {
            exceptionTaken.enter();
            return ex.getResult();
        }

        nullTaken.enter();
        return FNull.SINGLETON;
    }
}
