package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FException;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FNull;
import cz.metopa.fungus.runtime.FReadableFile;
import java.io.IOException;

@NodeInfo(shortName = "read")
@NodeChild(value = "file", type = FExpressionNode.class)
abstract public class FReadFileBuiltin extends FBuiltinNode {

    @Specialization
    public Object readLine(FReadableFile reader) {
        try {
            return FNull.escapeNull(reader.readLine());
        } catch (IOException e) {
            return FNull.SINGLETON;
        }
    }

    @Fallback
    protected Object typeError(Object arg) {
        throw FException.typeError(this, arg);
    }
}
