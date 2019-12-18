package cz.metopa.fungus.nodes.controlflow;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.nodes.FStatementNode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NodeInfo(shortName = "block", description = "The node implementing a source code block")
public final class FBlockNode extends FStatementNode {
    @Children private final FStatementNode[] bodyNodes;

    public FBlockNode(FStatementNode[] bodyNodes) { this.bodyNodes = bodyNodes; }

    @Override
    @ExplodeLoop
    public void executeVoid(VirtualFrame frame) {
        CompilerAsserts.compilationConstant(bodyNodes.length);

        for (FStatementNode statement : bodyNodes) {
            statement.executeVoid(frame);
        }
    }

    public List<FStatementNode> getStatements() {
        return Collections.unmodifiableList(Arrays.asList(bodyNodes));
    }
}
