#!/usr/bin/env bash

find language/src/main/ -iname *.java | xargs clang-format-8 -i -style=file
find launcher/src/main/ -iname *.java | xargs clang-format-8 -i -style=file
find examples/smallpt-java/src/main/ -iname *.java | xargs clang-format-8 -i -style=file