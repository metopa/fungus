struct Node {
  value, next
}

func prepend(value, tail) {
    return Node(value, tail);
}

func sum(node) {
    var r = 0;
    while (node != null) {
        r = r + node.value;
        node = node.next;
    }
    return r;
}

func main() {
    var list = null;
    list = prepend(2, list);
    list = prepend(4, list);
    list = prepend(6, list);
    assert(sum(list) == 12);
    assert(sum(list) == 12);
    assert(sum(list.next) == 6);
}