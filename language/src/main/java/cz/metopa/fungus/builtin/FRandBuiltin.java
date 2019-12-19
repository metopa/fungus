package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "rand")
public class FRandBuiltin extends FBuiltinNode {
    // TODO: float specialization
    @Override
    public Float executeGeneric(VirtualFrame frame) {
        return (float)Math.random();
    }
}
