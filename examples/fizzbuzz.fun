func fizz_buzz(i) {
    var printed = false;
    if (i % 3 == 0) {
        print("fizz");
        printed = true;
    }
    if (i % 5 == 0) {
        print("buzz");
        printed = true;
    }
    if (!printed) {
        print(i);
    }
    print("\n");
}

func main() {
    for (var i = 1; i <= 30; i = i + 1) {
        fizz_buzz(i);
    }
}