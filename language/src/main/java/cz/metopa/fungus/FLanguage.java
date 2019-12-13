package cz.metopa.fungus;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Scope;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.ContextPolicy;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.sl.nodes.local.SLLexicalScope;
import com.oracle.truffle.sl.builtins.SLBuiltinNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLFunction;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@TruffleLanguage.Registration(
    id = FLanguage.LANGUAGE_ID,
    name = "Fungus",
    defaultMimeType = FLanguage.MIME_TYPE,
    characterMimeTypes = FLanguage.MIME_TYPE,
    contextPolicy = ContextPolicy.SHARED,
    fileTypeDetectors = FFileDetector.class)
@ProvidedTags({
    StandardTags.CallTag.class,
    StandardTags.StatementTag.class,
    StandardTags.RootTag.class,
    StandardTags.RootBodyTag.class,
    StandardTags.ExpressionTag.class,
    DebuggerTags.AlwaysHalt.class})
public final class FLanguage extends TruffleLanguage<SLContext> {
  static final String LANGUAGE_ID = "fungus";
  static final String MIME_TYPE = "application/x-fungus";

  public FLanguage() {
  }

  @Override
  protected SLContext createContext(Env env) {
    return null; // new SLContext(this, env, new ArrayList<>(EXTERNAL_BUILTINS));
  }

  @Override
  protected CallTarget parse(ParsingRequest request) throws Exception {
    Source source = request.getSource();
    Map<String, RootCallTarget> functions;
    /*
     * Parse the provided source. At this point, we do not have an FContext yet.
     * Registration of the functions with the FContext happens lazily in FEvalRootNode.
     */
    if (request.getArgumentNames().isEmpty()) {
      //functions = FungusParser.parseSource(this, source);
    } else {
      Source requestedSource = request.getSource();
      StringBuilder sb = new StringBuilder();
      sb.append("function main(");
      String sep = "";
      for (String argumentName : request.getArgumentNames()) {
        sb.append(sep);
        sb.append(argumentName);
        sep = ",";
      }
      sb.append(") { return ");
      sb.append(request.getSource().getCharacters());
      sb.append(";}");
      String language = requestedSource.getLanguage() == null ? LANGUAGE_ID : requestedSource.getLanguage();
      Source decoratedSource = Source.newBuilder(language, sb.toString(), request.getSource().getName()).build();
      //functions = FungusParser.parseSource(this, decoratedSource);
    }
    return null;
  }

  @Override
  protected boolean isVisible(SLContext context, Object value) {
    return !InteropLibrary.getFactory().getUncached(value).isNull(value);
  }

  @Override
  protected boolean isObjectOfLanguage(Object object) {
    if (!(object instanceof TruffleObject)) {
      return false;
    } else if (object instanceof SLBigNumber || object instanceof SLFunction || object instanceof SLNull) {
      return true;
    } else return SLContext.isSLObject(object);
  }

  @Override
  protected String toString(SLContext context, Object value) {
    return toString(value);
  }

  public static String toString(Object value) {
    try {
      if (value == null) {
        return "ANY";
      }
      InteropLibrary interop = InteropLibrary.getFactory().getUncached(value);
      if (interop.fitsInLong(value)) {
        return Long.toString(interop.asLong(value));
      } else if (interop.isBoolean(value)) {
        return Boolean.toString(interop.asBoolean(value));
      } else if (interop.isString(value)) {
        return interop.asString(value);
      } else if (interop.isNull(value)) {
        return "NULL";
      } else if (interop.isExecutable(value)) {
        if (value instanceof SLFunction) {
          return ((SLFunction) value).getName();
        } else {
          return "Function";
        }
      } else if (interop.hasMembers(value)) {
        return "Object";
      } else if (value instanceof SLBigNumber) {
        return value.toString();
      } else {
        return "Unsupported";
      }
    } catch (UnsupportedMessageException e) {
      CompilerDirectives.transferToInterpreter();
      throw new AssertionError();
    }
  }

  @Override
  protected Object findMetaObject(SLContext context, Object value) {
    return getMetaObject(value);
  }

  public static String getMetaObject(Object value) {
    if (value == null) {
      return "ANY";
    }
    InteropLibrary interop = InteropLibrary.getFactory().getUncached(value);
    if (interop.isNumber(value) || value instanceof SLBigNumber) {
      return "Number";
    } else if (interop.isBoolean(value)) {
      return "Boolean";
    } else if (interop.isString(value)) {
      return "String";
    } else if (interop.isNull(value)) {
      return "NULL";
    } else if (interop.isExecutable(value)) {
      return "Function";
    } else if (interop.hasMembers(value)) {
      return "Object";
    } else {
      return "Unsupported";
    }
  }

  @Override
  protected SourceSection findSourceLocation(SLContext context, Object value) {
    if (value instanceof SLFunction) {
      return ((SLFunction) value).getDeclaredLocation();
    }
    return null;
  }

  @Override
  public Iterable<Scope> findLocalScopes(SLContext context, Node node, Frame frame) {
    final SLLexicalScope scope = SLLexicalScope.createScope(node);
    return new Iterable<Scope>() {
      @Override
      public Iterator<Scope> iterator() {
        return new Iterator<Scope>() {
          private SLLexicalScope previousScope;
          private SLLexicalScope nextScope = scope;

          @Override
          public boolean hasNext() {
            if (nextScope == null) {
              nextScope = previousScope.findParent();
            }
            return nextScope != null;
          }

          @Override
          public Scope next() {
            if (!hasNext()) {
              throw new NoSuchElementException();
            }
            Scope vscope = Scope.newBuilder(nextScope.getName(), nextScope.getVariables(frame)).node(nextScope.getNode()).arguments(nextScope.getArguments(frame)).build();
            previousScope = nextScope;
            nextScope = null;
            return vscope;
          }
        };
      }
    };
  }

  @Override
  protected Iterable<Scope> findTopScopes(SLContext context) {
    return context.getTopScopes();
  }

  public static SLContext getCurrentContext() {
    return getCurrentContext(FLanguage.class);
  }

  private static final List<NodeFactory<? extends SLBuiltinNode>> EXTERNAL_BUILTINS = Collections.synchronizedList(new ArrayList<>());

  public static void installBuiltin(NodeFactory<? extends SLBuiltinNode> builtin) {
    EXTERNAL_BUILTINS.add(builtin);
  }

}
