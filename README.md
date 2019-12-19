# Fungus

## Action plan

- [x] Minimal working example with SL nodes
- [x] Transfer node system
- [ ] @operator support
- [ ] Support arrays
- [ ] Support vectors
- [ ] Support short circuit operators
- [ ] Check implicit type conversion
- [ ] Support objects
- [ ] Support global variables
- [ ] Support name shadowing
- [ ] Support general lvalue node
- [x] Support binary operators
- [x] Support unary operators
- [x] Support control flow nodes
- [x] Use custom exceptions
- [x] Support builtin functions
- [ ] Implement builtins
- [x] Add clang-format
- [x] Bool conversion support
- [x] Null comparison support
- [ ] Support assert expression
- [ ] Add testing script

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
  - `vec3`
  - `array<T>`
  - `assert`
  - `return`
  - `break`
  - `continue`
  - `if`
  - `for`
  - `while`
  
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
  - `format`
  - `type`
  - Type conversions
    - `bool`
    - `int`
    - `float`
    - `string`
    
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

### Declarations

#### Basic
```
var x = 10; // Global variable

func A() { // Simple function
  x = x + x; 
}

func B(a, b) { // General function
  var c = a;
  a = b;
  b = c;
  return a + b * c;
}

func main() {
  A();
  var x = B(4, 3); // function result as variable value
  // Variable name shadowing
  print(x); // standard function call
}
```

#### Custom operator
TBA

#### Structure
TBA


### Control flow

#### Inner blocks
```
func main() {
  var x = 10;
  var y = 0;
  {
    var x = 11;
    {
      y = x;
    }
  }
  assert(y == 11);
}
```

#### If
```
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
```

#### While
```
func main() {
  var x = 0;
  
  while (x < 10) {
    x = x + 1;
  }
  assert(x == 10);
  
  var s = 0;
  while (true) {
    if (x % 2 == 0) {
      continue;
    }
    s = s + x;
    x = x - 1;
    if (x < 0) {
      break;
    }
  }
  assert(x == -1);
  assert(s == 1 + 3 + 5 + 7 + 9);
}
```

#### For
```
func main() {
  var i = 0;
  var s = 0;
  for (i = 0; i < 10; i = i + 1) {
    s = s + i;
  }
  assert(i == 10);
  assert(s == 45);
  
  for (var i = 5; i > 1; i = i - 2) {
    s = s / i;
  }
  assert(i == 10); // Check variable scope
  assert(s == 3);
  
  for (var i = 0; i > 10; i = i + 1) {
    s = 0; 
  }
  assert(s == 3);
  
  s = 0;
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
```

#### Return

```
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
```
### Predefined functions
```
func main() {
  - `print`
    - `read`
    - `open`
    - `close`
    - `min`
    - `max`
    - `sqrt`
    - `rand`
    - `now`
    
    
}
```


- type initialization
- type cast
- type alias
- type check
- predefined functions
- operators
- array access
- struct access
- vector access
- recursive/forward-decl functions
### Misc
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