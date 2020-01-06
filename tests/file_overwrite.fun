func main() {
    var time = now();
    var cookie = int(rand() * 100);

    var f = open("/tmp/fungus-test.txt", "w");
    assert(f != null);
    write(f, format("%d\n", time));
    close(f);

    f = open("/tmp/fungus-test.txt", "w");
    assert(f != null);
    write(f, string(cookie));
    close(f);

    f = open("/tmp/fungus-test.txt", "r");
    assert(f != null);
    var s = read(f);
    assert(int(s) == cookie);
    s = read(f);
    assert(s == null);
    close(f);
}