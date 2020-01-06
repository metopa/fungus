func main() {
    var f = open("/tmp/fungus-test.txt", "w");
    assert(f != null);
    write(f, "test");
    close(f);

    var f = open("/tmp/fungus-test.txt", "r");
    assert(f != null);
    write(f, "test");
}