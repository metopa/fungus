package cz.metopa.fungus.runtime;

import com.oracle.truffle.api.nodes.Node;
import cz.metopa.fungus.FException;

public interface FIndexable {
    int size();

    Object get(int index);

    void set(int index, Object value);

    static int adjustIndex(int index, int maxIndex, Node node) {
        if (index < 0) {
            index += maxIndex;
        }
        if (index < 0 || index >= maxIndex) {
            throw FException.runtimeError(
                String.format("out of bounds access: index %d of %d", index, maxIndex), node);
        }
        return index;
    }
}
