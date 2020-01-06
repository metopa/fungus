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
abstract public class FSubstr2Builtin extends FBuiltinNode {
    @Specialization
    public String substr(String s, int beginIndex) {
        beginIndex = FIndexable.adjustIndex(beginIndex, s.length(), true, this);
        return s.substring(beginIndex);
    }

    @Fallback
    protected Object typeError(Object str, Object begin) {
        throw FException.typeError(this, str, begin);
    }
}
