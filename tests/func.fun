func A() {
  return 10;
}

func B(a, b) {
  return a + b * A();
}

func main() {
  assert(B(2, 13) == 132);
}