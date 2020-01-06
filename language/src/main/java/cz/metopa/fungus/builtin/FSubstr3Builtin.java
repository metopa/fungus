package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FIndexable;

@NodeInfo(shortName = "substr")
@NodeChild(value = "s", type = FExpressionNode.class)
@NodeChild(value = "beginIndex", type = FExpressionNode.class)
@NodeChild(value = "endIndex", type = FExpressionNode.class)
abstract public class FSubstr3Builtin extends FBuiltinNode {
    @Specialization
    public String substr(String s, int beginIndex, int endIndex) {
        //if (beginIndex)
        beginIndex = FIndexable.adjustIndex(beginIndex, s.length(), true, this);
        endIndex = FIndexable.adjustIndex(endIndex, s.length(), true, this);
        return s.substring(beginIndex, endIndex);
    }

    @Fallback
    protected Object typeError(Object str, Object begin, Object end) {
        throw FException.typeError(this, str, begin, end);
    }
}
