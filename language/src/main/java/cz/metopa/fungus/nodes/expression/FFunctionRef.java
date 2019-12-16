package cz.metopa.fungus.nodes.expression;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.metopa.fungus.FLanguage;
import cz.metopa.fungus.nodes.FExpressionNode;
import cz.metopa.fungus.runtime.FContext;
import cz.metopa.fungus.runtime.FFunction;

@NodeInfo(shortName = "func")
public final class FFunctionRef extends FExpressionNode {
    private final String functionName;
    private final ContextReference<FContext> reference;
    @CompilationFinal
    private FFunction cachedFunction;

    public FFunctionRef(FLanguage language, String functionName) {
        this.functionName = functionName;
        this.reference = language.getContextReference();
    }

    @Override
    public FFunction executeGeneric(VirtualFrame frame) {
        if (cachedFunction == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedFunction = reference.get().lookupFunction(functionName);
        }

        return cachedFunction;
    }

}
