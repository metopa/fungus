package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "now")
public class FNowBuiltin extends FBuiltinNode {
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return (int)(System.nanoTime() / 1000);
    }
}
