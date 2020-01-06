func main() {
    assert(open("/tmp/5/4/3/2/1/nofile", "r") == null);
    assert(open("/tmp/5/4/3/2/1/nofile", "w") == null);
}