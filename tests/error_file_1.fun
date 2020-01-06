func main() {
    var f = open("/tmp/fungus-test.txt", "w");
    assert(f != null);
    read(f);
}