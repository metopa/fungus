func fail() {
    assert(false);
    return false;
}

func main() {
    var foo = false || fail();
}