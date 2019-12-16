package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.sl.SLLanguage;
import cz.metopa.fungus.FLanguage;


@NodeInfo(language = "Fungus", description = "The root of all Fungus execution trees")
public class FRootNode extends RootNode {
    @Child
    private FExpressionNode bodyNode;

    private final String name;

    public FRootNode(FLanguage language, FrameDescriptor frameDescriptor, FExpressionNode bodyNode, String name) {
        super(language, frameDescriptor);
        this.bodyNode = bodyNode;
        this.name = name;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        assert lookupContextReference(SLLanguage.class).get() != null;
        return bodyNode.executeGeneric(frame);
    }

    public FExpressionNode getBodyNode() {
        return bodyNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "root:" + name;
    }
}
