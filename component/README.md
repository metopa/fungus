# The Fungus language component for GraalVM

Truffle languages can be packaged as components which can be installed into
GraalVM using the [Graal
updater](http://www.graalvm.org/docs/reference-manual/graal-updater/). 
Running `mvn package` in the fungus folder also builds a
`fungus-component.jar`. 
This file is the fungus languages component for GraalVM and can be installed by
running:

```
/path/to/graalvm/bin/gu install /path/to/fungus-component.jar
```

