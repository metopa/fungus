/*
Features:
- function declaration
- local variable declaration
- string, int, and boolean constants
- binary operators (% == = < +)
- unary operations (!)
- function call
- block declaration
- while statement
- if statement
- build-in print function
- int conversion for print
*/

func fizz_buzz(i) {
    var printed = false;
    if (i % 3 == 0) {
        print("fizz");
        printed = true;
    }
    if (i % 5 == 0) {
        print("buzz");
        printed = true;
    }
    if (!printed) {
        print(i);
    }
    print("\n");
}

func main() {
    var i = 0;
    while (i < 20) {
        fizz_buzz(i);
        i = i + 1;
    }
}