package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FIndexable;

@NodeInfo(shortName = "len")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FLenBuiltin extends FBuiltinNode {
    @Specialization
    public int indexable(FIndexable arg) {
        return arg.size();
    }

    @Specialization
    public int string(String arg) {
        return arg.length();
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
