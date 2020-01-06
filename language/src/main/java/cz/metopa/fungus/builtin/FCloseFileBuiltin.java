package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FFile;
import cz.metopa.fungus.runtime.FNull;
import java.io.IOException;

@NodeInfo(shortName = "close")
@NodeChild(value = "file", type = FExpressionNode.class)
abstract public class FCloseFileBuiltin extends FBuiltinNode {

    @Specialization
    public Object closeFile(FFile file) {
        try {
            file.close();
            return FNull.SINGLETON;
        } catch (IOException e) {
            throw FException.runtimeError("Error closing file: " + e.getMessage(), this);
        }
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
