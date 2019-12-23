struct Foo {
    a, b
}

func main() {
    var a[3] = int(1);
    var f = Foo(1, a);
    var b[2] = f;
    assert(len(b) == 2);
    assert(b[0].b[1] == 1);
    b[0].b[1] = "test";
    assert(b[1].b[1] == "test");
    assert(b[1].b[0] == 1);
}