func main() {
    var a[10] = "test";
    for (var i = 0; i < len(a); i = i + 1) {
        assert(a[i] == "test");
    }
}