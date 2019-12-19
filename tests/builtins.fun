func main() {
    var start = now();

    assert(max(10, -10) == 10);
    assert(max(-10, 10) == 10);
    assert(max(10, 10.2) == 10.2);
    assert(max(20, 10.2) == 20.);

    assert(min(10, -10) == -10);
    assert(min(-10, 10) == -10);
    assert(min(10, 10.2) == 10.);
    assert(min(20, 10.2) == 10.2);

    assert(sqrt(4) == 2.);
    assert(sqrt(5.) > 2.236);
    assert(sqrt(5.) < 2.237);

    var stop = now();
    assert(stop > start);
    assert(stop - start < 100000); // 100ms

    for (var i = 0; i < 100; i = i + 1) {
        assert(rand() != rand());
    }

    assert(format("") == "");
    assert(format("str") == "str");
    assert(format("%s %d %f %s %s", "abc", 42, 5., true, null) == "abc 42 5.000000 true NULL");
}