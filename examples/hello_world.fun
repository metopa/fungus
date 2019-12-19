func fib() {
    var a = 1;
    var b = 1;

    for (var i = 0; i < 10; i = i + 1) {
        var c = a;
        a = a + b;
        b = c;
        if (a > 50) {
            continue;
        }
        while (true) {
            if (c < 1) {
                break;
            }
            printsln(c, a, b, i);
            c = c / 2;
        }
        println();
    }
    printsln(c, a, b, i);
}

func max(a, b) {
    if (a > b) {
        return a;
    } else {
        if (b != "aa") {
            return b;
        }
    }
}

func main() {
    printsln(max(9, 10), max(10, 10), max(11, 10));
}