FUNGUS_INTERPRETER="./fungus.sh"
TEST_DIR="tests"

test_passing () {
  ${FUNGUS_INTERPRETER} "$1" 1>/dev/null 2>/dev/null
  if [ $? -ne 0 ]
  then
    echo "[FAIL]: $1 expected to pass"
    echo "=================="
    ${FUNGUS_INTERPRETER} "$1"
    echo "=================="
    exit 1
  fi
}

test_failing () {
  ${FUNGUS_INTERPRETER} "$1" 2>&1 | grep -q -E -- "^Assertion failure"
  if [ $? -ne 0 ]
  then
    echo "[FAIL]: $1 expected to fail with assert"
    echo "=================="
    ${FUNGUS_INTERPRETER} "$1"
    echo "=================="
    exit 1
  fi
}

test_parsing_error () {
  ${FUNGUS_INTERPRETER} "$1" 2>&1 | grep -q -E -- "^(Type|Parsing|Runtime) error"
  if [ $? -ne 0 ]
  then
    echo "[FAIL]: $1 expected to fail with error"
    echo "=================="
    ${FUNGUS_INTERPRETER} "$1"
    echo "=================="
    exit 1
  fi
}

for f in "${TEST_DIR}"/*.fun
do
  case "$f" in
    "${TEST_DIR}"/fail*  ) test_failing "$f";;
    "${TEST_DIR}"/error* ) test_parsing_error "$f";;
    "${TEST_DIR}"/*      ) test_passing "$f";;
  esac
  echo "[ OK ]: $f"
done


