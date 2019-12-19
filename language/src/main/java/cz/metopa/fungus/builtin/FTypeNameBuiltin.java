package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FNull;

@NodeInfo(shortName = "typename")
@NodeChild(value = "arg", type = FExpressionNode.class)
abstract public class FTypeNameBuiltin extends FBuiltinNode {
    @Specialization
    public String boolType(boolean arg) {
        return "bool";
    }

    @Specialization
    public String intType(int arg) {
        return "int";
    }

    @Specialization
    public String floatType(float arg) {
        return "float";
    }

    @Specialization
    public String boolType(String arg) {
        return "string";
    }

    @Specialization
    public String boolType(FNull arg) {
        return "null";
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
