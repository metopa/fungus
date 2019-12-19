func main() {
  var x = 0;

  while (x < 10) {
    x = x + 1;
  }
  assert(x == 10);

  var s = 0;
  while (true) {
    x = x - 1;
    if (x % 2 == 0) {
      continue;
    }
    if (x < 0) {
      break;
    }
    s = s + x;
  }
  assert(x == -1);
  assert(s == 1 + 3 + 5 + 7 + 9);
}