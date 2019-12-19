func main() {
  var x = 10;
  if (x < 100) {
    x = -1;
  }
  assert(x == -1);

  if (x < 0) {
    x = 4;
  } else {
    x = x * 2;
  }
  assert(x == 4);

  if (x != 4) {
    x = 0;
  } else {
    x = x + 3;
  }
  assert(x == 7);

  if (x < 10) {
    if (x % 2 == 0) {
      x = x + 1;
    } else {
      x = x - 1;
    }
  } else {
    x = 0;
  }
  assert(x == 6);
}