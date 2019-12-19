func readNumber(prompt) {
    var s = "";
    var n = 0;
    while (true) {
        print(prompt);
        s = read();
        if (s == null) {
            return null;
        }
        n = int(s);
        if (n != null) {
            return n;
        }
    }
}

func main() {
    var a = readNumber("A> ");
    if (a == null) {
        return;
    }
    var b = readNumber("B> ");
    if (b == null) {
        return;
    }

    println(format("max(%d, %d) = %d", a, b, max(a, b)));
}