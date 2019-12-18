package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.metopa.fungus.nodes.FExpressionNode;

@NodeField(name = "slot", type = FrameSlot.class)
public abstract class FReadLocalVariableNode extends FExpressionNode {
    protected abstract FrameSlot getSlot();

    //@Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return FrameUtil.getBooleanSafe(frame, getSlot());
    }

    @Specialization(/*replaces = {"readLong", "readBoolean"}*/)
    protected Object readObject(VirtualFrame frame) {
        if (!frame.isObject(getSlot())) {
            /*
             * The FrameSlotKind has been set to Object, so from now on all writes to
             * the local variable will be Object writes. However, now we are in a
             * frame that still has an old non-Object value. This is a slow-path
             * operation: we read the non-Object value, and write it immediately as an
             * Object value so that we do not hit this path again multiple times for
             * the same variable of the same frame.
             */
            CompilerDirectives.transferToInterpreter();
            Object result = frame.getValue(getSlot());
            frame.setObject(getSlot(), result);
            return result;
        }

        return FrameUtil.getObjectSafe(frame, getSlot());
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return frame.getFrameDescriptor().getFrameSlotKind(getSlot()) == FrameSlotKind.Boolean;
    }
}
