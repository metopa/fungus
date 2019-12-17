package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

public class FReadArgumentNode extends FExpressionNode {
    private final int index;
    private final BranchProfile outOfBoundsTaken = BranchProfile.create();

    public FReadArgumentNode(int index) {
        this.index = index;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        if (index < args.length) {
            return args[index];
        } else {
            //outOfBoundsTaken.enter();
            throw FException.internalError("Invalid argument index: " + String.valueOf(index) + " of " + String.valueOf(args.length));
        }
    }
}
