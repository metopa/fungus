package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.*;

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
    public String stringType(String arg) {
        return "string";
    }

    @Specialization
    public String nullType(FNull arg) {
        return "null";
    }

    @Specialization
    public String arrayType(FArray arg) {
        return String.format("array[%d]", arg.size());
    }

    @Specialization
    public String vecType(FVec3 arg) {
        return "vec3";
    }

    @Specialization
    public String objType(FObject arg) {
        return arg.getTypeName();
    }

    @Specialization
    public String fileType(FFile arg) {
        return "file";
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
