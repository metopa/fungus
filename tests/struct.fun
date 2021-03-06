struct Foo {
    a, b
}

func main() {
    var f = Foo(1, 2);
    assert(1 == f.a);
    assert(f.b == 2);
    assert(typename(f) == "Foo");
    assert(string(f) == "Foo{a=1, b=2}");

    f.b = 3;
    assert(f.b == 3);
}