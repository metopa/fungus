package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import cz.metopa.fungus.FException;
import java.util.List;

public class FArray implements TruffleObject, FIndexable {
    private final Object[] data;

    private FArray(int size) {
        if (size <= 0) {
            throw FException.runtimeError("Array size must be positive", null);
        }
        this.data = new Object[size];
    }

    public static FArray allocHelper(List<Integer> shape, Object initVal) {
        FArray result = new FArray(shape.get(0));

        if (shape.size() > 1) {
            for (int i = 0; i < result.size(); i++) {
                result.set(i, allocHelper(shape.subList(1, shape.size()), initVal));
            }
        } else {
            for (int i = 0; i < result.size(); i++) {
                result.set(i, initVal);
            }
        }

        return result;
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public Object get(int index) {
        return data[index];
    }

    @Override
    public void set(int index, Object value) {
        data[index] = value;
    }
}
