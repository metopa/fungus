package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.nodes.expression.FFunctionRef;
import cz.metopa.fungus.runtime.FFunction;

import java.util.Arrays;
import java.util.stream.Collectors;

@NodeInfo(shortName = "invoke")
public final class FInvokeNode extends FExpressionNode {
    @Child
    private FFunctionRef functionNode;
    @Children
    private final FExpressionNode[] argumentNodes;

    public FInvokeNode(FFunctionRef functionNode, FExpressionNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        FFunction function = functionNode.executeGeneric(frame);

        CompilerAsserts.compilationConstant(argumentNodes.length);
        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }

        System.out.println(function.getName() + "(" + Arrays.stream(Arrays.copyOf(argumentValues,
                argumentValues.length)).
                map(Object::toString).collect(Collectors.joining(", ")) + ")");

        Integer parameterCount = function.getParameterCount();
        if (parameterCount != null) {
            if (parameterCount != argumentValues.length) {
                throw FException.parsingError(function.getName() + " expects " + parameterCount.toString() +
                        " parameters, provided " + String.valueOf(argumentValues.length));
            }
        }

        return function.getCallTarget().call(argumentValues);
    }
}
