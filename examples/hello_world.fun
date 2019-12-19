func main() {
    print("> ");
    var name = read();
    if (name == null) {
        println("Failed to read name");
    } else {
        println(format("Hello, %s", name));
    }
}