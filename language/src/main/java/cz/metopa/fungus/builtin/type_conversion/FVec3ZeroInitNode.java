package cz.metopa.fungus.builtin.type_conversion;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.builtin.FBuiltinNode;
import cz.metopa.fungus.runtime.FVec3;

@NodeInfo(shortName = "vec3")
public class FVec3ZeroInitNode extends FBuiltinNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return new FVec3(0, 0, 0);
    }
}
