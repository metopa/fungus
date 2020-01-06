func main() {
    var a = vec3(1, 2, 3);
    assert(len(a) == 3);
    assert(a[0] == 1);
    assert(a[1] == 2);
    assert(a[2] == 3);
    assert(a.x == 1);
    assert(a.y == 2);
    assert(a.z == 3);

    var b = vec3(10);
    assert(b == vec3(10, 10, 10));
    assert(a + b == vec3(11, 12, 13));
    assert(a - b == vec3(-9, -8, -7));
    assert(-b == vec3(-10, -10, -10));
    assert(-b * 2 + 3.5 == vec3(-16.5, -16.5, -16.5));
    assert(-b * 2 + 3.5 + a == vec3(-15.5, -14.5, -13.5));
    assert(b / 2 == vec3(5, 5, 5));
    println(a);
    assert(string(a) == "vec3[1.0, 2.0, 3.0]");
    assert(typename(a) == "vec3");
}