func main() {
    var a[10][20] = 3.14;

    for (var i = 0; i < len(a); i = i + 1) {
        for (var j = 0; j < len(a[i]); j = j + 1) {
            assert(a[i][j] == 3.14);
        }
    }
}