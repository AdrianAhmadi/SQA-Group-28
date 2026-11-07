
#!/usr/bin/env bash

set -u

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
EXPECTED_DIR="$ROOT_DIR/tests/expected"
OUTPUT_DIR="$ROOT_DIR/tests/output"

TOTAL=0
PASSED=0
FAILED=0

while IFS= read -r EXPECTED_FILE; do
    RELATIVE_PATH="${EXPECTED_FILE#"$EXPECTED_DIR"/}"
    ACTUAL_FILE="$OUTPUT_DIR/$RELATIVE_PATH"

    TOTAL=$((TOTAL + 1))

    echo "Checking: $RELATIVE_PATH"

    if [[ ! -f "$ACTUAL_FILE" ]]; then
        echo "  FAIL: Actual output file is missing."
        FAILED=$((FAILED + 1))
        continue
    fi

    if diff -q "$EXPECTED_FILE" "$ACTUAL_FILE" > /dev/null; then
        echo "  PASS"
        PASSED=$((PASSED + 1))
    else
        echo "  FAIL: Files differ."
        FAILED=$((FAILED + 1))
        diff -u "$EXPECTED_FILE" "$ACTUAL_FILE"
    fi

done < <(find "$EXPECTED_DIR" -type f \( -name "*.out" -o -name "*.atf" \) | sort)

echo
echo "================================"
echo "Comparison summary"
echo "================================"
echo "Files compared: $TOTAL"
echo "Passed: $PASSED"
echo "Failed: $FAILED"
echo "================================"

if [[ $FAILED -gt 0 ]]; then
    exit 1
fi

exit 0
