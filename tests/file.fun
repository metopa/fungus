func main() {
    var time = now();
    var cookie = int(rand() * 100);

    var f = open("/tmp/fungus-test.txt", "w");
    assert(f != null);
    assert(typename(f) == "file");
    assert(string(f) == "file(\"/tmp/fungus-test.txt\", \"w\"");
    write(f, format("%d\n", time));
    write(f, string(cookie));
    close(f);

    f = open("/tmp/fungus-test.txt", "r");
    assert(f != null);
    assert(typename(f) == "file");
    assert(string(f) == "file(\"/tmp/fungus-test.txt\", \"r\"");
    var s = read(f);
    assert(int(s) == time);
    s = read(f);
    assert(int(s) == cookie);
    s = read(f);
    assert(s == null);
    close(f);
}