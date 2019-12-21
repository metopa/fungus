package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.runtime.FArray;
import java.util.ArrayList;
import java.util.List;

public class FAllocArrayNode extends FExpressionNode {
    @Children private FExpressionNode[] shape;
    @Child private FExpressionNode initialValue;

    public FAllocArrayNode(List<FExpressionNode> shape, FExpressionNode initialValue) {
        assert (!shape.isEmpty());
        this.shape = shape.toArray(new FExpressionNode[0]);
        this.initialValue = initialValue;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        List<Integer> shapeValues = new ArrayList<>(shape.length);

        for (int i = 0; i < shape.length; i++) {
            try {
                shapeValues.add(shape[i].executeInt(frame));
            } catch (UnexpectedResultException e) {
                throw FException.runtimeError("Array dimension values must be integers", this);
            }
        }

        return FArray.allocHelper(shapeValues, initialValue.executeGeneric(frame));
    }
}
