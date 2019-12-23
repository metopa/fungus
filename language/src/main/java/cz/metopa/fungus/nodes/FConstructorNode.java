package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.BranchProfile;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FObject;
import java.util.List;

public class FConstructorNode extends FExpressionNode {
    private final String typeName;
    private final List<String> fieldNames;
    private final BranchProfile outOfBoundsTaken = BranchProfile.create();

    public FConstructorNode(String typeName, List<String> fieldNames) {
        this.typeName = typeName;
        this.fieldNames = fieldNames;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object[] args = frame.getArguments();
        if (fieldNames.size() != args.length) {
            outOfBoundsTaken.enter();
            throw FException.internalError("Invalid constructor argument count: " + args.length +
                                           " of " + fieldNames.size());
        }

        FObject object = new FObject(typeName, fieldNames);
        for (int i = 0; i < args.length; i++) {
            object.setField(fieldNames.get(i), args[i]);
        }

        return object;
    }
}
