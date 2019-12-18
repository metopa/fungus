package cz.metopa.fungus;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.ContextPolicy;
import com.oracle.truffle.api.source.Source;
import cz.metopa.fungus.builtin.FFormatFunction;
import cz.metopa.fungus.builtin.FPrintFunction;
import cz.metopa.fungus.parser.FNodeFactory;
import cz.metopa.fungus.parser.FungusParser;
import cz.metopa.fungus.runtime.FContext;
import cz.metopa.fungus.runtime.FFunction;
import java.util.Map;

@TruffleLanguage.
Registration(id = FLanguage.LANGUAGE_ID, name = "Fungus", defaultMimeType = FLanguage.MIME_TYPE,
             characterMimeTypes = FLanguage.MIME_TYPE, contextPolicy = ContextPolicy.EXCLUSIVE,
             fileTypeDetectors = FFileDetector.class)
public final class FLanguage extends TruffleLanguage<FContext> {
    static final String LANGUAGE_ID = "fungus";
    static final String MIME_TYPE = "application/x-fungus";

    public FLanguage() {}

    @Override
    protected FContext createContext(Env env) {
        return new FContext(this, env);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        Source source = request.getSource();
        FContext ctx = getContextReference().get();
        FNodeFactory factory = new FNodeFactory(this, source);
        registerBuiltins(factory);
        Map<String, FFunction> functions = FungusParser.parseLanguage(this, source, factory);
        functions.forEach((name, func) -> ctx.registerFunction(func));

        FFunction main = functions.get("main");
        if (main == null) {
            throw FException.parsingError("missing main function");
        }

        return main.getCallTarget();
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return false;
    }

    private void registerBuiltins(FNodeFactory factory) {
        FPrintFunction.register(factory);
        FFormatFunction.register(factory);
    }
}
