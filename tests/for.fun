func regular() {
  var s = 0;
  for (var i = 0; i < 10; i = i + 1) {
    s = s + i;
  }
  assert(i == 10);
  assert(s == 45);
}

func atypicalStep() {
  var s = 1;
  for (var i = 5; i > 1; i = i - 2) {
    s = s * i;
  }
  assert(s == 15);
}

func neverExecuted() {
  for (var i = 0; i > 10; i = i + 1) {
    assert(false);
  }
}

func inner() {
  var s = 0;
  for (var i = 0; i < 7; i = i + 1) {
    for (var j = 0; j < i; j = j + 2) {
      s = s + j;
    }
  }
  assert(s == 2 + 2 + 4 + 2 + 4 + 6);
}

func missingParts() {
  var s = 0;
  for (var i = 0; i < 3;) {
    for (var j = i;; j = j + 1) {
      for (;;) {
        if (j + i >= 3) {
          break;
        }
        j = j + 1;
      }

      s = s + j + i;
      if (s >= 10) {
        break;
      }
    }
  }
  assert(s == 3 + 4 + 5 + 3 + 4);
}

func main() {

}