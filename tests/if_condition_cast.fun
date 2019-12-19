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
  test_false(null);
  test_true(true);
  test_false(false);
  test_true(-10);
  test_false(0);
  test_true(4.2);
  test_false(0.0);
  test_true("value");
  test_false("");
}