func main() {
    var s = "abcde";
    assert(substr(s, 0) == "abcde");
    assert(substr(s, 1) == "bcde");
    assert(substr(s, 5) == "");
    assert(substr(s, -1) == "e");
    assert(substr(s, 0, 2) == "ab");
    assert(substr(s, 0, -1) == "abcd");
    assert(substr(s, 0, -2) == "abc");
    assert(substr(s, -3, -1) == "cd");
    assert(substr(s, 0, 0) == "");
    assert(substr(s, 5, 5) == "");
}