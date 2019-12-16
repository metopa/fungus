package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.builtins.SLPrintlnBuiltin;
import com.oracle.truffle.sl.builtins.SLReadlnBuiltin;
import com.oracle.truffle.sl.runtime.SLFunctionRegistry;
import cz.metopa.fungus.FLanguage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public final class FContext {
    private final Env env;
    private final BufferedReader input;
    private final PrintWriter output;
    private final FLanguage language;
    private final Map<String, FFunction> functionRegistry;

    public FContext(FLanguage language, Env env) {
        this.env = env;
        this.input = new BufferedReader(new InputStreamReader(env.in()));
        this.output = new PrintWriter(env.out(), true);
        this.language = language;
        this.functionRegistry = new HashMap<>();
        installBuiltins();
    }

    /**
     * Return the current Truffle environment.
     */
    public Env getEnv() {
        return env;
    }

    /**
     * Returns the default input, i.e., the source for the {@link SLReadlnBuiltin}. To allow unit
     * testing, we do not use {@link System#in} directly.
     */
    public BufferedReader getInput() {
        return input;
    }

    /**
     * The default default, i.e., the output for the {@link SLPrintlnBuiltin}. To allow unit
     * testing, we do not use {@link System#out} directly.
     */
    public PrintWriter getOutput() {
        return output;
    }

    /**
     * Adds all builtin functions to the {@link SLFunctionRegistry}. This method lists all
     * {@link SLBuiltinNode builtin implementation classes}.
     */
    private void installBuiltins() {
    }


    public FFunction lookupFunction(String name) {
        FFunction func = functionRegistry.get(name);
        if (func == null) {
            throw new RuntimeException("Unknown function: " + name);
        }
        return func;
    }

    public void registerFunction(FFunction func) {
        String name = func.getName();
        if (functionRegistry.containsKey(name)) {
            throw new RuntimeException("Function redefinition error: " + name);
        }
        functionRegistry.put(name, func);
    }
}
