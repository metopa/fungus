package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeInfo(shortName = "assert")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FAssertBuiltin extends FBuiltinNode {
    private final String source;

    public FAssertBuiltin(String source) { this.source = source; }

    @Specialization
    public boolean checkResult(boolean arg) {
        if (arg) {
            // TODO: profile
            return true;
        } else {
            throw FException.assertError(this, source);
        }
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
