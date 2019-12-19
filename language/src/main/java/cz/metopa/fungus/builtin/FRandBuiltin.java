package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "rand")
public class FRandBuiltin extends FBuiltinNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return Math.random();
    }
}
