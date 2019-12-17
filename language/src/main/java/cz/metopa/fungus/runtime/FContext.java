package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.TruffleLanguage.Env;
import cz.metopa.fungus.FException;
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

    public Env getEnv() {
        return env;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    private void installBuiltins() {}

    public FFunction lookupFunction(String name) {
        FFunction func = functionRegistry.get(name);
        if (func == null) {
            throw FException.parsingError("unknown function " + name);
        }
        return func;
    }

    public void registerFunction(FFunction func) {
        String name = func.getName();
        if (functionRegistry.containsKey(name)) {
            throw FException.internalError("function redefinition during registration: " + name);
        }
        functionRegistry.put(name, func);
    }
}
