func fib(n) {
    var a = 1;
    var b = 0;

    while (n > 1) {
        var c = a + b;
        b = a;
        a = c;
        n = n - 1;
    }

    return a;
}

func main() {
    assert(fib(0) == 1);
    assert(fib(1) == 1);
    assert(fib(10) == 55);
}