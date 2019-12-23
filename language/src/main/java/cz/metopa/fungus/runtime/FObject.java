package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.interop.TruffleObject;
import cz.metopa.fungus.FException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FObject implements TruffleObject {
    private String typeName;
    private Map<String, Object> fields;

    public FObject(String typeName, List<String> fieldNames) {
        this.typeName = typeName;
        fields = new HashMap<>();

        for (String s : fieldNames) {
            fields.put(s, FNull.SINGLETON);
        }
    }

    public Object getField(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) {
            throw FException.runtimeError(
                String.format("object of type '%s' has no field '%s'", typeName, fieldName), null);
        }
        return value;
    }

    public void setField(String fieldName, Object value) {
        if (!fields.containsKey(fieldName)) {
            throw FException.runtimeError(
                String.format("object of type '%s' has no field '%s'", typeName, fieldName), null);
        }
        fields.put(fieldName, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName).append("{");

        fields.forEach((n, v) -> { sb.append(n).append("=").append(v).append(", "); });
        if (!fields.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }

    public String getTypeName() { return typeName; }
}
