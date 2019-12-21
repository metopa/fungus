func main() {
    var a[200];
    for (var i = 0; i < len(a); i = i + 1) {
        a[i] = i;
    }

    assert(a[0] == 0);
    assert(a[-1] == 199);
    assert(a[-2] == 198);
    assert(a[-200] == 0);
}