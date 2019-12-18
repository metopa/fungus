package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(language = "Fungus", description = "The abstract base node for all Fungus statements")
@ReportPolymorphism
public abstract class FStatementNode extends Node {
    private static final int NO_SOURCE = -1;

    public abstract void executeVoid(VirtualFrame frame);

    @Override
    public String toString() {
        return this.getClass().getCanonicalName();
    }

    public final void setSourceSection(int charIndex, int length) {
        // Ignored
    }

    public final void setUnavailableSourceSection() {
        // Ignored
    }

    public int getStartIndex() { return NO_SOURCE; }

    public int getStopIndex() { return NO_SOURCE; }
}
