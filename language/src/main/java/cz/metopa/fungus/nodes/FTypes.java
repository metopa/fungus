package cz.metopa.fungus.nodes;

import com.oracle.truffle.api.dsl.TypeCast;
import com.oracle.truffle.api.dsl.TypeCheck;
import com.oracle.truffle.api.dsl.TypeSystem;
import cz.metopa.fungus.runtime.FNull;

@TypeSystem({boolean.class, int.class, float.class})
public abstract class FTypes {
    @TypeCheck(FNull.class)
    public static boolean isFNull(Object value) {
        return value == FNull.SINGLETON;
    }

    @TypeCast(FNull.class)
    public static FNull asFNull(Object value) {
        assert isFNull(value);
        return FNull.SINGLETON;
    }
}
