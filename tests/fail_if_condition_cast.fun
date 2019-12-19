func test_true(x) {
    var entered = false;
    if (x) {
        entered = true;
    } else {
        assert(false);
    }
    assert(entered);
}

func test_false(x) {
    var entered = false;
    if (x) {
        assert(false);
    } else {
        entered = true;
    }
    assert(entered);
}

func main() {
  test_true(true);
  test_true(false);
}