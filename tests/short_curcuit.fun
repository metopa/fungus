func fail() {
    assert(false);
    return false;
}

func main() {
    assert((true || fail()) == true);
    assert((false && fail()) == false);

    assert((true && (false && fail())) == false);
    assert((false || (false && fail())) == false);
    assert((false || (true || fail())) == true);
    assert((true && (true || fail())) == true);
}