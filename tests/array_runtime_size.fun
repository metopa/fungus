func size() {
    return int("100");
}

func main() {
    var a[size()][size() + 1] = 3.14;
    assert(len(a) == 100);
    assert(len(a[0]) == 101);
}