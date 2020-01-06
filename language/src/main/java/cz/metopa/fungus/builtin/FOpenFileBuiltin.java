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

@NodeInfo(shortName = "open")
@NodeChild(value = "filename", type = FExpressionNode.class)
@NodeChild(value = "mode", type = FExpressionNode.class)
abstract public class FOpenFileBuiltin extends FBuiltinNode {

    @Specialization
    public Object openFile(String filename, String mode) {
        try {
            return FFile.create(filename, mode);
        } catch (IOException e) {
            return FNull.SINGLETON;
        }
    }

    @Fallback
    protected Object typeError(Object filename, Object mode) {
        throw FException.typeError(this, filename, mode);
    }
}
