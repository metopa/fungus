package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeChild("valueNode")
@NodeField(name = "slot", type = FrameSlot.class)
public abstract class FWriteLocalVariableNode extends FExpressionNode {
    protected abstract FrameSlot getSlot();

    //    @Specialization(guards = "isBooleanOrIllegal(frame)")
    //    protected boolean writeBoolean(VirtualFrame frame, boolean value) {
    //        frame.getFrameDescriptor().setFrameSlotKind(getSlot(), FrameSlotKind.Boolean);
    //        frame.setBoolean(getSlot(), value);
    //        return value;
    //    }
    //
    //    @Specialization(guards = "isIntOrIllegal(frame)")
    //    protected int writeInt(VirtualFrame frame, int value) {
    //        frame.getFrameDescriptor().setFrameSlotKind(getSlot(), FrameSlotKind.Int);
    //        frame.setInt(getSlot(), value);
    //        return value;
    //    }
    //
    //    @Specialization(guards = "isFloatOrIllegal(frame)")
    //    protected float writeFloat(VirtualFrame frame, float value) {
    //        frame.getFrameDescriptor().setFrameSlotKind(getSlot(), FrameSlotKind.Float);
    //        frame.setFloat(getSlot(), value);
    //        return value;
    //    }

    @Specialization(/*replaces = {"writeBoolean", "writeInt", "writeFloat"}*/)
    protected Object write(VirtualFrame frame, Object value) {
        frame.getFrameDescriptor().setFrameSlotKind(getSlot(), FrameSlotKind.Object);
        frame.setObject(getSlot(), value);
        return value;
    }

    protected boolean isIntOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getFrameSlotKind(getSlot());
        return kind == FrameSlotKind.Int || kind == FrameSlotKind.Illegal;
    }

    protected boolean isBooleanOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getFrameSlotKind(getSlot());
        return kind == FrameSlotKind.Boolean || kind == FrameSlotKind.Illegal;
    }

    protected boolean isFloatOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getFrameSlotKind(getSlot());
        return kind == FrameSlotKind.Float || kind == FrameSlotKind.Illegal;
    }
}
