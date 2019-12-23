package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import cz.metopa.fungus.FException;

import java.util.Arrays;
import java.util.List;

public class FVec3 implements TruffleObject, FIndexable {
    private final float[] data;

    public FVec3(float x, float y, float z) {
        this.data = new float[3];
        data[0] = x;
        data[1] = y;
        data[2] = z;
    }

    public float x() { return data[0]; }

    public float y() { return data[1]; }

    public float z() { return data[2]; }

    public float getFloat(int i) { return data[i]; }

    public void setFloat(int i, float value) { data[i] = value; }

    public Float get(int i) { return getFloat(i); }

    @Override
    public void set(int index, Object value) {
        if (value instanceof Number) {
            setFloat(index, ((Number)value).floatValue());
        }
        throw FException.typeError(null, "vec3 only allows float values");
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public String toString() {
        return "vec3" + Arrays.toString(data);
    }
}
