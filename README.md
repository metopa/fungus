# Fungus

## Action plan

- [x] Minimal working example with SL nodes
- [x] Transfer node system
- [x] @operator support
- [x] Support arrays
- [ ] Support vectors
- [x] Support short circuit operators
- [ ] Support objects
- [x] Support binary operators
- [x] Support unary operators
- [x] Support control flow nodes
- [x] Use custom exceptions
- [x] Support builtin functions
- [x] Implement builtins
- [x] Add clang-format
- [x] Bool conversion support
- [x] Null comparison support
- [x] Support assert expression
- [x] Add testing script

## Fungus Types

- Primitive types
  - Boolean
  - Integer
  - Float
- Complex types
  - String
  - Structure
  - Array
  - Vector3
- Internal types
  - Function


## Keywords

- Declaration
  - `func`
  - `var`
  - `struct`
- Control flow
  - `if`
  - `while`
  - `for`
  - `break`
  - `continue`
  - `return`
- Operator
  - `=`
  - `==`, `!=`, `<`, `<=`, `=>`, `>`
  - `+`, `-`, `*`, `/`, `%`, `^`
  - `&&`, `||`, `!`
  - `@op`
- Type access
  - `.`
  - `[]`
  - `()`
- Special tokens
  - `true`
  - `false`
  - `return`
  - `break`
  - `continue`
  - `if`
  - `for`
  - `while`
  
- Standard functions
  - `print*`
  - `read`
  - `format`
  - `min`
  - `max`
  - `sqrt`
  - `rand`
  - `now`
  - `type`
  - `assert`
  - Type conversions
    - `bool`
    - `int`
    - `float`
    - `string`
    - `vec3`
    - `array<T>`
    
## Operator precedence
- `.`, `()`, `[]`
- `@op`
- `^`
- `*`, `/`, `%`
- `+`, `-`   
- `<`, `<=`, `==`, `!=`, `>=`, `>`
- `&&`
- `||`
- `=`
  
## Code samples
```
func max_value(a, b) {
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
    print(format("Hello, %s\n", name));
}
```
```
// Declare binary operator
func @dot(a, b) {
  var s = 0;
  for (var i = 0; i < len(a); i = i + 1) {
    s = s + a[i] * b[i];
  }
  return s;
}

func main() {
  var a = vec3(3);
  var b = vec3(0, 1, 2);
  if (a @dot b == 9) {
    println("Success");
  }
}
```
  
```
func fill(a) {
  var v = vec3(1, 2, 3);
  var step = vec3(3);
  for (var i = 0; i < len(a); i = i + 1) {
    a[i] = v;
    v = v + step;
  }
}

func print(a) {
  for (var i = 0; i < len(a); i = i + 1) {
    print(a[i], " ");
  }
  println();
}

func main() {
  var a[10] = vec3(0);
  fill(a);
  print(a);
}
```

```
struct Ray { origin, dir }
 
struct Sphere {
  rad,
  pos,
  emission,
  color,
  reflection
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