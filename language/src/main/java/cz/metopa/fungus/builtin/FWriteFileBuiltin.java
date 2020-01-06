package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FNull;
import cz.metopa.fungus.runtime.FWriteableFile;
import java.io.IOException;

@NodeInfo(shortName = "write")
@NodeChild(value = "file", type = FExpressionNode.class)
@NodeChild(value = "str", type = FExpressionNode.class)
abstract public class FWriteFileBuiltin extends FBuiltinNode {

    @Specialization
    public FNull writeString(FWriteableFile file, String str) {
        try {
            file.writeString(str);
            return FNull.SINGLETON;
        } catch (IOException e) {
            throw FException.runtimeError("Error writing data to file: " + e.getMessage(), this);
        }
    }

    @Fallback
    protected Object typeError(Object arg, Object data) {
        throw FException.typeError(this, arg, data);
    }
}
