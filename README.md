# Fungus

## Fungus Types

- Primitive types
  - Boolean
  - Integer
  - Float
  - null
- Complex types
  - String
  - Object
  - Array
  - Vector
- Internal types
  - Function


## Keywords

- Declaration
  - `func`
  - `type`
  - `var`
  - `struct`
- Control flow
  - `if`
  - `for`
  - `while`
  - `break`
  - `continue`
  - `return`
- Operator
  - `=`
  - `==`, `!=`, `<`, `<=`, `=>`, `>`
  - `+`, `-`, `*`, `/`, `%`, `^`
  - `&&`, `||`, `!`
  - `isinstanceof`
  - `@op`
- Type access
  - `.`
  - `[]`
  - `()`
- Special tokens
  - `null`
  - `true`
  - `false`
  - `bool`
  - `int`
  - `float`
  - `vec`
- Standard functions
  - `print`
  - `read`
  - `open`
  - `close`
  - `min`
  - `max`
  - `sqrt`
  - `rand`
  - `now`
    
  
## Code examples  
```
func max(a, b) {
    if (a >= b) {
      return a;
    } else {
      return b;
    }
}
```  
```
func fibb(n) {
    var a = 1;
    var b = 0;
    
    while (n > 1) {
        var c = a + b;
        b = a;
        a = c;
        n = n - 1;
    }
    
    return a;
}
```

```
func main() {
    var name = read();
    print("Hello, " + name + "\n");
}
```
```
type dir = vec<3>;

// Declare binary operator
func @dot(a, b) {
  var s = 0;
  for (var i = 0; i < a.size; i = i + 1) {
    s = s + a[i] * b[i];
  }
  return s;
}

func main() {
  var a = dir(3);
  var b = dir(0, 1, 2);
  if (a @dot b == 9) {
    print("Success");
  }
}
```

```
func min(a, b) {
  if (a isinstanceof vec) {
    var c = vec<a.size>(0);
    for (var i = 0; i < a.size; i = i + 1) {
      c[i] = min(a[i], b[i]);
    }
    return c;
  } else {
    if (a < b) {
      return a; 
    }
    return b;
  }
}
```
  
```
var A = vec<3>[10];

func fill() {
  var v = vec<3>(1, 2, 3);
  var step = vec<3>(3);
  for (var i = 0; i < A.size; i = i + 1) {
    A[i] = v;
    v = v + step;
  }
}

func save(name) {
  var f = open(name);
  if (f) {
    print(f, "header\n");
    for (var i = 0; i < A.size; i = i + 1) {
      print(f, A[i], " ");
    }
    print(f, "\n");
    close(f);
  }
}

func main() {
  fill();
  save("array.out");
}
```

```
struct Ray { origin, dir };
 
struct Sphere {
  rad,
  pos,
  emission,
  color,
  reflection,
}

func @dot(a, b) {
  return a;
}

func intersect(sphere, ray) {
  var op = sphere.pos - ray.origin;
  var eps = 1e-4;
  var b = op @dot ray.dir;
  var det = b * b - op @dot op + sphere.rad * sphere.rad;
  if (det < 0) {
    return 0;
  }
  det = sqrt(det);
  var t = b - det;
  if (t > eps) {
    return t;
  }
  t = b + det;
  if (t > eps) {
    return t;
  } else {
    return 0;
  }
}
```