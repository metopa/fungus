func foo(a) {
  if (a > 5) {
    for (var i = a; a > 8; i = i + 1) {
      while (a > 10) {
        if (a == 15) {
          return 1;
        }
        return 2;
      }
      return 3;
    }
    return 4;
  }
  return 5;
}

func main() {
  assert(foo(5)  == 5);
  assert(foo(8)  == 4);
  assert(foo(10) == 3);
  assert(foo(12) == 2);
  assert(foo(15) == 1);
}