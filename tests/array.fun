func generate() {
    var a[200];
    assert(len(a) == 200);
    for (var i = 0; i < len(a); i = i + 1) {
        assert(a[i] == null);
        a[i] = -i;
        assert(a[i] == -i);
    }
    return a;
}

func main() {
    var a = generate();
    assert(len(a) == 200);
    assert(typename(a) == "array[200]");
    for (var i = 0; i < len(a); i = i + 1) {
        assert(typename(a[i]) == "int");
        assert(a[i] == -i);
    }
    var b[3];
    for (i = 0; i < len(b); i = i + 1) {
        b[i] = i * 2;
    }
    assert(string(b) == "[0, 2, 4]");
    assert(generate()[3] == -3);
}