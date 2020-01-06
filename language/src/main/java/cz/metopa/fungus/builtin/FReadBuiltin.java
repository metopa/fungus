package cz.metopa.fungus.builtin;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.runtime.FNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@NodeInfo(shortName = "read")
public class FReadBuiltin extends FBuiltinNode {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        try {
            return FNull.escapeNull(reader.readLine());
        } catch (IOException e) {
            return FNull.SINGLETON;
        }
    }
}
