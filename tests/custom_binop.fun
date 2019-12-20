func @avg(a, b) {
    return (a + b) / 2;
}

func main() {
    assert(10 @avg 20 @avg 21 == 18);
}